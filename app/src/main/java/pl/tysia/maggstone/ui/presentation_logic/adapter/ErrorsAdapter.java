package pl.tysia.maggstone.ui.presentation_logic.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.tysia.maggstone.R;
import pl.tysia.maggstone.data.model.Error;
import pl.tysia.maggstone.data.service.QueueItem;
import pl.tysia.maggstone.ui.DownloadStateActivity;

public class ErrorsAdapter extends CatalogAdapter<Error, ErrorsAdapter.SendingViewHolder> {
    ErrorsListener listener;

    public ErrorsAdapter(ArrayList<Error> items) {
        super(items);

    }

    public void setListener(ErrorsListener listener) {
        this.listener = listener;
    }

    public class SendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        ImageView image;
        ImageButton forwardButton;
        ImageButton removeButton;

        private Observer<Integer> observer;

        SendingViewHolder(View v) {
            super(v);

            title = v.findViewById(R.id.title_tv);
            description = v.findViewById(R.id.description_tv);
            image = v.findViewById(R.id.icon_iv);
            forwardButton = v.findViewById(R.id.forward_button);
            removeButton = v.findViewById(R.id.remove_button);

            forwardButton.setOnClickListener(this);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onErrorRemoveClicked(allItems.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {
            Context context = title.getContext();

            int pos = getAdapterPosition();

            switch (allItems.get(pos).getType()){
                case Error.TYPE_DOWNLOAD :
                    context.startActivity(new Intent(context, DownloadStateActivity.class));
                    break;
                case Error.TYPE_PICTURE :
                    if (listener != null) listener.onPictureSendClicked(allItems.get(pos));
                    break;
            }
        }

    }

    @NonNull
    @Override
    public SendingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_error, viewGroup, false);

        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        SendingViewHolder vh = new SendingViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SendingViewHolder catalogItemViewHolder, int i) {
        Error item = shownItems.get(i);

        SendingViewHolder wareViewHolder = (SendingViewHolder) catalogItemViewHolder;

        wareViewHolder.title.setText(item.getTitle());
        wareViewHolder.description.setText(item.getDescription());

        switch (item.getType()){
            case Error.TYPE_CONNECTION :
                wareViewHolder.image.setImageResource(R.drawable.ic_round_wifi_off);
                wareViewHolder.forwardButton.setVisibility(View.GONE);
                wareViewHolder.removeButton.setVisibility(View.GONE);
                break;
            case Error.TYPE_DOWNLOAD :
                wareViewHolder.image.setImageResource(R.drawable.ic_round_sync_failed);
                wareViewHolder.forwardButton.setVisibility(View.VISIBLE);
                wareViewHolder.removeButton.setVisibility(View.GONE);
                break;
            case Error.TYPE_PICTURE :
                wareViewHolder.image.setImageResource(R.drawable.ic_round_image_not_sent);
                wareViewHolder.forwardButton.setVisibility(View.VISIBLE);
                wareViewHolder.removeButton.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SendingViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

    }

    public interface ErrorsListener{
        void onPictureSendClicked(Error error);

        void onErrorRemoveClicked(Error error);
    }
}
