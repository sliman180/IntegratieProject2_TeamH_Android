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

public class SubthemaTest {
    private static final String TAG = "subthema_test";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final long LONG_WAIT_TIME = 3000;

    @Rule
    public ActivityTestRule<FirstActivity> mActivityRule =
            new ActivityTestRule<>(FirstActivity.class);

    @Before
    public void login() {
        onView(withId(R.id.aanmeld_button))
                .perform(click());

        onView(withId(R.id.gebruikersnaam))
                .perform(typeText(ADMIN_USERNAME));
        onView(withId(R.id.wachtwoord))
                .perform(typeText(ADMIN_PASSWORD));

        onView(withId(R.id.aanmeld_button))
                .perform(click());

        // wait for the response from kandoe api
        GemeenschappelijkeMethoden.sleep(LONG_WAIT_TIME);
    }

    @Test
    public void openSubthema() {
        onView(withId(R.id.drawer))
                .perform(GemeenschappelijkeMethoden.actionOpenDrawer());

        onView(withText("Mijn deelnames"))
                .perform(click());

        onView(withId(R.id.deelname_header))
                .check(matches(isDisplayed()));
    }
}
