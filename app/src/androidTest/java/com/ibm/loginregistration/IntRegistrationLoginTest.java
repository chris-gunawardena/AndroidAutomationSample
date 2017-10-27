package com.ibm.loginregistration;


import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class IntRegistrationLoginTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.wireMockConfig().port(8080)
            .notifier(new ConsoleNotifier(true)), false);

    @Before
    public void setup() {
        wireMockRule.stubFor(any(urlEqualTo("/v2/configuration"))
                .atPriority(5).willReturn(aResponse()
                        .withBody(asset("testdata/configuration.json"))));
        mActivityTestRule.launchActivity(null);
    }


    @Test
    public void e2ERegisterLoginTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.tv_register), withText("Not Registered ? Register Now !"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_frame),
                                        0),
                                4),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_name),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Chris"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.et_email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        Random r = new Random();
        int randomNumber = r.nextInt(99999) + 1;
        appCompatEditText2.perform(replaceText("chris" + randomNumber + "@test.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.et_password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("zxzxzx"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.et_password), withText("zxzxzx"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                0),
                        isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_register), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_frame),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

        sleep(3000);

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.tv_login), withText("Already Registered ? Login Now !"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_frame),
                                        0),
                                5),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.et_email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("chris" + randomNumber + "@test.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.et_password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("zxzxzx"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.btn_login), isDisplayed()));
        appCompatEditText7.perform(click());

        sleep(3000);

        ViewInteraction textView = onView(
                allOf(withId(R.id.tv_name), withText("Welcome : Chris"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_frame),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Welcome : Chris")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /**
     * sleeps for x num of milliseconds using thread sleep
     * @param mils
     */
    public static void sleep(int mils) {
        try {
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the content of a file in the path
     * @param assetPath File path
     * @return
     */
    public static String asset(String assetPath) {
        Context context = getInstrumentation().getContext();
        try {
            InputStream inputStream = context.getAssets().open(assetPath);
            return inputStreamToString(inputStream, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static final int BUFFER_SIZE = 4 * 1024;
    private static String inputStreamToString(InputStream inputStream, String charsetName)
            throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream, charsetName);
        char[] buffer = new char[BUFFER_SIZE];
        int length;
        while ((length = reader.read(buffer)) != -1) {
            builder.append(buffer, 0, length);
        }
        return builder.toString();
    }

}
