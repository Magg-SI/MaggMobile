package pl.tysia.maggstone.data.logic

import pl.tysia.maggstone.constants.AccessibleFunctionsTypes
import pl.tysia.maggstone.constants.UserTypes
import pl.tysia.maggstone.data.model.UserAccessibleFunctions
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.data.source.TokenProvider
import retrofit2.Retrofit
import javax.inject.Inject

class UserAccessLogic @Inject constructor(private val loginRepository : LoginRepository) {
    fun getAccessibleFunctions() : UserAccessibleFunctions{
        val user = loginRepository.user!!

        return when(user.getUserType()){
            UserTypes.MAIN_MAGAZINE_WORKER -> getMainMagazineAccessibleFuncs()
            UserTypes.OTHER_MAGAZINE_WORKER -> getOtherMagazineAccessibleFuncs()
        }
    }

    private fun getMainMagazineAccessibleFuncs() : UserAccessibleFunctions{
        return UserAccessibleFunctions(AccessibleFunctionsTypes.values().toList())
    }

    private fun getOtherMagazineAccessibleFuncs() : UserAccessibleFunctions{
        return UserAccessibleFunctions(AccessibleFunctionsTypes.values().toList().filter {
            it != AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_PACKING &&
            it != AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_RECEIVE &&
            it != AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_SHIFT &&
            it != AccessibleFunctionsTypes.MENU_TILE_DOCUMENT_ORDER
        })
    }
}