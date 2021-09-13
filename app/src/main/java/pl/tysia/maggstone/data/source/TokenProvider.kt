package pl.tysia.maggstone.data.source

import android.content.Context
import pl.tysia.maggstone.data.Database
import javax.inject.Inject


class TokenProvider @Inject constructor(val repo: LoginRepository) {

    fun getToken() : String{
        return repo.user!!.token
    }
}