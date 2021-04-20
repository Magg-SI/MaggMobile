package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.DocumentItem
import java.lang.Integer.MAX_VALUE
import java.util.*

class ShiftDocumentAdapter<T : DocumentItem>(items: ArrayList<T>) :
    DocumentAdapter<T>(items) {

    override fun onMoreClicked(item: DocumentItem) {
        val availability = item.getMainAvailability()!!.quantity

        if (item.ilosc < MAX_VALUE && item.ilosc < availability) {
            item.ilosc = item.ilosc + 1
            item.iloscOk=true
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val item: DocumentItem = shownItems[position]

        holder.title.text = item.getTitle()

        val availability = item.getMainAvailability()

        if (availability == null){
            holder.description.text = ""
        }else{
            //holder.description.text = "Dostępność: ${availability.quantity}"
            holder.description.text = "Dostępność: "+iloscToStr(availability.quantity)
        }

        holder.name.text = item.getShortDescription()
        //holder.numberET.setText(item.ilosc.toString())
        holder.numberET.setText(iloscToStr(item.ilosc))

        if(item.iloscOk) holder.numberET.setTextColor(Color.LTGRAY)
        else holder.numberET.setTextColor(Color.RED)
    }

    override fun setIloscState(i: DocumentItem) {
        val availability = i.getMainAvailability()
        if (availability != null) {
            if(i.ilosc>availability.quantity )i.iloscOk = false;
        }
        if (i.ilosc == 0.0) i.iloscOk = false;
    }
}