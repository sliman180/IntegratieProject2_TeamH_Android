package be.kdg.kandoe.kandoeandroid;

import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@LargeTest
public class AutorisatieTest {
    private static final String TAG = "auth_test";
    private static final long SLEEP_SHORT = 1000;
    private String randomGebruiker;
    private static final String GEBRUIKERSNAAM = "userone";
    private static final String WACHTWOORD = "userone";

    @Rule
    public ActivityTestRule<FirstActivity> mActivityRule =
            new ActivityTestRule<>(FirstActivity.class);

    @Before
    public void setRandomUser() {
        randomGebruiker = GemeenschappelijkeMethoden.generateString(10);
    }

    @Test
    public void testLogin() {
        onView(withId(R.id.aanmeld_button))
                .perform(click());

        onView(withId(R.id.gebruikersnaam))
                .perform(typeText(GEBRUIKERSNAAM));
        onView(withId(R.id.wachtwoord))
                .perform(typeText(WACHTWOORD));

        onView(withId(R.id.aanmeld_button))
                .perform(click());

        onView(withId(R.id.drawer_layout))
                .check(matches(isDisplayed()));

        GemeenschappelijkeMethoden.goBackN(TAG);
    }

    @Test
    public void testRegister() {
        onView(withId(R.id.registreer_button))
                .perform(click());

        onView(withId(R.id.gebruikersnaam))
                .perform(typeText(randomGebruiker));
        onView(withId(R.id.wachtwoord))
                .perform(typeText(randomGebruiker));
        onView(withId(R.id.wachtwoord2))
                .perform(typeText(randomGebruiker));
        onView(withId(R.id.email))
                .perform(typeText(randomGebruiker + "@mail.com"));

        onView(withId(R.id.registreer_button))
                .perform(click());

        GemeenschappelijkeMethoden.sleep(SLEEP_SHORT);

        onView(withId(R.id.aanmeld_button)).check(matches(isDisplayed()));

        onView(withId(R.id.gebruikersnaam))
                .perform(typeText(randomGebruiker));
        onView(withId(R.id.wachtwoord))
                .perform(typeText(randomGebruiker));

        onView(withId(R.id.aanmeld_button))
                .perform(click());

        GemeenschappelijkeMethoden.sleep(SLEEP_SHORT);

        onView(withId(R.id.drawer))
                .check(matches(isDisplayed()));

        GemeenschappelijkeMethoden.goBackN(TAG);
    }

    @Test
    public void logOff() {
        onView(withId(R.id.aanmeld_button))
                .perform(click());

        onView(withId(R.id.gebruikersnaam))
                .perform(typeText(GEBRUIKERSNAAM));
        onView(withId(R.id.wachtwoord))
                .perform(typeText(WACHTWOORD));

        onView(withId(R.id.aanmeld_button))
                .perform(click());

        String token = SharedPreferencesMethods.getFromSharedPreferences
                (GemeenschappelijkeMethoden.getActivityInstance(), GemeenschappelijkeMethoden.getActivityInstance().getString(R.string.token));

        assertNotEquals("", token);

        onView(withId(R.id.drawer))
                .perform(GemeenschappelijkeMethoden.actionOpenDrawer());

        GemeenschappelijkeMethoden.sleep(SLEEP_SHORT);

        onView(withText("Afmelden"))
                .perform(click());

        GemeenschappelijkeMethoden.sleep(SLEEP_SHORT);

        onView(withId(R.id.aanmeld_button))
                .check(matches(isDisplayed()));

        token = SharedPreferencesMethods.getFromSharedPreferences
                (GemeenschappelijkeMethoden.getActivityInstance(), GemeenschappelijkeMethoden.getActivityInstance().getString(R.string.token));

        assertEquals("", token);

        GemeenschappelijkeMethoden.goBackN(TAG);
    }
}
