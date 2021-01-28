package pl.tysia.maggstone.ui.presentation_logic.adapter;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.tysia.maggstone.R;
import pl.tysia.maggstone.data.model.DocumentItem;


public class DocumentAdapter<T extends DocumentItem> extends CatalogAdapter<T, DocumentAdapter<T>.DocumentViewHolder> {

    public DocumentAdapter(ArrayList<T> items) {
        super(items);

    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder implements TextWatcher, View.OnClickListener {
        TextView title;
        TextView description;
        TextView name;
        ConstraintLayout back;
        ImageButton deleteButton;
        ImageButton infoButton;

        ImageView moreButton;
        ImageView lessButton;
        EditText numberET;

        ImageView checkImage;


        DocumentViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.title_tv);
            description = v.findViewById(R.id.description_tv);
            name = v.findViewById(R.id.name_tv);
            back = v.findViewById(R.id.back);
            deleteButton = v.findViewById(R.id.delete_button);
            infoButton = v.findViewById(R.id.info_button);
            moreButton = v.findViewById(R.id.more_button);
            lessButton = v.findViewById(R.id.less_button);
            numberET = v.findViewById(R.id.number_et);

            checkImage = v.findViewById(R.id.check_image);

            numberET.addTextChangedListener(this);

            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentItem item = shownItems.get(getAdapterPosition());

                    if (item.getIlosc() < Double.MAX_VALUE)
                        item.setIlosc(item.getIlosc() + 1);

                    notifyDataSetChanged();
                }
            });

            lessButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentItem item = (DocumentItem) shownItems.get(getAdapterPosition());

                    if (item.getIlosc() > 0)
                        item.setIlosc(item.getIlosc() - 1);

                    notifyDataSetChanged();

                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allItems.remove(allItems.get(getAdapterPosition()));
                    filter();
                    notifyDataSetChanged();
                }
            });

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            onItemClick(v, pos);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            DocumentItem item = (DocumentItem) shownItems.get(getAdapterPosition());

            try {
                double quantity = Double.parseDouble(numberET.getText().toString());

                if (quantity >= 0)
                    item.setIlosc(quantity);
                else numberET.setText("0");

            }catch (NumberFormatException ex){
                numberET.setText("0");
            }


        }
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_document, viewGroup, false);

        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return new DocumentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        DocumentItem item = shownItems.get(position);
        Context context = holder.back.getContext();

        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.name.setText(item.getShortDescription());

        holder.numberET.setText(Double.toString(item.getIlosc()));

    }
}
