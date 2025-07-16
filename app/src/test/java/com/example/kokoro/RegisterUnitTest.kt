package com.example.kokoro

import com.example.kokoro.repository.RegisterRepo
import com.example.kokoro.repository.RegisterRepoImpl
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


class RegisterUnitTest {
    @Mock
    private lateinit var mockAuth: FirebaseAuth

    @Mock
    private lateinit var mockTask: Task<AuthResult>

    private lateinit var userRepository: RegisterRepoImpl

    @Captor
    private lateinit var captor: ArgumentCaptor<OnCompleteListener<AuthResult>>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        userRepository = RegisterRepoImpl(mockAuth)
    }
    @Test
    fun testRegister_Successful() {
        val email = "test@example.com"
        val password = "testPassword"
        var expectedResult = "Initial Value" // Define the initial value

        // Mocking task to simulate successful registration
        `when`(mockTask.isSuccessful).thenReturn(true)
        `when`(mockAuth.createUserWithEmailAndPassword(any(), any()))
            .thenReturn(mockTask)

        // Mocking currentUser to return a mock FirebaseUser with a specific UID
        val mockFirebaseUser = mock(FirebaseUser::class.java)
        `when`(mockFirebaseUser.uid).thenReturn("testUID")
        `when`(mockAuth.currentUser).thenReturn(mockFirebaseUser)

        // Define a callback that updates the expectedResult
        val callback = { success: Boolean, message: String?, uid: String ->
            expectedResult = message ?: "Callback message is null"
        }

        // Call the function under test
        userRepository.register(email, password, callback)

        verify(mockTask).addOnCompleteListener(captor.capture())
        captor.value.onComplete(mockTask)

        // Assert the result
        assertEquals("Registration successfull", expectedResult)
    }


}