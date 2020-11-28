package pl.tysia.maggstone.data.service

import androidx.lifecycle.MutableLiveData
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable

data class QueueItem(var percentSent : MutableLiveData<Int> = MutableLiveData(0), var ware : Ware) : ICatalogable {
    override fun getTitle(): String = ware.index!!
    override fun getShortDescription(): String = ware.name
}
