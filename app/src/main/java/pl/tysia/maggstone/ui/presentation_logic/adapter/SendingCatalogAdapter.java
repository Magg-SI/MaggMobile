package pl.tysia.maggstone.ui.presentation_logic.adapter;


import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.tysia.maggstone.R;
import pl.tysia.maggstone.data.service.QueueItem;

public class SendingCatalogAdapter extends CatalogAdapter<QueueItem, SendingCatalogAdapter.SendingViewHolder> {

    public SendingCatalogAdapter(ArrayList<QueueItem> items) {
        super(items);

    }

    public class SendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ProgressBar progress;

        private Observer<Integer> observer;

        SendingViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.name_tv);
            progress = v.findViewById(R.id.progress_bar);

            observer = new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    progress.setProgress(integer);
                }
            };

            v.setOnClickListener(this);

        }

        public void setObserver() {
            if (getAdapterPosition() >= 0)
                allItems.get(getAdapterPosition()).getPercentSent().observeForever(observer);
        }

        public void removeObserver(){
            if (getAdapterPosition() >= 0)
                allItems.get(getAdapterPosition()).getPercentSent().removeObserver(observer);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            onItemClick(v, pos);
        }

    }

    @NonNull
    @Override
    public SendingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_sending_state, viewGroup, false);

        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        SendingViewHolder vh = new SendingViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SendingViewHolder catalogItemViewHolder, int i) {
        QueueItem item = shownItems.get(i);

        SendingViewHolder wareViewHolder = (SendingViewHolder) catalogItemViewHolder;

        wareViewHolder.title.setText(item.getTitle());
        wareViewHolder.progress.setProgress(item.getPercentSent().getValue());

        wareViewHolder.setObserver();

    }


    @Override
    public void onViewDetachedFromWindow(@NonNull SendingViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.removeObserver();

    }
}
