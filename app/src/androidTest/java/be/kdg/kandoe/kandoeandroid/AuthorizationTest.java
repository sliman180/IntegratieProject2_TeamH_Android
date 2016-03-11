package be.kdg.kandoe.kandoeandroid;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

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
        randomUser = generateString(6);
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

        goBackN();
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


        onView(withId(R.id.username))
                .perform(typeText(randomUser));
        onView(withId(R.id.password))
                .perform(typeText(randomUser));

        onView(withId(R.id.sign_in_button))
                .perform(click());

        onView(withId(R.id.drawer_layout))
                .check(matches(isDisplayed()));

        goBackN();
    }

//    @Test
//    public void logOff() {
//        onView(withId(R.id.login_button))
//                .perform(click());
//
//        onView(withId(R.id.username))
//                .perform(typeText(ADMIN_USERNAME));
//        onView(withId(R.id.password))
//                .perform(typeText(ADMIN_PASSWORD));
//
//        onView(withId(R.id.sign_in_button))
//                .perform(click());
//
//        onView(withId(R.id.drawer_layout))
//                .perform(actionOpenDrawer());
//
//        sleep(SLEEP_SHORT);
//
//        onView(withText("Afmelden"))
//                .perform(click());
//
//        onView(withId(R.id.login_button))
//                .check(matches(isDisplayed()));
//
//        String token = SharedPreferencesMethods.getFromSharedPreferences
//                (new MainActivity(), (new MainActivity()).getString(R.string.token));
//
//        assertEquals("", token);
//
//        goBackN();
//    }

    /**
     * This method exists to fix a bug in espresso where the app refuses to
     * return to the activity where it began for a new test.
     */
    private void goBackN() {
        final int N = 10; // how many times to hit back button
        try {
            for (int i = 0; i < N; i++)
                Espresso.pressBack();
        } catch (NoActivityResumedException e) {
            Log.e(TAG, "Closed all activities", e);
        }
    }

    private static String generateString(int length)
    {
        Random rng = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    private static ViewAction actionOpenDrawer() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "open drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).openDrawer(GravityCompat.START);
            }
        };
    }

    private void sleep(long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
