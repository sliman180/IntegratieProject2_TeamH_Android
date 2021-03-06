package be.kdg.kandoe.kandoeandroid;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class DeelnamesTest {
    private static final String TAG = "deelnames_test";

    @Rule
    public ActivityTestRule<FirstActivity> mActivityRule =
            new ActivityTestRule<>(FirstActivity.class);

    @Before
    public void login() {
        onView(withId(R.id.aanmeld_button))
                .perform(click());

        onView(withId(R.id.gebruikersnaam))
                .perform(typeText(GemeenschappelijkeMethoden.GEBRUIKERSNAAM));
        onView(withId(R.id.wachtwoord))
                .perform(typeText(GemeenschappelijkeMethoden.WACHTWOORD));

        onView(withId(R.id.aanmeld_button))
                .perform(click());

        // wait for the response from kandoe api
        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.LONG_WAIT_TIME);
    }

    @Test
    public void openDeelnames() {
        onView(withId(R.id.drawer))
                .perform(GemeenschappelijkeMethoden.actionOpenDrawer());

        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.SHORT_WAIT_TIME);

        onView(withText("Mijn deelnames"))
                .perform(click());

        onView(withId(R.id.deelname_header))
                .check(matches(isDisplayed()));
    }

}
