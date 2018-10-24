package com.example.hmod_.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailsStepBasicTest {
    private static final String TAG = "DetailsStepBasicTest";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void DetailsStepBasicTest() {
        ViewInteraction recyclerView = onView(allOf(withId(R.id.recyclerView), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recycler_View = onView(allOf(withId(R.id.steps_list), withParent(allOf(withId(R.id.recipe_detail_fragment),
                withParent(withId(android.R.id.content)))),
                isDisplayed()));
        Log.d(TAG, "DetailsStepBasicTest: " + recycler_View);
        recycler_View.perform(actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

            ViewInteraction appCompatImageButton = onView(
                    allOf(withId(R.id.exo_play), withContentDescription("Play"), isDisplayed()));
            appCompatImageButton.perform(click());


//
    }

}
