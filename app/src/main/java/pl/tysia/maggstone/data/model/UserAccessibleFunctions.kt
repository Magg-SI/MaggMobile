package pl.tysia.maggstone.data.model

import pl.tysia.maggstone.constants.AccessibleFunctionsTypes

class UserAccessibleFunctions(var accessibleFuncs : List<AccessibleFunctionsTypes>){

    fun hasAccess(function: AccessibleFunctionsTypes) : Boolean{
        return accessibleFuncs.contains(function)
    }

    fun getAccessibleMenuTiles() : List<AccessibleFunctionsTypes>{
        return accessibleFuncs.filter {
            it.name.startsWith("MENU_TILE")
        }
    }

}