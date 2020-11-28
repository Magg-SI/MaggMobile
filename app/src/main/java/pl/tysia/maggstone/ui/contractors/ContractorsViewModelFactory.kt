package pl.tysia.maggstone.ui.contractors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.tysia.maggstone.data.source.ContractorsDataSourceMock
import pl.tysia.maggstone.data.source.ContractorsRepository

@Deprecated("switch to general Factory")
class ContractorsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContractorsViewModel::class.java)) {
            return ContractorsViewModel(
                contractorsRepository = ContractorsRepository(
                    dataSource = ContractorsDataSourceMock()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}