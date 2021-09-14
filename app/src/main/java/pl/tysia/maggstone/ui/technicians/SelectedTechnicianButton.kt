package pl.tysia.maggstone.ui.technicians

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.selected_person_button.view.*
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.Technician

class SelectedTechnicianButton(val technician : Technician, val activity : AppCompatActivity) : LinearLayout(activity) {

    private var listener : OnButtonRemoveListener? = null

    init{
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        inflater.inflate(
            R.layout.selected_person_button,
            this
        )

        if (activity is OnButtonRemoveListener){
            listener = activity
        }

        name_tv.text = technician.name
        remove_button.setOnClickListener { listener?.onRemove(this) }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val params = layoutParams as ConstraintLayout.LayoutParams

        layoutParams = params;
    }

    interface OnButtonRemoveListener{
        fun onRemove(button : SelectedTechnicianButton)
    }


}