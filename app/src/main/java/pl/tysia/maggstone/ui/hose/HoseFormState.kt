package pl.tysia.maggstone.ui.hose

import pl.tysia.maggstone.data.model.Ware

class HoseFormState {
    var cordError : Int? = null
    var tip1Error : Int? = null
    var tip2Error : Int? = null
    var sleeveError : Int? = null

    var cordValid : Boolean = false
    var tip1Valid : Boolean = false
    var tip2Valid : Boolean = false
    var sleeveValid : Boolean = false
    var lengthValid : Boolean = false
    var codeValid : Boolean = true


    fun isFormValid():Boolean{
        return  cordError == null &&
                cordValid &&
                tip1Error == null &&
                tip1Valid &&
                tip2Error == null &&
                tip2Valid &&
                sleeveError == null &&
                sleeveValid &&
                lengthValid && codeValid
    }

}