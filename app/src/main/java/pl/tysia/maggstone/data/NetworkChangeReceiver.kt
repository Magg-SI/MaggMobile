package pl.tysia.maggstone.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import pl.tysia.maggstone.data.model.Error

private const val ERROR_SOURCE = "pl.tysia.maggstone.error_source_internet_connection"

class NetworkChangeReceiver : ConnectivityManager.NetworkCallback() {
    companion object{
        private val _internetConnection = MutableLiveData<Boolean>()
        val internetConnection : LiveData<Boolean> = _internetConnection
    }

    private var db : Database? = null


    var networkRequest: NetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()


    fun enable(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, this)


        db = Room.databaseBuilder(
            context,
            Database::class.java, "pl.tysia.database"
        ).build()
    }

    override fun onLost(network: Network) {
        db?.errorsDAO()?.insert(
            Error(
                Error.TYPE_CONNECTION,
                ERROR_SOURCE, "Brak łączności z internetem", "")
        )

        _internetConnection.postValue(false)

    }

    override fun onAvailable(network: Network) {
        db?.errorsDAO()?.clearSource(ERROR_SOURCE)

        _internetConnection.postValue(true)

    }

}