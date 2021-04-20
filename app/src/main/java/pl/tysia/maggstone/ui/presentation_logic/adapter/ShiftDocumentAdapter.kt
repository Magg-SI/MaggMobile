package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.graphics.Color
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

        val context = holder.back.context

        holder.title.text = item.getTitle()

        val availability = item.getMainAvailability()

        if (availability == null){
            holder.description.text = ""
        }else{
            holder.description.text = context.getString(R.string.availability, countToStr(availability.quantity))
        }

        holder.name.text = item.getShortDescription()
        holder.numberET.setText(countToStr(item.ilosc))

        if (!item.iloscOk){
            holder.numberET.error = "Liczba musi być większa niż 0"
        }else{
            holder.numberET.error = null
        }
    }

    override fun isCountValid(item: T): Boolean {
        return super.isCountValid(item)
                && item.ilosc <= item.getMainAvailability()!!.quantity
    }

    override fun fixCount(item: T){
        if (!isCountValid(item)) {
            val availability = item.getMainAvailability()!!.quantity

            if (item.ilosc > availability) {
                item.ilosc = availability
            }
        }

        super.fixCount(item)

    }
}