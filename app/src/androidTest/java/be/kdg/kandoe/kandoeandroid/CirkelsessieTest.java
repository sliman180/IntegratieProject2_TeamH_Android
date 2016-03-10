package be.kdg.kandoe.kandoeandroid;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import be.kdg.kandoe.kandoeandroid.pojo.Spelkaart;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@LargeTest
public class CirkelsessieTest {
    private static final String TAG = "cirkel_test";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final long LONG_WAIT_TIME = 3000;
    private static final long SHORT_WAIT_TIME = 1000;

    @Rule
    public ActivityTestRule<FirstActivity> mActivityRule =
            new ActivityTestRule<>(FirstActivity.class);

    @Before
    public void login() {
        onView(withId(R.id.login_button))
                .perform(click());

        onView(withId(R.id.username))
                .perform(typeText(ADMIN_USERNAME));
        onView(withId(R.id.password))
                .perform(typeText(ADMIN_PASSWORD));

        onView(withId(R.id.sign_in_button))
                .perform(click());

        // wait for the response from kandoe api
        // TODO: properly implement idlingresource stuff. http://stackoverflow.com/questions/30155227/espresso-how-to-wait-for-some-time1-hour
        sleep(LONG_WAIT_TIME);
    }

    @Test
    public void testCircleSessionsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.listview))
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.circle_length))
                .check(matches(isDisplayed()));

        goBackN();
    }

    @Test
    public void testCreateCard() {
        String typedText = generateString(10);
        onData(anything())
                .inAdapterView(withId(R.id.listview))
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.buttonAddKaart))
                .perform(click());

        sleep(SHORT_WAIT_TIME);

        onView(withId(R.id.dialogtext))
                .perform(typeText(typedText));

        onView(withId(R.id.dialogButtonOK))
                .perform(click());

        sleep(SHORT_WAIT_TIME);

        onView(withText(typedText))
                .check(matches(isDisplayed()));
    }

//    @Test
//    public void moveCard() {
//        onData(anything())
//                .inAdapterView(withId(R.id.listview))
//                .atPosition(0)
//                .perform(click());
//
//        onData(anything())
//                .inAdapterView(withId(R.id.card_list))
//                .atPosition(0)
//                .perform(getText(isDisplayed()));
//
//
//    }
//
//    String getText(final Matcher<View> matcher) {
//        final String[] stringHolder = { null };
//        onView(matcher).perform(new ViewAction() {
//            @Override
//            public Matcher<View> getConstraints() {
//                return isAssignableFrom(TextView.class);
//            }
//
//            @Override
//            public String getDescription() {
//                return "getting text from a ListItem";
//            }
//
//            @Override
//            public void perform(UiController uiController, View view) {
//                TextView tv = (TextView) view; //Save, because of check in getConstraints()
//                stringHolder[0] = tv.getText().toString();
//            }
//        });
//        return stringHolder[0];
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

    private void sleep(long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
