package pl.tysia.maggstone.ui.wares

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.tysia.maggstone.data.*

class WareViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WareViewModel::class.java)) {
            return WareViewModel(
                wareRepository = WareRepository(
                    dataSource = WareDataSourceMock()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}