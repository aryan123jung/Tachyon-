package com.example.kokoro

import android.content.Intent
import android.widget.Toast
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.kokoro.ui.activity.RegisterActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun testRegistration_Successful() {
        // Input username, email, and password
        onView(withId(R.id.usernameRegister)).perform(typeText("JohnDoe"))
        onView(withId(R.id.emailRegister)).perform(typeText("john.doe@example.com"))
        onView(withId(R.id.passwordRegister)).perform(typeText("Password123"))

        // Close the keyboard
        closeSoftKeyboard()

        // Click the Register button
        onView(withId(R.id.registerBtn)).perform(click())

        // Wait for the registration process (adjust the wait time as needed for the actual request)
        Thread.sleep(2000)

        // Check for a success message (Assuming there's a Toast displayed upon success)
        onView(withText("Registration successful")).check(matches(isDisplayed()))
    }

    @Test
    fun testRegistration_Failed() {
        // Input invalid email or missing required fields
        onView(withId(R.id.usernameRegister)).perform(typeText("JohnDoe"))
        onView(withId(R.id.emailRegister)).perform(typeText("invalid-email"))
        onView(withId(R.id.passwordRegister)).perform(typeText(""))

        // Close the keyboard
        closeSoftKeyboard()

        // Click the Register button
        onView(withId(R.id.registerBtn)).perform(click())

        // Wait for the registration process (adjust the wait time as needed for the actual request)
        Thread.sleep(2000)

        // Check for an error message (Toast for failed registration)
        onView(withText("Invalid email format")).check(matches(isDisplayed()))
    }
}
