package com.example.kokoro.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kokoro.R
import com.example.kokoro.databinding.ActivityLoginBinding
import com.example.kokoro.repository.UserRepositoryImpl
import com.example.kokoro.utils.LoadingUtils
import com.example.kokoro.viewModel.UserViewModel

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var userViewModel: UserViewModel
    lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingUtils = LoadingUtils(this)

        var repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        binding.loginBtn.setOnClickListener {
            loadingUtils.show()

            var email = binding.emailLogin.text.toString()
            var password = binding.passwordLogin.text.toString()

            userViewModel.login(email, password) { success, message ->
                if (success) {
                    loadingUtils.dismiss()

                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    var intent = Intent(
                        this@LoginActivity,
                        DashboardActivity::class.java
                    )
                    startActivity(intent)
                } else {
                    loadingUtils.dismiss()

                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.registerLink.setOnClickListener {
            val intent = Intent(
                this@LoginActivity,
                RegisterActivity::class.java
            )
            startActivity(intent)
        }
        binding.forgotPasswordLink.setOnClickListener {
            val intent = Intent(
                this@LoginActivity, ForgotPasswordActivity::class.java
            )
            startActivity(intent)
        }
    }
}