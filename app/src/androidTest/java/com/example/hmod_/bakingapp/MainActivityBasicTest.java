package com.example.hmod_.bakingapp;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import junit.extensions.ActiveTestSuite;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Test
    public void clickCard_OpensDetailNutella() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.title_details)).check(matches(withText("Nutella Pie")));
    }

    @Test
    public void clickCard_OpensDetailBrownies() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.title_details)).check(matches(withText("Brownies")));
    }

    @Test
    public void clickCard_OpensDetailYellow() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.title_details)).check(matches(withText("Yellow Cake")));
    }

    @Test
    public void clickCard_OpensDetailCheesecake() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(withId(R.id.title_details)).check(matches(withText("Cheesecake")));
    }


}
