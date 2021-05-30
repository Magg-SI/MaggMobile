package pl.tysia.maggstone.ui.hose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_hose.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.HosePartType.PART_CORD
import pl.tysia.maggstone.constants.HosePartType.PART_SLEEVE
import pl.tysia.maggstone.constants.HosePartType.PART_TIP1
import pl.tysia.maggstone.constants.HosePartType.PART_TIP2
import pl.tysia.maggstone.data.Database
import pl.tysia.maggstone.data.dao.WaresDAO
import pl.tysia.maggstone.data.model.Hose
import pl.tysia.maggstone.data.model.Ware
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.login.afterTextChanged
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import pl.tysia.maggstone.ui.simple_list.SimpleListDialogFragment
import javax.inject.Inject


class HoseActivity : BaseActivity(), SimpleListDialogFragment.SimpleListOwner<Ware> {
    private lateinit var dao : WaresDAO

    @Inject lateinit var viewModel: HoseViewModel
    @Inject lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hose)

        (application as MaggApp).appComponent.inject(this)

        dao = db.waresDao()

        setParts()

        length_et.afterTextChanged { onFormEdited() }
        hose_code_et.afterTextChanged { onFormEdited() }
        twist_angle_et.afterTextChanged { onFormEdited() }
        creator_et.afterTextChanged { onFormEdited() }

        viewModel.hoseForm.observe(this@HoseActivity, Observer {
            save_button.isEnabled = it.isFormValid()

            if (it.cordError == null && it.cordValid){
                tip1_part.visibility = View.VISIBLE
                tip2_part.visibility = View.VISIBLE
                sleeve_part.visibility = View.VISIBLE
            }else{
                tip1_part.visibility = View.GONE
                tip2_part.visibility = View.GONE
                sleeve_part.visibility = View.GONE
            }

        })

        viewModel.hoseResult.observe(this@HoseActivity, Observer {
            val returnIntent = Intent()
            returnIntent.putExtra(Hose.HOSE_EXTRA, it)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()

        })

        viewModel.result.observe(this@HoseActivity, Observer {
            okDialog("Błąd", it, this@HoseActivity)
            showBlockingLoading(false)

        })
    }

    private fun setParts(){
        cord_part.viewModel = viewModel
        tip1_part.viewModel = viewModel
        tip2_part.viewModel = viewModel
        sleeve_part.viewModel = viewModel

        tip1_part.visibility = View.GONE
        tip2_part.visibility = View.GONE
        sleeve_part.visibility = View.GONE
    }

    fun addHose(view: View){
        if (formValid()){
            showBlockingLoading(true)

            viewModel.hose.ktrID = getIntent().getIntExtra("ktrID", 0);
            viewModel.addHose(viewModel.hose)
        }else{
            save_button.isEnabled = false
        }

    }

    private fun formValid() : Boolean{
        return viewModel.hoseForm.value!!.isFormValid()
                && !cord_part.wasEdited
                && !tip1_part.wasEdited
                && !tip2_part.wasEdited
                && !sleeve_part.wasEdited
    }

    private fun onFormEdited(){
        viewModel.formChanged(
            length_et.text.toString(),
            hose_code_et.text.toString(),
            twist_angle_et.text.toString(),
            creator_et.text.toString())
    }

    override fun onItemSelected(item: ICatalogable, tag: String) {
        item as Ware
        when (tag){
            PART_CORD -> {
                viewModel.onCordChanged(item)
            }
            PART_SLEEVE -> {
                viewModel.onSleeveChanged(item)
            }
            PART_TIP1 -> {
                viewModel.onTip1Changed(item)
            }
            PART_TIP2 -> {
                viewModel.onTip2Changed(item)
            }
        }
    }

    override fun getLiveData(tag: String): LiveData<List<Ware>>? {
        val hose = viewModel.hose
        return when (tag){
            PART_CORD -> dao.getAllCords()
            PART_SLEEVE -> dao.getSleevesFor(hose.cord!!.hoseFi!!, hose.cord!!.hoseIdx!!)
            PART_TIP1, PART_TIP2 -> dao.getTipsFor(hose.cord!!.hoseFi!!)
            else -> null
        }
    }
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