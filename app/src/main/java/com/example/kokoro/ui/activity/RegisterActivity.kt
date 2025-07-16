package com.example.kokoro.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kokoro.R
import com.example.kokoro.databinding.ActivityRegisterBinding
import com.example.kokoro.model.UserModel
import com.example.kokoro.repository.UserRepositoryImpl
import com.example.kokoro.utils.LoadingUtils
import com.example.kokoro.viewModel.UserViewModel

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var userViewModel: UserViewModel
    lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)
        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        binding.registerBtn.setOnClickListener{
            loadingUtils.show()

            var username = binding.usernameRegister.text.toString()
            var email = binding.emailRegister.text.toString()
            var password = binding.passwordRegister.text.toString()

            userViewModel.register(email,password){
                    success,message,userId ->
                if(success){
                    loadingUtils.dismiss()
                    var userModel = UserModel(userId.toString(),
                        username,email)
                    addUser(userModel)
                }else{
                    loadingUtils.dismiss()
                    Toast.makeText(this@RegisterActivity,
                        message, Toast.LENGTH_SHORT).show()
                }
//
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.signinLink.setOnClickListener {
            val intent = Intent(
                this@RegisterActivity, LoginActivity::class.java
            )
            startActivity(intent)
        }
    }

    private fun addUser(userModel: UserModel){
        userViewModel.addUserToDatabase(userModel.userId,userModel){
                success,message ->
            if (success){
                Toast.makeText(this@RegisterActivity,
                    message, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@RegisterActivity,
                    message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}