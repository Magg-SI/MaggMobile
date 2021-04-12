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
import pl.tysia.maggstone.data.model.Order

class OrdersAdapter(items : ArrayList<Order>) : CatalogAdapter<Order, OrdersAdapter.OrderViewHolder>(items) {

    inner class OrderViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var title: TextView = v.findViewById(R.id.title_tv)
        var description: TextView = v.findViewById(R.id.subtitle_tv)
        var back: View = v.findViewById(R.id.back)
        var imageView: ImageView = v.findViewById(R.id.stateImage2)

        override fun onClick(v: View) {
            val pos: Int = adapterPosition
            onItemClick(v, pos)
        }

        init {
            v.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): OrderViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_order, viewGroup, false)
        v.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        return OrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = shownItems[position]
        val context: Context = holder.back.context

        holder.title.text = item.getTitle()
        holder.description.text = item.getDescription()

        when (item.getPackingState()){
            PackingState.UNTOUCHED -> {
                val color = ContextCompat.getColor(context, R.color.lightGray);
                holder.title.setTextColor(color)

                holder.imageView.setImageResource(0)
            }
            PackingState.PACKED -> {
                holder.imageView.setImageResource(R.drawable.round_check_24)
                val color = ContextCompat.getColor(context, R.color.colorAccentBlue);
                holder.title.setTextColor(color)
                ImageViewCompat.setImageTintList(holder.imageView, ColorStateList.valueOf(color));
            }
            PackingState.STARTED -> {
                holder.imageView.setImageResource(R.drawable.ic_baseline_more)
                val color = ContextCompat.getColor(context, R.color.colorAccentRed);
                holder.title.setTextColor(color)
                ImageViewCompat.setImageTintList(holder.imageView, ColorStateList.valueOf(color));
            }
        }
    }

}