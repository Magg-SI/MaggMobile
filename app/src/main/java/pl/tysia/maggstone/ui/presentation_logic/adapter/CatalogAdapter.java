package pl.tysia.maggstone.ui.presentation_logic.adapter;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import pl.tysia.maggstone.ui.presentation_logic.filterer.CatalogFilterer;
import pl.tysia.maggstone.ui.presentation_logic.filterer.Filterer;


public abstract class CatalogAdapter<T extends ICatalogable, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    protected ArrayList<T> shownItems;
    public ArrayList<T> allItems;

    protected Filterer filterer;

    protected T selectedItem;

    private ArrayList<ItemSelectedListener> listeners;
    protected ArrayList<EmptyListListener> emptyListeners;


    public T getSelectedItem() {
        return selectedItem;
    }

    public Filterer getFilterer() {
        return filterer;
    }

    public void setFilterer(Filterer filterer) {
        this.filterer = filterer;
    }

    protected void onItemClick(View v, int adapterPosition){
        T item = shownItems.get(adapterPosition);

        selectedItem = item;
        notifyDataSetChanged();

        for (ItemSelectedListener listener : listeners) listener.onItemSelected(item);
    }

    public void addItemSelectedListener(ItemSelectedListener listener){
        listeners.add(listener);
    }

    public void addEmptyListener(EmptyListListener listener){
        emptyListeners.add(listener);
    }

    public void addItem(T item){
        allItems.add(0, item);
    }

    public void addAll(Collection<T> items){
        allItems.addAll(items);
    }


    public CatalogAdapter(ArrayList<T> items) {
        allItems = items;
        shownItems = new ArrayList<>();

        filterer = new CatalogFilterer<>(allItems, shownItems);

        listeners = new ArrayList<>();
        emptyListeners = new ArrayList<>();

    }

    public void filter(){
        filterer.filter();
    }


    @Override
    public int getItemCount() {
        return shownItems.size();
    }

    public interface ItemSelectedListener<T extends ICatalogable>{
        void onItemSelected(T item);
    }

    public interface EmptyListListener{
        void onListEmptied();
    }


}