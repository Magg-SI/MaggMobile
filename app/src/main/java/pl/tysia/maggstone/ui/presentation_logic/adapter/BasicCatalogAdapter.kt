package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import java.util.*

class BasicCatalogAdapter(items: ArrayList<ICatalogable>) :
    CatalogAdapter<ICatalogable, BasicCatalogAdapter.BasicViewHolder>(
        items
    ) {

    inner class BasicViewHolder internal constructor(v: View) :
        RecyclerView.ViewHolder(v), View.OnClickListener {

        var title: TextView = v.findViewById(R.id.title_tv)
        var subtitle: TextView = v.findViewById(R.id.subtitle_tv)
        var description: TextView = v.findViewById(R.id.description_tv)
        var additionalInfo: TextView = v.findViewById(R.id.additional_info_tv)
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

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): BasicViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_simple, viewGroup, false)
        v.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        return BasicViewHolder(v)
    }

    override fun onBindViewHolder(
        catalogItemViewHolder: BasicViewHolder,
        i: Int
    ) {
        val item = shownItems[i]!!

        if (item.getAdditionalInfo() != null && item.getAdditionalInfo()!!.isNotEmpty()) {
            catalogItemViewHolder.additionalInfo.text = item.getAdditionalInfo()
            catalogItemViewHolder.additionalInfo.visibility = View.VISIBLE
        } else {
            catalogItemViewHolder.additionalInfo.visibility = View.GONE
        }


        if (item.getSubtitle()!!.isNotEmpty()) {
            catalogItemViewHolder.subtitle.text = item.getSubtitle()
            catalogItemViewHolder.subtitle.visibility = View.VISIBLE
        } else {
            catalogItemViewHolder.subtitle.visibility = View.GONE
        }

        if (item.getDescription()!!.isNotEmpty()) {
            catalogItemViewHolder.description.text = item.getDescription()
            catalogItemViewHolder.description.visibility = View.VISIBLE
        } else {
            catalogItemViewHolder.description.visibility = View.GONE
        }

        catalogItemViewHolder.title.text = item.getTitle()

        if (item === selectedItem) {
            catalogItemViewHolder.back.setBackgroundResource(R.drawable.list_item_background_selected)
        } else {
            catalogItemViewHolder.back.setBackgroundResource(R.drawable.list_item_background)
        }
    }
}