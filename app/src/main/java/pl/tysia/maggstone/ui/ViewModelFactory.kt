package pl.tysia.maggstone.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.tysia.maggstone.data.source.*
import pl.tysia.maggstone.ui.contractors.ContractorsViewModel
import pl.tysia.maggstone.ui.login.LoginViewModel
import pl.tysia.maggstone.ui.orders.OrdersViewModel
import pl.tysia.maggstone.ui.orders.OrderedWaresViewModel
import pl.tysia.maggstone.ui.picture.PictureViewModel
import pl.tysia.maggstone.ui.ware_ordering.WareOrderingViewModel
import pl.tysia.maggstone.ui.wares.WareInfoViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(
                    loginRepository = LoginRepository(
                        dataSource = LoginDataSource(),
                        context = context
                    )
                ) as T
            }
            modelClass.isAssignableFrom(ContractorsViewModel::class.java) -> {
                return ContractorsViewModel(
                    contractorsRepository = ContractorsRepository(
                        dataSource = ContractorsDataSourceMock()
                    )
                ) as T
            }
            modelClass.isAssignableFrom(OrdersViewModel::class.java) -> {
                return OrdersViewModel(
                    repository = OrdersRepository(
                        dataSource = OrdersDataSource()
                    )
                ) as T
            }
            modelClass.isAssignableFrom(OrderedWaresViewModel::class.java) -> {
                return OrderedWaresViewModel(
                    repository = OrdersRepository(
                        dataSource = OrdersDataSource()
                    )
                ) as T
            }
            modelClass.isAssignableFrom(PictureViewModel::class.java) -> {
                return PictureViewModel(
                    PictureDataSource(context)
                ) as T
            }
            modelClass.isAssignableFrom(WareInfoViewModel::class.java) -> {
                return WareInfoViewModel(
                    wareRepository = WareRepository(
                        dataSource = WareDataSource()
                    )
                ) as T
            }
            modelClass.isAssignableFrom(WareOrderingViewModel::class.java) -> {
                return WareOrderingViewModel(
                    repository = OrdersRepository(
                        dataSource = OrdersDataSource()
                    )
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}