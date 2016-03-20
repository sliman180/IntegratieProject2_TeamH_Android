package be.kdg.kandoe.kandoeandroid;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

public class CirkelsessieTest {
    private static final String TAG = "cirkel_test";

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
        // TODO: properly implement idlingresource stuff. http://stackoverflow.com/questions/30155227/espresso-how-to-wait-for-some-time1-hour
        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.LONG_WAIT_TIME);
    }

    @Test
    public void testCircleSessionsDisplayed() {
        onData(anything())
                .inAdapterView(withId(R.id.listview))
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.buttonInfo))
                .check(matches(isDisplayed()));

        GemeenschappelijkeMethoden.goBackN(TAG);
    }

    @Test
    public void testCreateCard() {
        String typedText = GemeenschappelijkeMethoden.generateString(10);

        onView(withId(R.id.buttonGestart)).perform(click());

        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.SHORT_WAIT_TIME);

        onData(anything())
                .inAdapterView(withId(R.id.listview))
                .atPosition(0)
                .perform(click());

        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.SHORT_WAIT_TIME);

        Button deelnameButton = (Button) GemeenschappelijkeMethoden.getActivityInstance().findViewById(R.id.buttonDeelname);
        if (deelnameButton.isEnabled()) {
            onView(withId(R.id.buttonDeelname))
                    .perform(click());
            onView(withText(R.string.yes))
                    .perform(click());
        }

        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.SHORT_WAIT_TIME);

        onView(withText("Sessie"))
                .perform(click());

        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.LONG_WAIT_TIME);

        onView(withId(R.id.buttonAddKaart))
                .perform(click());

        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.SHORT_WAIT_TIME);

        onView(withId(R.id.dialogtext))
                .perform(typeText(typedText));

        onView(withId(R.id.dialogButtonOK))
                .perform(click());

        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.SHORT_WAIT_TIME);

        onView(withText(typedText))
                .check(matches(isDisplayed()));
    }

    @Test
    public void chat() {
        String typedText = GemeenschappelijkeMethoden.generateString(10);

        onView(withId(R.id.buttonGestart)).perform(click());

        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.SHORT_WAIT_TIME);

        onData(anything())
                .inAdapterView(withId(R.id.listview))
                .atPosition(0)
                .perform(click());

        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.SHORT_WAIT_TIME);

        onView(withText("Chat"))
                .perform(click());

        onView(withId(R.id.etChat))
                .perform(typeText(typedText));

        GemeenschappelijkeMethoden.sleep(GemeenschappelijkeMethoden.SHORT_WAIT_TIME);

        onView(withText(typedText))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btSend)).perform(click());
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
