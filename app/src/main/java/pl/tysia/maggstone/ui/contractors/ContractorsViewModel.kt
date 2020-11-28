package pl.tysia.maggstone.ui.contractors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.source.ContractorsRepository
import pl.tysia.maggstone.data.Result
import pl.tysia.maggstone.data.model.Contractor

class ContractorsViewModel(private val contractorsRepository: ContractorsRepository) : ViewModel() {
    private val _contractorsResult = MutableLiveData<Int>()
    val contractorsResult: LiveData<Int> = _contractorsResult

    private val _contractors = MutableLiveData<ArrayList<Contractor>>()
    val contractors: LiveData<ArrayList<Contractor>> = _contractors

    fun getContractors() {
        val result = contractorsRepository.getContractors()

        if (result is Result.Success) {
            _contractors.value = result.data
        } else {
            _contractorsResult.value = R.string.err_contractors_download
        }
    }


}