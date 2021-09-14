package pl.tysia.maggstone.app

import dagger.Component
import pl.tysia.maggstone.data.api.service.ContractorsService
import pl.tysia.maggstone.data.service.ContractorsDownloadService
import pl.tysia.maggstone.data.service.SendingService
import pl.tysia.maggstone.data.service.WaresDownloadService
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.DownloadStateActivity
import pl.tysia.maggstone.ui.SendingStateActivity
import pl.tysia.maggstone.ui.SettingsActivity
import pl.tysia.maggstone.ui.contractors.ContractorListActivity
import pl.tysia.maggstone.ui.cooperation.CooperationHistoryActivity
import pl.tysia.maggstone.ui.document.BasicNewDocumentActivity
import pl.tysia.maggstone.ui.document.NewDocumentActivity
import pl.tysia.maggstone.ui.document.NewShiftDocumentActivity
import pl.tysia.maggstone.ui.error.ErrorsActivity
import pl.tysia.maggstone.ui.hose.HoseActivity
import pl.tysia.maggstone.ui.hose.HoseInfoActivity
import pl.tysia.maggstone.ui.login.LoginActivity
import pl.tysia.maggstone.ui.main.MainActivity
import pl.tysia.maggstone.ui.orders.OrdersActivity
import pl.tysia.maggstone.ui.picture.PictureEditorActivity
import pl.tysia.maggstone.ui.scanner.*
import pl.tysia.maggstone.ui.service.ServiceActivity
import pl.tysia.maggstone.ui.sign.SignActivity
import pl.tysia.maggstone.ui.stocktaking.StocktakingActivity
import pl.tysia.maggstone.ui.technicians.TechniciansActivity
import pl.tysia.maggstone.ui.ware_ordering.OrderedWaresActivity
import pl.tysia.maggstone.ui.ware_ordering.WareOrderingActivity
import pl.tysia.maggstone.ui.warehouses.WarehousesListActivity
import pl.tysia.maggstone.ui.wares.WareInfoActivity
import pl.tysia.maggstone.ui.wares.WareListActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface ApplicationComponent {
    fun inject(activity: LoginActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: ContractorListActivity)
    fun inject(activity: BasicNewDocumentActivity)
    fun inject(activity: NewShiftDocumentActivity)
    fun inject(activity: ErrorsActivity)
    fun inject(activity: HoseActivity)
    fun inject(activity: HoseInfoActivity)
    fun inject(activity: OrdersActivity)
    fun inject(activity: PictureEditorActivity)
    fun inject(activity: ShelfScannerActivity)
    fun inject(activity: WareScannerActivity)
    fun inject(activity: WaresShelfScannerActivity)
    fun inject(activity: ServiceActivity)
    fun inject(activity: SignActivity)
    fun inject(activity: TechniciansActivity)
    fun inject(activity: OrderedWaresActivity)
    fun inject(activity: WareOrderingActivity)
    fun inject(activity: WarehousesListActivity)
    fun inject(activity: WareInfoActivity)
    fun inject(activity: WareListActivity)
    fun inject(activity: DownloadStateActivity)
    fun inject(activity: SendingStateActivity)
    fun inject(activity: SettingsActivity)
    fun inject(activity: NewDocumentActivity)
    fun inject(activity: CooperationHistoryActivity)
    fun inject(activity: StocktakingActivity)

    fun inject(service: WaresDownloadService)
    fun inject(service: ContractorsDownloadService)
    fun inject(service: SendingService)

    fun inject(app: MaggApp)
}
