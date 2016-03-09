package be.kdg.kandoe.kandoeandroid;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
public class AuthorizationTest {
    private static final String TAG = "auth_test";
    private String randomUser;

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
                .perform(typeText("admin"));
        onView(withId(R.id.password))
                .perform(typeText("admin"));

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
}
