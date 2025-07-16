package com.example.kokoro

import com.example.kokoro.repository.AuthRepoImpl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

class AuthUnitTest {
    @Mock
    private lateinit var mockAuth: FirebaseAuth

    @Mock
    private lateinit var mockTask: Task<AuthResult>

    private lateinit var authRepo: AuthRepoImpl

    @Captor
    private lateinit var captor: ArgumentCaptor<OnCompleteListener<AuthResult>>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        authRepo = AuthRepoImpl(mockAuth)
    }

    @Test
    fun testLogin_Successful() {
        val email = "test@example.com"
        val password = "testPassword"
        var actualMessage: String? = null // Stores the callback message

        // Mock Firebase Authentication method
        `when`(mockAuth.signInWithEmailAndPassword(any(), any()))
            .thenReturn(mockTask)

        // Mock successful task completion
        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockTask.addOnCompleteListener(any())).thenAnswer { invocation ->
            val listener = invocation.arguments[0] as OnCompleteListener<AuthResult>
            listener.onComplete(mockTask)
            mockTask // Ensure method chaining works
        }

        // Define the callback function
        val callback = { success: Boolean, message: String? ->
            actualMessage = message
        }

        // Call the function under test
        authRepo.login(email, password, callback)

        // Verify that addOnCompleteListener was actually called
        verify(mockTask).addOnCompleteListener(captor.capture())

        // Simulate task completion
        captor.value.onComplete(mockTask)

        // Assert expected result
        assertEquals("Logging In", actualMessage)
    }
}
