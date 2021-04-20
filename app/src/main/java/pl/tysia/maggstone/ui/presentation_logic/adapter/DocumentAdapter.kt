package pl.tysia.maggstone.ui.presentation_logic.adapter

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
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.DocumentItem
import java.lang.Integer.MAX_VALUE
import java.util.*
import kotlin.math.round

open class DocumentAdapter<T : DocumentItem>(items: ArrayList<T>) :
    CatalogAdapter<T, DocumentAdapter<T>.DocumentViewHolder>(items) {

    inner class DocumentViewHolder(v: View) :
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

        private var lastEdited : DocumentItem? = null

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
            }

            lessButton.setOnClickListener {
                val item = shownItems[adapterPosition] as DocumentItem
                numberET.clearFocus();
                onLessClicked(item)
            }

            deleteButton.setOnClickListener {
                allItems.remove(allItems[adapterPosition])
                filter()
                notifyDataSetChanged()
            }

            v.setOnClickListener(this)
        }

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            /*if (!hasFocus && lastEdited != null){
                val item = lastEdited!!
                try {
                    val quantity = numberET.text.toString().toDouble()
                    if (quantity >= 0) item.ilosc = quantity else numberET.setText("0")
                } catch (ex: NumberFormatException) {
                    numberET.setText("0")
                }
                numberET.clearFocus();
            }*/

            if (!hasFocus) {
                title.setText("0")
                numberET.clearFocus();
            }
        }

        override fun afterTextChanged(s: Editable?) {
            //if(adapterPosition>=0) lastEdited = allItems[adapterPosition]

            val item = allItems[adapterPosition]
            if(numberET.text.toString().isEmpty()) {
                item.ilosc=0.0
                item.iloscOk=false
            }
            else if (numberET.hasFocus() ) {
                try {
                    val quantity = numberET.text.toString().replace(',','.').toDouble()
                    if (quantity >= 0) item.ilosc = quantity else numberET.setText("0")
                    item.iloscOk=true
                    setIloscState(item)
                } catch (ex: NumberFormatException) {
                    item.ilosc=0.0
                    item.iloscOk=false
                    //numberET.setText("0")
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    protected open fun onMoreClicked(item : DocumentItem){
        if (item.ilosc < MAX_VALUE) {
            item.ilosc = item.ilosc + 1
            item.iloscOk=true
        }
        notifyDataSetChanged()
    }

    protected open fun onLessClicked(item : DocumentItem){
        if (item.ilosc > 1) {
            item.ilosc = item.ilosc - 1
            item.iloscOk=true
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
        val item: DocumentItem = shownItems[position]
        val context = holder.back.context
        holder.title.text = item.getTitle()
        holder.description.text = item.getDescription()
        holder.name.text = item.getShortDescription()
        //holder.numberET.setText(java.lang.Double.toString(item.ilosc))
        holder.numberET.setText(iloscToStr(item.ilosc))
    }

    protected open fun setIloscState(i: DocumentItem) {
    }

    fun iloscToStr(ilo: Double): String {
        val x = 1.0*ilo.toInt()
        if(x==ilo) return ilo.toInt().toString()
        return ilo.round(4).toString().replace('.',',')
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}