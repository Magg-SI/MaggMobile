package pl.tysia.maggstone.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.data.Result

import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Database
import java.io.IOException
import javax.inject.Inject

class LoginViewModel @Inject constructor(val loginRepository: LoginRepository, db : Database) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result

    fun testToken(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val result = loginRepository.testToken()

                if (result is Result.Success) {
                    _result.postValue(result.data)
                } else {
                    _result.postValue(false)
                }
            }catch (ex : IOException){
                _result.postValue(false)
            }
        }
    }

    fun logout(){
        loginRepository.logout()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val result = loginRepository.login(username, password)

                if (result is Result.Success) {
                    _loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = result.data.displayName)))
                } else {
                    _loginResult.postValue(LoginResult(error = R.string.login_failed))
                }
            }catch (ex : IOException){
                _loginResult.postValue(LoginResult(error = R.string.connection_error))
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return true
    }

    private fun isPasswordValid(password: String): Boolean {
        return true
    }
}