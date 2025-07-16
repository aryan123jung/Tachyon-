package com.example.kokoro

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.kokoro.ui.activity.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testLoginSuccess() {
        // Input valid email and password
        onView(withId(R.id.emailLogin)).perform(typeText("test@example.com"))
        onView(withId(R.id.passwordLogin)).perform(typeText("password123"))

        closeSoftKeyboard()  // Close the keyboard

        // Click the login button
        onView(withId(R.id.loginBtn)).perform(click())

        // Check if the DashboardActivity is launched by verifying a view within it
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun testLoginFailure() {
        // Input invalid email and password
        onView(withId(R.id.emailLogin)).perform(typeText("invalid@example.com"))
        onView(withId(R.id.passwordLogin)).perform(typeText("wrongpassword"))

        closeSoftKeyboard()  // Close the keyboard

        // Click the login button
        onView(withId(R.id.loginBtn)).perform(click())

        // Check if the error message is displayed
        onView(withText("Invalid credentials")).check(matches(isDisplayed()))
    }
}
