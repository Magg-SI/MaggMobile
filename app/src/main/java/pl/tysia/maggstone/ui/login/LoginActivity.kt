package pl.tysia.maggstone.ui.login

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.children
import kotlinx.android.synthetic.main.activity_login.*
import pl.tysia.maggstone.ui.main.MainActivity

import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.data.NetworkChangeReceiver
import pl.tysia.maggstone.ui.BaseActivity
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    private var testingStarted : Boolean = false

    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MaggApp).appComponent.inject(this)

        when(loginViewModel.loginRepository.mode){
            null, "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        setContentView(R.layout.activity_login)

        NetworkChangeReceiver().enable(this)

        NetworkChangeReceiver.internetConnection.observe(this, Observer {
            if (it && loginViewModel.loginRepository.isLoggedIn && !testingStarted){
                showBlockingLoading(true)

                loginViewModel.testToken()

                testingStarted = true
            }
        })

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)

        loginViewModel.result.observe(this@LoginActivity, Observer {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            showBlockingLoading(false)
        })

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })


        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)

            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)

                setResult(Activity.RESULT_OK)

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            showBlockingLoading(false)

        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->{
                        showBlockingLoading(true)
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                    }

                }
                false
            }

            login.setOnClickListener {
                showBlockingLoading(true)
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {

    }


    override fun showBlockingLoading(show: Boolean) {
        container.children.forEach {
            if (!show) it.visibility = View.VISIBLE
            else it.visibility = View.INVISIBLE
        }

        super.showBlockingLoading(show)
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}