package pl.tysia.maggstone.ui.hose

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_hose.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.NetAddressManager
import pl.tysia.maggstone.data.dao.WaresDAO
import pl.tysia.maggstone.data.model.Hose
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.data.source.LoginDataSource
import pl.tysia.maggstone.data.source.LoginRepository
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.ViewModelFactory
import pl.tysia.maggstone.ui.login.afterTextChanged
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.simple_list.SimpleListDialogFragment


private const val FRAGMENT_TAG_CORD = "pl.tysia.maggstone.cord_fragment_tag"
private const val FRAGMENT_TAG_TIP1 = "pl.tysia.maggstone.tip_fragment_tag1"
private const val FRAGMENT_TAG_TIP2 = "pl.tysia.maggstone.tip_fragment_tag2"
private const val FRAGMENT_TAG_SLEEVE = "pl.tysia.maggstone.sleeve_fragment_tag"
class HoseActivity : AppCompatActivity(), SimpleListDialogFragment.SimpleListOwner<Ware> {
    private lateinit var dao : WaresDAO

    private lateinit var viewModel: HoseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hose)

        val db = Room.databaseBuilder(
            this,
            Database::class.java, "pl.tysia.database"
        ).build()

        dao = db.waresDao()

        viewModel = ViewModelProvider(this,
            ViewModelFactory(this)
        ).get(HoseViewModel::class.java)

        pipe_et.onFocusChange { focused ->
            run {
                sleeve_et.setText("")
                end2_et.setText("")
                end1_et.setText("")

                viewModel.hose.tip1 = null
                viewModel.hose.tip2 = null
                viewModel.hose.sleeve = null
                if (!focused) viewModel.onCordChanged(pipe_et.text.toString())
            }
        }
        end1_et.onFocusChange { focused -> if (!focused) viewModel.onTip1Changed(end1_et.text.toString()) }
        end2_et.onFocusChange { focused -> if (!focused) viewModel.onTip2Changed(end2_et.text.toString()) }
        sleeve_et.onFocusChange { focused -> if (!focused) viewModel.onSleeveChanged(sleeve_et.text.toString()) }

        length_et.afterTextChanged { onFormEdited() }
        number_et.afterTextChanged { onFormEdited() }
        hose_code_et.afterTextChanged { onFormEdited() }
        twist_angle_et.afterTextChanged { onFormEdited() }
        creator_et.afterTextChanged { onFormEdited() }

        viewModel.hoseForm.observe(this@HoseActivity, Observer {
            save_button.isEnabled = it.isFormValid()

            if (it.cordError == null){
                tips_layout.visibility = View.VISIBLE
                sleeve_layout.visibility = View.VISIBLE
            }else{
                tips_layout.visibility = View.GONE
                sleeve_layout.visibility = View.GONE
            }

            if (!it.isFormValid()){
                pipe_et.error = if (it.cordError != null) getString(it.cordError!!) else null
                end1_et.error = if (it.tip1Error != null) getString(it.tip1Error!!) else null
                end2_et.error = if (it.tip2Error != null) getString(it.tip2Error!!) else null
                sleeve_et.error = if (it.sleeveError != null) getString(it.sleeveError!!) else null
            }

        })

        viewModel.hoseResult.observe(this@HoseActivity, Observer {
            val returnIntent = Intent()
            returnIntent.putExtra(Hose.HOSE_EXTRA, it)
            setResult(Activity.RESULT_OK, returnIntent)
//            showProgress(false)
            finish()

        })

        viewModel.result.observe(this@HoseActivity, Observer {
            okDialog("Błąd", it, this@HoseActivity)
            showProgress(false)

        })
    }

    private fun showProgress(show : Boolean){
        if (show){
            hose_form.visibility = View.INVISIBLE
            save_button.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }else{
            hose_form.visibility = View.VISIBLE
            save_button.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

    fun addHose(view: View){
        showProgress(true)

        val token = LoginRepository(
            LoginDataSource(NetAddressManager(this)),
            this
        ).user!!.token

        viewModel.addHose(token, viewModel.hose)
    }

    private fun onFormEdited(){
        viewModel.formChanged(
            length_et.text.toString(),
            number_et.text.toString(),
            hose_code_et.text.toString(),
            twist_angle_et.text.toString(),
            creator_et.text.toString())
    }

    fun onSearchClicked(view: View){
        val tag = when (view.id){
            R.id.tip_search_button -> FRAGMENT_TAG_TIP1
            R.id.tip_search_button2 -> FRAGMENT_TAG_TIP2
            R.id.cord_search_button -> FRAGMENT_TAG_CORD
            R.id.sleeve_search_button -> FRAGMENT_TAG_SLEEVE
            else -> null
        }

        SimpleListDialogFragment.newInstance("Wybór węża").show(supportFragmentManager, tag)
    }



    override fun onItemSelected(item: ICatalogable, tag: String) {
        item as Ware
        when (tag){
            FRAGMENT_TAG_CORD -> {
                sleeve_et.setText("")
                end2_et.setText("")
                end1_et.setText("")

                viewModel.hose.tip1 = null
                viewModel.hose.tip2 = null
                viewModel.hose.sleeve = null
                viewModel.onCordChanged(item)
                pipe_et.setText(item.index)
            }
            FRAGMENT_TAG_SLEEVE -> {
                viewModel.onSleeveChanged(item)
                sleeve_et.setText(item.index)
            }
            FRAGMENT_TAG_TIP1 -> {
                viewModel.onTip1Changed(item)
                end1_et.setText(item.index)
            }
            FRAGMENT_TAG_TIP2 -> {
                viewModel.onTip2Changed(item)
                end2_et.setText(item.index)
            }
        }
    }

    override fun getLiveData(tag: String): LiveData<List<Ware>>? {
        val hose = viewModel.hose
        return when (tag){
            FRAGMENT_TAG_CORD -> dao.getAllCords()
            FRAGMENT_TAG_SLEEVE -> dao.getSleevesFor(hose.cord!!.hoseFi!!, hose.cord!!.hoseIdx!!)
            FRAGMENT_TAG_TIP1, FRAGMENT_TAG_TIP2 -> dao.getTipsFor(hose.cord!!.hoseFi!!)
            else -> null
        }
    }
}

fun EditText.onFocusChange(onFocusChange: (Boolean) -> Unit) {
    this.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus -> onFocusChange.invoke(hasFocus) }
}

fun EditText.afterTextChanged(afterTextChanged: () -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}