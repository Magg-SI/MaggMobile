package pl.tysia.maggstone.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.tysia.maggstone.data.service.ContractorsDownloadService
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.ui.wares.WareInfoActivity
import pl.tysia.maggstone.data.service.WaresDownloadService
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.ui.document.BasicNewDocumentActivity
import pl.tysia.maggstone.ui.login.LoginActivity
import pl.tysia.maggstone.ui.orders.OrdersActivity
import pl.tysia.maggstone.ui.scanner.WareScannerActivity

class MainActivity : AppCompatActivity() {
    companion object{
        private const val WARE_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this, WaresDownloadService::class.java))
        startService(Intent(this, ContractorsDownloadService::class.java))
    }

    fun onNewDocumentClicked(view : View){
        startActivity(Intent(this, BasicNewDocumentActivity::class.java))
    }

    fun onSynchroniseClicked(view : View){
        startActivity(Intent(this, DownloadStateActivity::class.java))
    }

    fun onOrdersClicked(view : View){
        startActivity(Intent(this, OrdersActivity::class.java))
    }

    fun onScanWareClicked(view : View){
        startActivityForResult(Intent(this, WareScannerActivity::class.java),
            WARE_REQUEST_CODE
        )
    }

    fun onSendingClicked(view: View){
        startActivity(Intent(this, SendingStateActivity::class.java))
    }

    fun onLogoutClicked(view: View){
        //TODO: change to viewmodel usage?
        LoginRepository(LoginDataSource(), this).logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == WARE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val ware = data!!.getSerializableExtra(Ware.WARE_EXTRA) as Ware

            val intent = Intent(this, WareInfoActivity::class.java)
            intent.putExtra(Ware.WARE_EXTRA, ware)
            startActivity(intent)
        }
    }
}