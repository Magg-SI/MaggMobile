package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.DocumentItem
import java.util.*

class StocktakingDocumentAdapter<T : DocumentItem>(items: ArrayList<T>) :
    DocumentAdapter<T>(items) {

    var listener : OnEditConfirmedListener? = null

    var editedItem : EditedItem? = null

    class EditedItem (val item: DocumentItem, var new : Boolean = false, var quantity : Double = item.ilosc){
        var alreadyAdded = 0.0
    }

    inner class StocktakingViewHolder(v : View) : DocumentAdapter<T>.DocumentViewHolder(v){
        val editingLayout: LinearLayout = v.findViewById(R.id.save_changes_ll)
        val confirmButton: ImageView = v.findViewById(R.id.confirm_button)
        val cancelButton: ImageView = v.findViewById(R.id.cancel_button)

        override fun afterTextChanged(s: Editable?) {
            val item = shownItems[adapterPosition]

            if (!numberET.hasFocus()) return

            if (editedItem == null){
                editedItem = EditedItem(item)
            }

            if (s.isNullOrBlank()){
                numberET.setText("0.0")
                editedItem!!.quantity = 0.0
            }
            else{
                editedItem!!.quantity = s.toString().toDouble()
            }

            editingLayout.visibility = View.VISIBLE
            confirmButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            numberET.isEnabled = true
            moreButton.isClickable = true
            lessButton.isClickable = true

            deleteButton.isClickable = true
        }

        override fun onFocusChange(v: View?, hasFocus: Boolean) {

        }

        override fun onClick(v: View) {

        }

        init{
            cancelButton.setOnClickListener {
                if (editedItem!!.item.ilosc < 0){
                    allItems.remove(editedItem!!.item)
                }

                editedItem = null
                notifyDataSetChanged()
            }

            confirmButton.setOnClickListener {
                if (listener != null){
                    listener!!.editConfirmed(editedItem!!)
                }
            }
        }

        override fun onDelete() {
            if (editedItem == null){
                editedItem = EditedItem(shownItems[adapterPosition])
            }

            editedItem!!.quantity = 0.0

            notifyDataSetChanged()
        }
    }

    fun acceptChanges(){
        val updatedItem = allItems.firstOrNull {
            it.towID == editedItem!!.item.towID
                    && it.ilosc != editedItem!!.item.ilosc
        }

        if (updatedItem != null){
            editedItem!!.item.ilosc = editedItem!!.quantity + editedItem!!.alreadyAdded

            allItems.remove(updatedItem)

        }else{
            editedItem!!.item.ilosc = editedItem!!.quantity + editedItem!!.alreadyAdded
        }

        editedItem!!.new = false

        editedItem = null

        filter()
        notifyDataSetChanged()
    }

    override fun addItem(item : T){
        editedItem = EditedItem(item, true)
        allItems.add(item)

        filter()
        notifyDataSetChanged()
    }

    override fun onMoreClicked(item: DocumentItem) {
        if (editedItem == null){
            editedItem = EditedItem(item)
        }

        editedItem!!.quantity++

        notifyDataSetChanged()
    }

    override fun onLessClicked(item: DocumentItem) {
        if (editedItem == null){
            editedItem = EditedItem(item)
        }

        editedItem!!.quantity--

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {

        holder as StocktakingViewHolder

        val item = shownItems[position]

        holder.title.text = item.getTitle()
        holder.description.text = item.getDescription()
        holder.name.text = item.getSubtitle()

        if (editedItem != null && item == editedItem!!.item){
            holder.numberET.setText(editedItem!!.quantity.toString())

            holder.editingLayout.visibility = View.VISIBLE
            holder.confirmButton.visibility = View.VISIBLE
            holder.cancelButton.visibility = View.VISIBLE
            holder.numberET.isEnabled = true
            holder.moreButton.isClickable = true
            holder.lessButton.isClickable = true

            holder.deleteButton.isClickable = true
        }else if (editedItem == null){
            holder.editingLayout.visibility = View.GONE
            holder.confirmButton.visibility = View.GONE
            holder.cancelButton.visibility = View.GONE

            holder.numberET.isEnabled = true
            holder.moreButton.isClickable = true
            holder.lessButton.isClickable = true

            holder.deleteButton.isClickable = true

            holder.numberET.setText(item.ilosc.toString())

        }else{
            holder.editingLayout.visibility = View.GONE
            holder.confirmButton.visibility = View.GONE
            holder.cancelButton.visibility = View.GONE

            holder.numberET.isEnabled = false
            holder.moreButton.isClickable = false
            holder.lessButton.isClickable = false

            holder.deleteButton.isClickable = false
            holder.numberET.setText(item.ilosc.toString())
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DocumentViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_document_stocktaking, viewGroup, false)
        v.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        return StocktakingViewHolder(v)
    }

    interface OnEditConfirmedListener{
        fun editConfirmed(item : EditedItem)
    }
}