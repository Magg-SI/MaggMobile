package pl.tysia.maggstone.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.settings_activity.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.ui.login.LoginActivity
import pl.tysia.maggstone.ui.login.LoginViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var listener : SharedPreferences.OnSharedPreferenceChangeListener;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

        loginViewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        )
            .get(LoginViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val user =  LoginRepository(LoginDataSource(NetAddressManager(this@SettingsActivity)),this@SettingsActivity).user!!

        title_username.text = user.displayName

        val prefs = PreferenceManager.getDefaultSharedPreferences(this);

        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, s: String ->
            if (s == "mode_preference") {
                val mode = sharedPreferences.getString("mode_preference", "auto")

                when(mode){
                    "auto" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener);

    }

    fun onLogoutClicked(view: View){
        loginViewModel.logout()

        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

}