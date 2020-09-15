package pl.tysia.maggstone.data.model

import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable

sealed class DocumentItem<out T : ICatalogable>(val  item: ICatalogable) : ICatalogable {

    class WareItem(item: Ware) : DocumentItem<Ware>(item){
        var quantity : Double = 1.0
    }

    class OrderedWareItem(item: Ware,
                          var orderedNumber: Double,
                          var packedNumber: Double,
                          var postponedNumber: Double,
                          var cancelledNumber: Double,
                          var availability: Double) : DocumentItem<Ware>(item){
        var packed = false

    }


    class HoseItem<out T : ICatalogable>(item: T) : DocumentItem<T>(item)


    override fun getTitle(): String {
        return item.title
    }

    override fun getShortDescription(): String {
        return item.shortDescription!!
    }

}