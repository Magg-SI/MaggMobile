package pl.tysia.maggstone.data.service

import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable

@Entity
class QueueItem(val name : String, val index : String, val wareID : Int, val photoPath : String) : ICatalogable {
    @PrimaryKey
    var id : Long? = null

    @Ignore
    var picture : List<String>? = null

    @Ignore
    var percentSent : MutableLiveData<Int> = MutableLiveData(0)
    var batchesSent = 0
    set(value) {
        field = value
        countPercentSent()
    }

    var finished : Boolean = false
    var photoID : Int = -1

    init {
        countPercentSent()
    }

    constructor(name : String, index : String, wareID : Int, path : String, id : Long) : this(name, index, wareID, path){
        this.id = id
    }

    private fun countPercentSent(){
        if (picture!=null)
            percentSent.postValue(((batchesSent * 100f)/ picture!!.size).toInt())
    }

    fun allSent() : Boolean {
        return batchesSent == picture?.size
    }

    override fun getTitle(): String = index
    override fun getDescription(): String = name
}

