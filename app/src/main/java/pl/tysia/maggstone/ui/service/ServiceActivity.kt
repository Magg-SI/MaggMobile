package pl.tysia.maggstone.ui.service

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_service.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.app.MaggApp
import pl.tysia.maggstone.constants.Extras.TECHNICIANS
import pl.tysia.maggstone.data.model.Service
import pl.tysia.maggstone.data.model.Technician
import pl.tysia.maggstone.okDialog
import pl.tysia.maggstone.ui.BaseActivity
import pl.tysia.maggstone.ui.login.afterTextChanged
import pl.tysia.maggstone.ui.technicians.SelectedTechnicianButton
import pl.tysia.maggstone.ui.technicians.TechniciansActivity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


const val TECHNICIANS_REQUEST = 7777

class ServiceActivity : BaseActivity(), SelectedTechnicianButton.OnButtonRemoveListener {
    @Inject lateinit var viewModel: ServiceViewModel

    private val currentTime: Calendar = Calendar.getInstance()
    private var hour: Int = currentTime.get(Calendar.HOUR_OF_DAY)
    private var minute: Int = currentTime.get(Calendar.MINUTE)
    private var day: Int = currentTime.get(Calendar.DAY_OF_MONTH)
    private var month: Int = currentTime.get(Calendar.MONTH)
    private var year: Int = currentTime.get(Calendar.YEAR)

    private var technicians = ArrayList<Technician>()

    private var onTimeClickedListener = View.OnClickListener { v ->
        val textView = v as TextView
        hour = currentTime.get(Calendar.HOUR_OF_DAY)
        minute = currentTime.get(Calendar.MINUTE)

        val mTimePicker  = TimePickerDialog(this@ServiceActivity,
            OnTimeSetListener { _, selectedHour, selectedMinute ->
                currentTime.set(Calendar.HOUR_OF_DAY, selectedHour)
                currentTime.set(Calendar.MINUTE, selectedMinute)

                textView.text = SimpleDateFormat("HH:mm").format(currentTime.time)
                checkAndDisplaySave()

            }, hour, minute, true
        )

        mTimePicker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Anuluj", mTimePicker)
        mTimePicker.setButton(TimePickerDialog.BUTTON_POSITIVE, "Ok", mTimePicker)
        mTimePicker.show()
    }

    private var onDateClickedListener = View.OnClickListener { v ->
        val textView = v as TextView
        day = currentTime.get(Calendar.DAY_OF_MONTH)
        month = currentTime.get(Calendar.MONTH)
        year = currentTime.get(Calendar.YEAR)

        val mTimePicker = DatePickerDialog(this@ServiceActivity,
            DatePickerDialog.OnDateSetListener{ _, selectedYear, selectedMonth, selectedDay  ->
                currentTime.set(Calendar.YEAR, selectedYear)
                currentTime.set(Calendar.MONTH, selectedMonth)
                currentTime.set(Calendar.DAY_OF_MONTH, selectedDay)

                setDate(textView)
                checkAndDisplaySave()

            }, year, month, day
        )

        mTimePicker.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Anuluj", mTimePicker)
        mTimePicker.setButton(TimePickerDialog.BUTTON_POSITIVE, "Ok", mTimePicker)
        mTimePicker.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        (application as MaggApp).appComponent.inject(this)

        start_time_tv.setOnClickListener(onTimeClickedListener)
        end_time_tv.setOnClickListener(onTimeClickedListener)

        date_tv.setOnClickListener(onDateClickedListener)

        setDate(date_tv)

        viewModel.service.observe(this@ServiceActivity, Observer {
            showBlockingLoading(false)

            val service = it as Service
            intent.putExtra(Service.SERVICE_EXTRA, service)
            setResult(Activity.RESULT_OK, intent)
            finish()
        })

        viewModel.serviceResult.observe(this@ServiceActivity, Observer {
            okDialog("Błąd", getString(it), this)
            showBlockingLoading(false)
        })

        km_et.afterTextChanged {
            checkAndDisplaySave()
        }
    }

    private fun getService() : Service{
        val serviceDate = SimpleDateFormat("yyyy.MM.dd").format(currentTime.time)

        return Service().apply {
            ktrID= getIntent().getIntExtra("ktrID", 0);
            peopleCount = this@ServiceActivity.technicians.size
            date = serviceDate
            startTime = start_time_tv.text.toString()
            endTime = end_time_tv.text.toString()
            kilometers = km_et.text.toString().toInt()
            commuting = travel_to_company_sw.isChecked.toInt()
            saturday = saturday_cb.isChecked.toInt()
            holiday = holiday_sw.isChecked.toInt()
            technicians = this@ServiceActivity.technicians
        }
    }

    fun onOkClick(view: View){
        if (isFormValid()){
            viewModel.addService(getService())

            showBlockingLoading(true)
        }
    }

    private fun checkAndDisplaySave(){
        button2.isEnabled = isFormValid()
    }

    private fun setDate(textView: TextView){
        textView.text = SimpleDateFormat("dd.MM.yyyy").format(currentTime.time)

        saturday_cb.isChecked = currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
    }

    private fun refreshSelectedTechnicians(){
        flow.referencedIds.forEach {
                id -> servisants_layout.removeView(findViewById(id))
        }

        technicians.forEach {
            val technicianButton = SelectedTechnicianButton(it, this)

            technicianButton.id = View.generateViewId()

            servisants_layout.addView(technicianButton)
            flow.addView(technicianButton)
        }
    }

    private fun isFormValid() : Boolean{
        return technicians.isNotEmpty()
                && start_time_tv.text.isNotEmpty()
                && end_time_tv.text.isNotEmpty()
                && km_et.text.isNotBlank()
    }

    fun onTechniciansChoose(view: View){
        val intent = Intent(this, TechniciansActivity::class.java)
        startActivityForResult(intent, TECHNICIANS_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TECHNICIANS_REQUEST && resultCode == Activity.RESULT_OK ){
           technicians = data!!.getSerializableExtra(TECHNICIANS) as ArrayList<Technician>
            refreshSelectedTechnicians()

            checkAndDisplaySave()
        }

    }

    override fun onRemove(button: SelectedTechnicianButton) {
        technicians.remove(button.technician)
        refreshSelectedTechnicians()
        checkAndDisplaySave()
    }

}

fun Boolean.toInt() = if (this) 1 else 0