package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.Order

class OrdersAdapter(items : ArrayList<Order>) : CatalogAdapter<Order, OrdersAdapter.OrderViewHolder>(items) {


    inner class OrderViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var title: TextView = v.findViewById(R.id.title_tv)
        var description: TextView = v.findViewById(R.id.description_tv)
        var back: View = v.findViewById(R.id.back)
        var checkImage: ImageView = v.findViewById(R.id.check_image)

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

        holder.title.text = item.title
        holder.description.text = item.description

        if (item.packed) {
            holder.title.isEnabled = false
            holder.description.isEnabled = false
            holder.checkImage.visibility = View.VISIBLE

        } else {
            holder.title.isEnabled = true
            holder.description.isEnabled = true
            holder.checkImage.visibility = View.GONE
        }
    }

}