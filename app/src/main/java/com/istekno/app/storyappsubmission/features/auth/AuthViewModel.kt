package com.istekno.app.storyappsubmission.features.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istekno.app.core.data.source.remote.model.Login
import com.istekno.app.core.data.source.remote.model.Register
import com.istekno.app.core.utils.Validator
import com.istekno.app.core.domain.repository.AuthRepository
import com.istekno.app.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {

    private var _loginData = MutableLiveData<Resource<Login.Response>>()
    var loginData: MutableLiveData<Resource<Login.Response>>
        get() = _loginData
        set(value) {
            _loginData = value
        }

    private var _registerData = MutableLiveData<Resource<Validator.Response>>()
    var registerData: MutableLiveData<Resource<Validator.Response>>
        get() = _registerData
        set(value) {
            _registerData = value
        }

    fun userLogin(request: Login.Request) {
        loginData.postValue(Resource.loading())

        viewModelScope.launch {
            try {
                val result = authRepository.login(request)
                if (!result.error)
                    loginData.postValue(Resource.success(result))
                else
                    loginData.postValue(Resource.error(result.message))

            } catch (e: Exception) {
                e.printStackTrace()
                loginData.postValue(Resource.error(e.message))
            }
        }
    }

    fun userRegister(request: Register.Request) {
        registerData.postValue(Resource.loading())

        viewModelScope.launch {
            try {
                val result = authRepository.register(request)
                if (!result.error)
                    registerData.postValue(Resource.success(result))
                else
                    registerData.postValue(Resource.error(result.message))

            } catch (e: Exception) {
                e.printStackTrace()
                registerData.postValue(Resource.error(e.message))
            }
        }
    }
}