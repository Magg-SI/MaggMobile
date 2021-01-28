package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.OrderedWare

class OrderedItemsAdapter(items : ArrayList<OrderedWare>) : CatalogAdapter<OrderedWare, OrderedItemsAdapter.BasicViewHolder>(items) {

    inner class BasicViewHolder(v: View) : RecyclerView.ViewHolder(v),
        View.OnClickListener {
        var title: TextView = v.findViewById(R.id.title_tv)
        var description: TextView = v.findViewById(R.id.description_tv)
        var back: View = v.findViewById(R.id.back)
        var deleteButton: ImageButton = v.findViewById(R.id.delete_button)

        override fun onClick(v: View) {
            val pos = adapterPosition
            onItemClick(v, pos)
        }

        init {
            v.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BasicViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_simple, viewGroup, false)
        v.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        return BasicViewHolder(v)
    }

    override fun onBindViewHolder(catalogItemViewHolder: BasicViewHolder, i: Int) {
        val item = shownItems[i]
        val context: Context = catalogItemViewHolder.back.getContext()

        catalogItemViewHolder.title.text = item.title
        catalogItemViewHolder.description.text = item.description

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (item.packed) {
                catalogItemViewHolder.title.setTextColor(context.resources.getColor(R.color.hintGray, context.theme))
            } else {
                catalogItemViewHolder.title.setTextColor(context.resources.getColor(R.color.lightGray, context.theme))
            }
        }else{
            if (item.packed) {
                catalogItemViewHolder.title.setTextColor(context.resources.getColor(R.color.hintGray))
            } else {
                catalogItemViewHolder.title.setTextColor(context.resources.getColor(R.color.lightGray))
            }
        }

        if (item.packed) {
            catalogItemViewHolder.back.setBackgroundResource(R.drawable.list_item_background_selected)
        } else {
            catalogItemViewHolder.back.setBackgroundResource(R.drawable.list_item_background)
        }
    }

}