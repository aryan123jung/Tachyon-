package com.example.kokoro.viewModel


import androidx.lifecycle.MutableLiveData
import com.example.kokoro.model.UserModel
import com.example.kokoro.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel(val repo: UserRepository) {

    fun login(email:String, password:String,
              callback:(Boolean,String) -> Unit){
        repo.login(email,password,callback)
    }

    fun register(email: String,password: String,
                 callback: (Boolean,String, String) -> Unit){
        repo.register(email,password,callback)
    }

    fun forgotPassword(email: String,
                       callback: (Boolean, String) -> Unit){
        repo.forgotPassword(email,callback)
    }

    fun addUserToDatabase(userId:String, userModel: UserModel,
                          callback: (Boolean, String) -> Unit){
        repo.addUserToDatabase(userId,userModel,callback)
    }

    fun logout(callback: (Boolean, String) -> Unit){

    }

    fun editProfile(userId: String, data: MutableMap<String,Any>,
                    callback: (Boolean, String) -> Unit){

    }

    fun getCurrentUser(): FirebaseUser?{
        return repo.getCurrentUser()
    }

    var _userData = MutableLiveData<UserModel?>()
    var userData = MutableLiveData<UserModel?>()
        get() = _userData

    fun getUserFromDatabase(userId: String,
                            callback: (Boolean, String) -> Unit){
        repo.getUserFromDatabase(userId){
                user,success, message ->
            if(success){
                _userData.value = user
            }
        }
    }

}