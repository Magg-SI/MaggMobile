package pl.tysia.maggstone.ui.wares

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.tysia.maggstone.data.source.WareDataSource
import pl.tysia.maggstone.data.source.WareRepository

class WareViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WareViewModel::class.java)) {
            return WareViewModel(
                wareRepository = WareRepository(
                    dataSource = WareDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}