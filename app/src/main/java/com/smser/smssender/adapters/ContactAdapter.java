package com.smser.smssender.adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smser.smssender.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private Context localContext;
    private Cursor contactData;
    private ItemClickListener clickListener;

    public ContactAdapter(Context context, Cursor contactData) {

        this.localContext = context;
        this.contactData = contactData;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(localContext).inflate(R.layout.contact_list_item, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        contactData.moveToPosition(position);
        holder.contName.setText(contactData.getString(contactData.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
        holder.contNumber.setText(contactData.getString(contactData.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

    }

    @Override
    public int getItemCount() {
        return contactData.getCount();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView contName, contNumber;
        private CheckBox selector;

        private ContactHolder(@NonNull View itemView) {
            super(itemView);
            contName = itemView.findViewById(R.id.contact_name);
            contNumber = itemView.findViewById(R.id.contact_number);
            selector = itemView.findViewById(R.id.contact_selector);

            selector.setClickable(false);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());

        }
    }
}
