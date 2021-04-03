package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.service.QueueItem
import java.util.*

class SendingCatalogAdapter(items: ArrayList<QueueItem>) :
    CatalogAdapter<QueueItem, SendingCatalogAdapter.SendingViewHolder>(
        items
    ) {

    inner class SendingViewHolder internal constructor(v: View) :
        RecyclerView.ViewHolder(v), View.OnClickListener {

        var title: TextView = v.findViewById(R.id.name_tv)
        var progress: ProgressBar = v.findViewById(R.id.progress_bar)

        private val observer: Observer<Int>

        fun setObserver() {
            if (adapterPosition >= 0) allItems[adapterPosition]!!.percentSent.observeForever(
                observer
            )
        }

        fun removeObserver() {
            if (adapterPosition >= 0) allItems[adapterPosition]!!.percentSent.removeObserver(
                observer
            )
        }

        override fun onClick(v: View) {
            val pos = adapterPosition
            onItemClick(v, pos)
        }

        init {
            observer = Observer { integer -> progress.progress = integer!! }
            v.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): SendingViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_sending_state, viewGroup, false)
        v.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        return SendingViewHolder(
            v
        )
    }

    override fun onBindViewHolder(
        catalogItemViewHolder: SendingViewHolder,
        i: Int
    ) {
        val item = shownItems[i]

        catalogItemViewHolder.title.text = item.getTitle()
        catalogItemViewHolder.progress.progress = item.percentSent.value!!
        catalogItemViewHolder.setObserver()
    }

    override fun onViewDetachedFromWindow(holder: SendingViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.removeObserver()
    }
}