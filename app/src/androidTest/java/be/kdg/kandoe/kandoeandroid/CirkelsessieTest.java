package be.kdg.kandoe.kandoeandroid;

import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
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
        CommonMethods.sleep(LONG_WAIT_TIME);
    }

    @Test
    public void testCircleSessionsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.listview))
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.buttonInfo))
                .check(matches(isDisplayed()));

        CommonMethods.goBackN(TAG);
    }

    @Test
    public void testCreateCard() {
        String typedText = CommonMethods.generateString(10);

        onView(withId(R.id.buttonGestart)).perform(click());

        CommonMethods.sleep(SHORT_WAIT_TIME);

        onData(anything())
                .inAdapterView(withId(R.id.listview))
                .atPosition(0)
                .perform(click());

        CommonMethods.sleep(SHORT_WAIT_TIME);

        Button deelnameButton = (Button) CommonMethods.getActivityInstance().findViewById(R.id.buttonDeelname);
        if (deelnameButton.isEnabled()) {
            onView(withId(R.id.buttonDeelname))
                    .perform(click());
            onView(withText(R.string.yes))
                    .perform(click());
        }

        CommonMethods.sleep(SHORT_WAIT_TIME);

        onView(withText("Sessie"))
                .perform(click());

        CommonMethods.sleep(LONG_WAIT_TIME);

        onView(withId(R.id.buttonAddKaart))
                .perform(click());

        CommonMethods.sleep(SHORT_WAIT_TIME);

        onView(withId(R.id.dialogtext))
                .perform(typeText(typedText));

        onView(withId(R.id.dialogButtonOK))
                .perform(click());

        CommonMethods.sleep(SHORT_WAIT_TIME);

        onView(withText(typedText))
                .check(matches(isDisplayed()));
    }

    @Test
    public void chat() {
        String typedText = CommonMethods.generateString(10);
        onData(anything())
                .inAdapterView(withId(R.id.listview))
                .atPosition(0)
                .perform(click());

        CommonMethods.sleep(SHORT_WAIT_TIME);

        onView(withText("Chat"))
                .perform(click());

        onView(withId(R.id.etChat))
                .perform(typeText(typedText));

        CommonMethods.sleep(SHORT_WAIT_TIME);

        onView(withText(typedText))
                .check(matches(isDisplayed()));
    }

//    @Test
//    public void moveCard() {
//        String typedText = generateString(10);
//
//        onData(anything())
//                .inAdapterView(withId(R.id.listview))
//                .atPosition(0)
//                .perform(click());
//
//        sleep(SHORT_WAIT_TIME);
//
//        onView(withId(R.id.buttonAddKaart))
//                .perform(click());
//
//        sleep(SHORT_WAIT_TIME);
//
//        onView(withId(R.id.dialogtext))
//                .perform(typeText(typedText));
//
//        onView(withId(R.id.dialogButtonOK))
//                .perform(click());
//
//        onView(withText(typedText))
//                .perform(click());
//
//        onData(anything())
//                .inAdapterView(withId(R.id.card_list))
//                .atPosition(0)
//                .check(assertThat("", ));
//
//        onData(withItemContent(""))
//                .inAdapterView(withId(R.id.card_list))
//                .atPosition(0)
//                .check(matches());
//    }

}
