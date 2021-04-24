package pl.tysia.maggstone.app

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.source.LoginRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit{
        val url = NetAddressManager(application).getNetAddress()

        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesRoom(): pl.tysia.maggstone.data.Database{
        val db = Room.databaseBuilder(
            application,
            pl.tysia.maggstone.data.Database::class.java, "pl.tysia.database"
        ).build()

        return db
    }
}