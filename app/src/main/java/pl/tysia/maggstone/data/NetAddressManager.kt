package pl.tysia.maggstone.data

import android.content.Context
import android.content.Intent
import androidx.preference.PreferenceManager
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.SettingsActivity

class NetAddressManager(context : Context) {
    private var privateNetAddress : String? = null
    private var globalNetAddress : String = "http://martech.magg.pl/"
    private var privateNetUsed : Boolean = false

    init {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        privateNetUsed = prefs.getBoolean("private_net_usage", false)

        val privateNetAddress = prefs.getString("net_address_private", null)
        val globalNetAddress = prefs.getString("net_address_global", null)

        if (privateNetUsed && privateNetAddress == null)
            okDialog("Brak adresu sieci prywatnej",
                "Wyłącz korzystanie z sieci prywatnej lub podaj jej adres",
                context) {context.startActivity(Intent(context, SettingsActivity::class.java))}

        else if (globalNetAddress != null){
            this.globalNetAddress = globalNetAddress
        }

        this.privateNetAddress = privateNetAddress

    }

    fun getNetAddress() : String {
        return if (privateNetUsed) privateNetAddress!!
        else globalNetAddress!!
    }
}