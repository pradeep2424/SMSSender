package com.smser.smssender;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.smser.smssender.adapters.ContactAdapter;
import com.smser.smssender.adapters.ItemClickListener;
import com.smser.smssender.model.BlockData;

import java.util.ArrayList;

public class WebSmsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickListener {

    private Button btnContact, btnSubmit;
    private EditText smsText;
    private TextView textCount;
    private RecyclerView contactList;

    private ContactAdapter contactAdapter;
    private ArrayList<BlockData> blockList;

    public WebSmsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View webSmsView = inflater.inflate(R.layout.fragment_web_sms, container, false);

        blockList = new ArrayList<>();

        init(webSmsView);
        listener();

        return webSmsView;
    }

    private void init(View view) {

        btnContact = view.findViewById(R.id.btn_open_contact);
        btnSubmit = view.findViewById(R.id.btn_submit);

        smsText = view.findViewById(R.id.edt_web_template);

        textCount = view.findViewById(R.id.text_count);

        contactList = view.findViewById(R.id.contact_list);

        getLoaderManager().initLoader(1, null, this);
    }

    private void listener() {

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactList();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Starts the query

        Uri CONTACT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        return new CursorLoader(getActivity(), CONTACT_URI, null,
                null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC");
    }

    @Override
    public void onLoadFinished(@NonNull androidx.loader.content.Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();
        contactAdapter = new ContactAdapter(getActivity(), data);
    }

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<Cursor> loader) {

    }

    private void showContactList() {
        contactList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        contactList.setLayoutManager(layoutManager);
        contactAdapter.setClickListener(this);
        contactList.setAdapter(contactAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(contactList.getContext(),
                layoutManager.getOrientation());
        contactList.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onClick(View view, int position) {

        CheckBox selector = view.findViewById(R.id.contact_selector);
        TextView name = view.findViewById(R.id.contact_name);
        TextView num = view.findViewById(R.id.contact_number);

        BlockData data = new BlockData();
        data.setBlockName(name.getText().toString());
        data.setBlockNumber(num.getText().toString());

        if (selector.isChecked()) {
            selector.setChecked(false);
            removeAddData(data, "remove");
        } else {
            selector.setChecked(true);
            removeAddData(data, "add");
        }

        Log.e("onClick", "clicked");
    }

    private void removeAddData(BlockData data, String action) {

        if (blockList != null) {
            boolean flag = false;
            int i;
            for (i = 0; i < blockList.size(); i++) {

                if (blockList.get(i).getBlockNumber().equals(data.getBlockNumber())) {
                    if (action.equals("remove")) {
                        blockList.remove(i);
                    }
                    flag = true;
                    break;
                }
            }

            if (action.equals("add") && !flag) {
                blockList.add(data);
            }

        } else {

            if (action.equals("add")) {
                blockList = new ArrayList<>();
                blockList.add(data);
            }
        }
    }
}
