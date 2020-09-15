package pl.tysia.maggstone.ui.presentation_logic.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import pl.tysia.maggstone.R;


public class BasicCatalogAdapter extends CatalogAdapter<ICatalogable, BasicCatalogAdapter.BasicViewHolder> {

    public BasicCatalogAdapter(ArrayList<ICatalogable> items) {
        super(items);

    }

    public class BasicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        View back;
        ImageButton deleteButton;

        BasicViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.title_tv);
            description = v.findViewById(R.id.description_tv);
            back = v.findViewById(R.id.back);
            deleteButton = v.findViewById(R.id.delete_button);


            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            onItemClick(v, pos);
        }

    }

    @NonNull
    @Override
    public BasicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_simple, viewGroup, false);

        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        BasicViewHolder vh = new BasicViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder catalogItemViewHolder, int i) {
        ICatalogable item = shownItems.get(i);

        BasicViewHolder wareViewHolder = (BasicViewHolder) catalogItemViewHolder;

        wareViewHolder.title.setText(item.getTitle());

        if (item == selectedItem){
            wareViewHolder.back.setBackgroundResource(R.drawable.list_item_background_selected);
        }else{
            wareViewHolder.back.setBackgroundResource(R.drawable.list_item_background);
        }


    }
}
