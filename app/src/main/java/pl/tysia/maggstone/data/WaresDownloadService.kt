package pl.tysia.maggstone.data

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.room.Room

class WaresDownloadService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val res = super.onStartCommand(intent, flags, startId)

        WaresDownloadThread().start()

        return res

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    inner class WaresDownloadThread : Thread() {

        override fun run() {
            super.run()

            val databaseChanged = WareDataSourceMock().databaseChanged()
            if (databaseChanged is Result.Success && !databaseChanged.data) stopSelf()

            val result = WareDataSourceMock().getWares()
            if (result is Result.Success) {
                val wares = result.data

                val db = Room.databaseBuilder(
                    this@WaresDownloadService,
                    Database::class.java, "pl.tysia.wares_database"
                ).build()

                db.waresDao().refillTable(wares)

            }

            stopSelf()

        }
    }
}