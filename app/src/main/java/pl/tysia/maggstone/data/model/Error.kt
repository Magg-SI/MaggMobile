package pl.tysia.maggstone.data.model

import android.graphics.Bitmap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.json.JSONObject
import pl.tysia.maggstone.data.service.QueueItem
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable


@Entity(indices = [Index(value = ["source"], unique = true)])
class Error(val type : Int, val source : String, val errorName : String, val cause : String) : ICatalogable{
    @PrimaryKey
    var errorID : Long? = null

    @Embedded
    var queueItem : QueueItem? = null

    fun addQueueItemExtra(item : QueueItem){
       queueItem = item
    }

    fun getQueueItemExtra() : QueueItem?{
        return queueItem
    }

    constructor(type : Int, source : String, name : String, cause : String, id : Long) : this(type,source,name,cause){
        this.errorID = id
    }

    companion object{
        const val TYPE_CONNECTION = 0
        const val TYPE_PICTURE = 1
        const val TYPE_DOWNLOAD = 2
        const val TYPE_UPLOAD = 3

        const val QUEUE_ITEM_EXTRA = "pl.tysia.maggstone.queue_item_extra"

    }

    override fun getDescription(): String {
        return cause
    }

    override fun getAdditionalInfo() = null

    override fun getFilteredValue(): String {
        return source + errorName + cause
    }

    override fun getTitle(): String {
        return ""
    }

    override fun getSubtitle(): String {
        return errorName
    }
}