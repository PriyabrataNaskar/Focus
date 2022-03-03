package com.priyo.focus.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.priyo.focus.R;

import java.util.List;

/**
 * Created by Priyabrata Naskar on 03-03-2022.
 */
public class StatAdapter extends RecyclerView.Adapter<StatAdapter.ViewHolder> {

    // Member variables.
    private List<Integer> mData;
    private Context mContext;

    public StatAdapter(List<Integer> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public StatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.item_statistics, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatAdapter.ViewHolder holder, int position) {
        // Get current item time
        Integer currentTime = mData.get(position);

        // Populate the textViews with data.
        holder.bindTo(currentTime);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Member Variables for the TextViews
        private TextView mMessageTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views.
            mMessageTextView = itemView.findViewById(R.id.message_text);
        }

        public void bindTo(Integer currentTime) {
            String message = "I just put my phone down for " + currentTime + " minutes to focus.";
            mMessageTextView.setText(message);

        }
    }
}