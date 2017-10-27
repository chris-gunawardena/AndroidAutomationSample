package com.ibm.loginregistration;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import de.nenick.espressomacchiato.tools.EspAppDataTool;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;


    /**
     * Created by chris on 23/04/2017.
     */

    public class TestUtils {



        /**
         * When the allow permission dialog pops up, allow it
         */
        public static void allowPermissionsIfNeeded() {
            if (Build.VERSION.SDK_INT >= 23) {
                UiDevice device = UiDevice.getInstance(getInstrumentation());
                UiObject allowPermissionBtn = device.findObject(new UiSelector().text("ALLOW"));
                if (!allowPermissionBtn.exists()) {
                    allowPermissionBtn = device.findObject(new UiSelector().text("Allow"));
                    // allowPermissionBtn = device.findObject(new UiSelector().clickable(true).checkable(false).index(1));
                }
                if (allowPermissionBtn.exists()) {
                    try {
                        allowPermissionBtn.click();
                    } catch (UiObjectNotFoundException e) {
                        //Log.e(e, "There is no permissions dialog to interact with ");
                    }
                }
            }
        }

        /**
         * sleeps for x nym of millisoncs using thread sleep
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
         * Wait for view to become avaiable and timeout after x attepts
         */
        public static boolean waitForToast(Activity activity, int toastText) {
            boolean toastIsDisplayed = false;
            int count = 0;
            while(!toastIsDisplayed && count < 100) {
                try{
                    onView(withText(toastText))
                            .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
                            .check(matches(isDisplayed()));
                    toastIsDisplayed = true;
                } catch(Exception e) {
                    sleep(100);
                    count++;
                }
            }
            return toastIsDisplayed;
        }

        /**
         * Wait for view to become available and timeout after x attepts
         */
        public static boolean waitForViewToDisplay(Matcher<View> matcher) {
            int count = 0;
            while(count < 100) {
                if(viewExists(matcher)) {
                    return true;
                }
                sleep(100);
                count++;
            }
            return false;
        }

        /**
         * Returns true or values if the view exists
         * @param matcher for the view
         * @return
         */
        public static boolean viewExists(Matcher<View> matcher) {
            try {
                onView(matcher).check(matches(isDisplayed()));
                return true;
            } catch (NoMatchingViewException e) {
                return false;
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

        public static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

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

        public static void resetApp() {
            Context context = InstrumentationRegistry.getTargetContext();

            // clearing app data
            EspAppDataTool.clearApplicationData();
            EspAppDataTool.clearCache();
            EspAppDataTool.clearStorage();
            EspAppDataTool.clearDatabase();
        }


        public static Activity getCurrentActivity() {
            final Activity[] activity = new Activity[1];
            onView(isRoot()).check(new ViewAssertion() {
                @Override
                public void check(View view, NoMatchingViewException noViewFoundException) {
                    activity[0] = (Activity) view.findViewById(android.R.id.content).getContext();
                }
            });
            return activity[0];
        }
    }
}
