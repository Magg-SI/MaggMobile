package pl.tysia.maggstone.app

import android.app.Application
import pl.tysia.maggstone.data.source.LoginRepository
import javax.inject.Inject

class MaggApp: Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        this.appComponent = this.initDagger()

        appComponent.inject(this)
    }

    private fun initDagger() = DaggerApplicationComponent.builder()
        .appModule(AppModule(this))
        .build()

    @Inject
    lateinit var userRepository : LoginRepository

}