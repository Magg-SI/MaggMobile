package pl.tysia.maggstone.ui.presentation_logic.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.Error
import pl.tysia.maggstone.ui.DownloadStateActivity
import java.util.*

class ErrorsAdapter(items: ArrayList<Error>) :
    CatalogAdapter<Error, ErrorsAdapter.SendingViewHolder>(items) {

    var listener: ErrorsListener? = null

    inner class SendingViewHolder internal constructor(v: View) :
        RecyclerView.ViewHolder(v), View.OnClickListener {

        var title: TextView = v.findViewById(R.id.title_tv)
        var description: TextView = v.findViewById(R.id.subtitle_tv)
        var image: ImageView = v.findViewById(R.id.icon_iv)
        var forwardButton: ImageButton = v.findViewById(R.id.forward_button)
        var removeButton: ImageButton = v.findViewById(R.id.remove_button)

        private val observer: Observer<Int>? = null

        override fun onClick(v: View) {
            val context = title.context
            val pos = adapterPosition
            when (allItems[pos]!!.type) {
                Error.TYPE_DOWNLOAD -> context.startActivity(
                    Intent(
                        context,
                        DownloadStateActivity::class.java
                    )
                )
                Error.TYPE_PICTURE -> if (listener != null) listener!!.onPictureSendClicked(
                    allItems[pos]
                )
            }
        }

        init {
            forwardButton.setOnClickListener(this)
            removeButton.setOnClickListener {
                listener!!.onErrorRemoveClicked(
                    allItems[adapterPosition]
                )
            }
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        i: Int
    ): SendingViewHolder {

        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_item_error, viewGroup, false)

        v.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        return SendingViewHolder(v)
    }

    override fun onBindViewHolder(
        catalogItemViewHolder: SendingViewHolder,
        i: Int
    ) {

        val item = shownItems[i]!!

        val wareViewHolder =
            catalogItemViewHolder

        wareViewHolder.title.text = item.getTitle()
        wareViewHolder.description.text = item.getDescription()

        when (item.type) {
            Error.TYPE_CONNECTION -> {
                wareViewHolder.image.setImageResource(R.drawable.ic_round_wifi_off)
                wareViewHolder.forwardButton.visibility = View.GONE
                wareViewHolder.removeButton.visibility = View.GONE
            }
            Error.TYPE_DOWNLOAD -> {
                wareViewHolder.image.setImageResource(R.drawable.ic_round_sync_failed)
                wareViewHolder.forwardButton.visibility = View.VISIBLE
                wareViewHolder.removeButton.visibility = View.GONE
            }
            Error.TYPE_PICTURE -> {
                wareViewHolder.image.setImageResource(R.drawable.ic_round_image_not_sent)
                wareViewHolder.forwardButton.visibility = View.VISIBLE
                wareViewHolder.removeButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: SendingViewHolder) {
        super.onViewDetachedFromWindow(holder)
    }

    interface ErrorsListener {
        fun onPictureSendClicked(error: Error)
        fun onErrorRemoveClicked(error: Error)
    }
}