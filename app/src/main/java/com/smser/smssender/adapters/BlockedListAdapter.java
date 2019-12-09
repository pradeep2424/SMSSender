package com.smser.smssender.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smser.smssender.R;
import com.smser.smssender.model.BlockData;

import java.util.ArrayList;

public class BlockedListAdapter extends RecyclerView.Adapter<BlockedListAdapter.BlockListHolder> {

    private Context localContext;
    private ArrayList<BlockData> dataList;
    private LongClickListener clickListener;

    public BlockedListAdapter(Context context, ArrayList<BlockData> dataList) {

        this.localContext = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public BlockListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(localContext).inflate(R.layout.block_list_item, parent, false);
        return new BlockListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockListHolder holder, int position) {

        BlockData data = dataList.get(position);
        holder.contName.setText(data.getBlockName());
        holder.contNumber.setText(data.getBlockNumber());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setClickListener(LongClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public class BlockListHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private TextView contName, contNumber;

        private BlockListHolder(@NonNull View itemView) {
            super(itemView);

            contName = itemView.findViewById(R.id.block_contact_name);
            contNumber = itemView.findViewById(R.id.block_contact_number);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {

            if (clickListener != null) clickListener.onLongClick(v, getAdapterPosition());

            return true;
        }
    }
}
