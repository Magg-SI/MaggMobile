package pl.tysia.maggstone.ui.technicians

import android.view.View
import pl.tysia.maggstone.R
import pl.tysia.maggstone.data.model.Technician
import pl.tysia.maggstone.ui.presentation_logic.adapter.BasicCatalogAdapter
import pl.tysia.maggstone.ui.presentation_logic.adapter.ICatalogable
import java.util.ArrayList

class TechniciansAdapter(items: ArrayList<ICatalogable>) : BasicCatalogAdapter(items) {
    val selectedItems = ArrayList<ICatalogable>()

    override fun onItemClick(v: View?, adapterPosition: Int) {
        super.onItemClick(v, adapterPosition)

        val item = allItems[adapterPosition]

        if (selectedItems.contains(item)){
            selectedItems.remove(item)
        }else{
            selectedItems.add(item)
        }

        for (listener in listeners) listener.onItemSelected(item)
        notifyDataSetChanged()
    }

    override fun applySelection(item: ICatalogable, holder: BasicViewHolder) {
        if (selectedItems.contains(item)) {
            holder.back.setBackgroundResource(R.drawable.list_item_background_selected)
        } else {
            holder.back.setBackgroundResource(R.drawable.list_item_background)
        }
    }
}