package pl.tysia.maggstone.data.model

import com.google.gson.annotations.SerializedName
import pl.tysia.maggstone.constants.PackingState
import java.lang.StringBuilder

class OrderedWare(name: String) : Ware(name) {
    @SerializedName("ilZamow")
    var orderedNumber : Double = 0.0
    @SerializedName("ilZapak")
    var packedNumber : Double = 0.0
    @SerializedName("ilNext")
    var postponedNumber : Double = 0.0
    @SerializedName("ilAnul")
    var cancelledNumber : Double = 0.0
    @SerializedName("stanMag")
    var availability : Double? = null

    fun copyNumbers(ware: OrderedWare){
        this.cancelledNumber = ware.cancelledNumber
        this.packedNumber = ware.packedNumber
        this.postponedNumber = ware.postponedNumber
    }

    fun getPackingState() : PackingState{
        val packed = packedNumber + postponedNumber + cancelledNumber

        return when {
            packed >= orderedNumber -> PackingState.PACKED
            packed == 0.0 -> PackingState.UNTOUCHED
            else -> PackingState.STARTED
        }
    }

    override fun getDescription(): String {
        val sb = StringBuilder()


        sb.append("Zamówione: "+countToStr(orderedNumber)+"\n")

        if (packedNumber != 0.0){
            sb.append("\nSpakowane: "+countToStr(packedNumber))
        }

        if (postponedNumber != 0.0){
            sb.append("\nNastępne zamówienie: "+countToStr(postponedNumber))
        }

        if (cancelledNumber != 0.0){
            sb.append("\nAnulowane: "+countToStr(cancelledNumber))
        }

        return sb.toString()
    }

    fun countToStr(ilo: Double): String {
        val x = 1.0*ilo.toInt()
        if(x==ilo) return ilo.toInt().toString()
        return ilo.round(4).toString().replace('.', ',')
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }
}