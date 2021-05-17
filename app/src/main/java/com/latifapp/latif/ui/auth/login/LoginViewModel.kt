package com.latifapp.latif.ui.auth.login

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.latifapp.latif.data.local.AppPrefsStorage
import com.latifapp.latif.ui.base.BaseViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(appPrefsStorage: AppPrefsStorage):BaseViewModel(appPrefsStorage) {

    val validateLiveData= MutableLiveData<SignUpFiled>(null)
    public fun login(username:String,password:String){
        if (validate(username,password)){}
    }

    private fun validate(username:String,password:String):Boolean{
        var valid =true
        if (Patterns.EMAIL_ADDRESS.matcher(username).matches()||Patterns.PHONE.matcher(username).matches()){

        }else {
            validateLiveData.value= SignUpFiled.email_

            valid=false
        }

        if (password.length>=6){

        }else {
            validateLiveData.value= SignUpFiled.password_
            valid=false
        }
        validateLiveData.value= null
        return valid
    }

    enum class SignUpFiled{
        email_,password_,phone_,name_,address_,country_,city_,governorate_
    }
}