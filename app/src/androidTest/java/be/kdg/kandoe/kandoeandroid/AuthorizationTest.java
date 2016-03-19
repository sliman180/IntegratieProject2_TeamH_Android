package be.kdg.kandoe.kandoeandroid;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;
import java.util.Random;

import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static org.junit.Assert.assertNotEquals;

@LargeTest
public class AuthorizationTest {
    private static final String TAG = "auth_test";
    private static final long SLEEP_SHORT = 1000;
    private String randomUser;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @Rule
    public ActivityTestRule<FirstActivity> mActivityRule =
            new ActivityTestRule<>(FirstActivity.class);

    @Before
    public void setRandomUser() {
        randomUser = CommonMethods.generateString(10);
    }

    @Test
    public void testLogin() {
        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.username))
                .perform(typeText(ADMIN_USERNAME));
        onView(withId(R.id.password))
                .perform(typeText(ADMIN_PASSWORD));

        onView(withId(R.id.sign_in_button))
                .perform(click());

        onView(withId(R.id.drawer_layout))
                .check(matches(isDisplayed()));

        CommonMethods.goBackN(TAG);
    }

    @Test
    public void testRegister() {
        onView(withId(R.id.register_button))
                .perform(click());

        onView(withId(R.id.username))
                .perform(typeText(randomUser));
        onView(withId(R.id.password))
                .perform(typeText(randomUser));
        onView(withId(R.id.password2))
                .perform(typeText(randomUser));
        onView(withId(R.id.email))
                .perform(typeText(randomUser + "@mail.com"));

        onView(withId(R.id.register_button))
                .perform(click());

        CommonMethods.sleep(SLEEP_SHORT);

        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));

        onView(withId(R.id.username))
                .perform(typeText(randomUser));
        onView(withId(R.id.password))
                .perform(typeText(randomUser));

        onView(withId(R.id.sign_in_button))
                .perform(click());

        CommonMethods.sleep(SLEEP_SHORT);

        onView(withId(R.id.drawer))
                .check(matches(isDisplayed()));

        CommonMethods.goBackN(TAG);
    }

    @Test
    public void logOff() {
        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.username))
                .perform(typeText(ADMIN_USERNAME));
        onView(withId(R.id.password))
                .perform(typeText(ADMIN_PASSWORD));

        onView(withId(R.id.sign_in_button))
                .perform(click());

        String token = SharedPreferencesMethods.getFromSharedPreferences
                (CommonMethods.getActivityInstance(), CommonMethods.getActivityInstance().getString(R.string.token));

        assertNotEquals("", token);

        onView(withId(R.id.drawer))
                .perform(CommonMethods.actionOpenDrawer());

        CommonMethods.sleep(SLEEP_SHORT);

        onView(withText("Afmelden"))
                .perform(click());

        CommonMethods.sleep(SLEEP_SHORT);

        onView(withId(R.id.login_button))
                .check(matches(isDisplayed()));

        token = SharedPreferencesMethods.getFromSharedPreferences
                (CommonMethods.getActivityInstance(), CommonMethods.getActivityInstance().getString(R.string.token));

        assertEquals("", token);

        CommonMethods.goBackN(TAG);
    }
}
