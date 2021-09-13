package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.DocumentItem
import java.lang.Integer.MAX_VALUE
import java.util.*
import kotlin.math.round


open class DocumentAdapter<T : DocumentItem>(items: ArrayList<T>) :
    CatalogAdapter<T, DocumentAdapter<T>.DocumentViewHolder>(items) {

    override var selectedItem: T? = null

    open inner class DocumentViewHolder(v: View) :
        RecyclerView.ViewHolder(v), View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

        var title: TextView = v.findViewById(R.id.title_tv)
        var description: TextView = v.findViewById(R.id.subtitle_tv)
        var name: TextView = v.findViewById(R.id.name_tv)
        var back: ConstraintLayout = v.findViewById(R.id.back)
        var deleteButton: ImageButton = v.findViewById(R.id.delete_button)
        var infoButton: ImageButton = v.findViewById(R.id.info_button)
        var moreButton: ImageView = v.findViewById(R.id.more_button)
        var lessButton: ImageView = v.findViewById(R.id.less_button)
        var numberET: EditText = v.findViewById(R.id.number_et)
        var checkImage: ImageView = v.findViewById(R.id.check_image)

        override fun onClick(v: View) {
            val pos = adapterPosition
            onItemClick(v, pos)
        }

        init {
            numberET.onFocusChangeListener = this
            numberET.addTextChangedListener(this)

            moreButton.setOnClickListener {
                val item: DocumentItem = shownItems[adapterPosition]
                numberET.clearFocus();
                onMoreClicked(item)

                changeListeners.forEach { it.onListChanged() }
            }

            lessButton.setOnClickListener {
                val item = shownItems[adapterPosition] as DocumentItem
                numberET.clearFocus();
                onLessClicked(item)

                changeListeners.forEach { it.onListChanged() }
            }

            deleteButton.setOnClickListener {
               onDelete()
            }

            v.setOnClickListener(this)
        }

        open fun onDelete(){
            allItems.remove(allItems[adapterPosition])
            filter()
            notifyDataSetChanged()

            changeListeners.forEach { it.onListChanged() }
        }

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (!hasFocus) {
                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        numberET.clearFocus()
                        notifyDataSetChanged()
                    }
                },50)
            }
        }

        override fun afterTextChanged(s: Editable?) {
            val item = allItems[adapterPosition]
            if (selectedItem != item)
                selectedItem = item

            if(numberET.text.toString().isEmpty()) {
                item.ilosc=0.0
                fixCount(item,1)
                showError(item, numberET)
            }

            else if (numberET.hasFocus()) {
                try {
                    val quantity = numberET.text.toString().replace(',', '.').toDouble()

                    if (quantity >= 0) item.ilosc = quantity
                    fixCount(item,0)
                } catch (ex: NumberFormatException) {
                    fixCount(item,-1)
                    item.ilosc=0.0
                } finally {
                    //if (numberET.text.toString().replace(',','.').toDouble() != item.ilosc) numberET.setText(countToStr(item.ilosc))


                    showError(item, numberET)
                }

            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    protected open fun isCountValid(item: T) {
        if(item.ilosc <= 0) item.iloscOk=1;
    }

    /*private fun setCountValidity(item: T){
        if(item.ilosc > 0.0) {
            item.iloscOk = 0
        }

        changeListeners.forEach { it.onListChanged() }
    }*/

    fun fixCount(item: T, err: Int){
        /*if (!isCountValid(item)){
            if (item.ilosc < 0) item.ilosc = 0.0
        }*/
        item.iloscOk=err
        if(item.iloscOk==0) isCountValid(item)

        //setCountValidity(item)
        changeListeners.forEach { it.onListChanged() }
    }

    private fun showError(item: DocumentItem, view: EditText){
        if (item.iloscOk!=0) {
            view.error = getErrorTx(item.iloscOk)
        }
        else{
            view.error = null
        }
    }

    fun getErrorTx(err: Int): String {
        return when (err) {
            -1 -> {
                "Nieprawidłowy format danych"
            }
            1 -> {
                "Ilość musi być większa niż 0"
            }
            2 -> {
                "Zbyt duża ilość"
            }
            else -> ""
        }
    }

    protected open fun onMoreClicked(item: DocumentItem){
        if (item.ilosc < MAX_VALUE) {
            item.ilosc = item.ilosc + 1
            item.iloscOk = 0
        }
        notifyDataSetChanged()
    }

    protected open fun onLessClicked(item: DocumentItem){
        if (item.ilosc > 1) {
            item.ilosc = item.ilosc - 1
            item.iloscOk=0
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DocumentViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_document, viewGroup, false)
        v.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        return DocumentViewHolder(v)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val item: T = shownItems[position]

        holder.title.text = item.getTitle()
        holder.description.text = item.getDescription()
        holder.name.text = item.getSubtitle()
        holder.numberET.setText(countToStr(item.ilosc))

        showError(item, holder.numberET)
    }

    fun countToStr(ilo: Double): String {
        val x = 1.0*ilo.toInt()
        if(x==ilo) return ilo.toInt().toString()
        return ilo.round(4).toString().replace('.', ',')
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}