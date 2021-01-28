package pl.tysia.maggstone.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.source.*
import pl.tysia.maggstone.ui.document.DocumentViewModel
import pl.tysia.maggstone.ui.hose.HoseViewModel
import pl.tysia.maggstone.ui.login.LoginViewModel
import pl.tysia.maggstone.ui.orders.OrdersViewModel
import pl.tysia.maggstone.ui.orders.OrderedWaresViewModel
import pl.tysia.maggstone.ui.picture.PictureViewModel
import pl.tysia.maggstone.ui.scanner.ShelfScannerViewModel
import pl.tysia.maggstone.ui.scanner.WaresShelfScannerViewModel
import pl.tysia.maggstone.ui.ware_ordering.WareOrderingViewModel
import pl.tysia.maggstone.ui.wares.WareInfoViewModel
import pl.tysia.maggstone.ui.wares.WareViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val netAddressManager = NetAddressManager(context)
        when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(
                    loginRepository = LoginRepository(
                        dataSource = LoginDataSource(netAddressManager),
                        context = context
                    )
                ) as T
            }
            modelClass.isAssignableFrom(OrdersViewModel::class.java) -> {
                return OrdersViewModel(
                    repository = OrdersRepository(
                        dataSource = OrdersDataSource(netAddressManager)
                    )
                ) as T
            }
            modelClass.isAssignableFrom(OrderedWaresViewModel::class.java) -> {
                return OrderedWaresViewModel(
                    repository = OrdersRepository(
                        dataSource = OrdersDataSource(netAddressManager)
                    )
                ) as T
            }
            modelClass.isAssignableFrom(PictureViewModel::class.java) -> {
                return PictureViewModel(
                    PictureDataSource(context, netAddressManager)
                ) as T
            }
            modelClass.isAssignableFrom(WareInfoViewModel::class.java) -> {
                return WareInfoViewModel(
                    wareRepository = WareRepository(
                        dataSource = WareDataSource(netAddressManager)
                    )
                ) as T
            }
            modelClass.isAssignableFrom(WareOrderingViewModel::class.java) -> {
                return WareOrderingViewModel(
                    repository = OrdersRepository(
                        dataSource = OrdersDataSource(netAddressManager)
                    )
                ) as T
            }
            modelClass.isAssignableFrom(DocumentViewModel::class.java) -> {
                return DocumentViewModel(
                        dataSource = DocumentsDataSource(netAddressManager)
                ) as T
            }
            modelClass.isAssignableFrom(WareViewModel::class.java) -> {
                return WareViewModel(
                    wareRepository = WareRepository(
                        dataSource = WareDataSource(NetAddressManager(context))
                    ), dao = Room.databaseBuilder(
                        context,
                        Database::class.java, "pl.tysia.database"
                    ).build().waresDao()
                ) as T
            }
            modelClass.isAssignableFrom(HoseViewModel::class.java) -> {
                return HoseViewModel(
                    loalDataSource = Room.databaseBuilder(
                        context,
                        Database::class.java, "pl.tysia.database"
                    ).build().waresDao(),
                    remoteDataSource = WareDataSource(NetAddressManager(context))
                ) as T
            }
            modelClass.isAssignableFrom(ShelfScannerViewModel::class.java) -> {
                return ShelfScannerViewModel(
                     ShelfDataSource(NetAddressManager(context))
                ) as T
            }
            modelClass.isAssignableFrom(WaresShelfScannerViewModel::class.java) -> {
                return WaresShelfScannerViewModel(
                    ShelfDataSource(NetAddressManager(context))
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}