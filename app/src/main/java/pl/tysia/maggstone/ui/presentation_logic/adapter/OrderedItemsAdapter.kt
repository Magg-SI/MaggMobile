package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.constants.PackingState
import pl.tysia.maggstone.data.model.OrderedWare

class OrderedItemsAdapter(items : ArrayList<OrderedWare>) : CatalogAdapter<OrderedWare, OrderedItemsAdapter.BasicViewHolder>(items) {

    inner class BasicViewHolder(v: View) : RecyclerView.ViewHolder(v),
        View.OnClickListener {
        var title: TextView = v.findViewById(R.id.title_tv)
        var description: TextView = v.findViewById(R.id.description_tv)
        var back: View = v.findViewById(R.id.back)
        var imageView: ImageView = v.findViewById(R.id.stateImage)
        var subtitle: TextView = v.findViewById(R.id.subtitle_tv)
        var additionalInfo: TextView = v.findViewById(R.id.additional_info_tv)

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
        val context: Context = catalogItemViewHolder.back.context

        catalogItemViewHolder.title.text = item.getTitle()

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


        when (item.getPackingState()){
            PackingState.UNTOUCHED -> {
                catalogItemViewHolder.imageView.setImageResource(0)
                catalogItemViewHolder.imageView.visibility = View.GONE
            }
            PackingState.PACKED -> {
                catalogItemViewHolder.imageView.visibility = View.VISIBLE
                catalogItemViewHolder.imageView.setImageResource(R.drawable.round_check_24)
                val color = ContextCompat.getColor(context, R.color.colorAccent);
                ImageViewCompat.setImageTintList(catalogItemViewHolder.imageView, ColorStateList.valueOf(color));
            }
            PackingState.STARTED -> {
                catalogItemViewHolder.imageView.visibility = View.VISIBLE
                catalogItemViewHolder.imageView.setImageResource(R.drawable.ic_baseline_more)
                val color = ContextCompat.getColor(context, R.color.colorAccentRed);
                ImageViewCompat.setImageTintList(catalogItemViewHolder.imageView, ColorStateList.valueOf(color));
            }
        }

    }

}