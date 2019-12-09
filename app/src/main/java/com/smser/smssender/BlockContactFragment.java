package com.smser.smssender;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.smser.smssender.adapters.BlockedListAdapter;
import com.smser.smssender.adapters.ContactAdapter;
import com.smser.smssender.adapters.ItemClickListener;
import com.smser.smssender.adapters.LongClickListener;
import com.smser.smssender.dbmanager.dbidentifier.MasterCaller;
import com.smser.smssender.model.BlockData;

import java.util.ArrayList;

public class BlockContactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        ItemClickListener, LongClickListener {

    private Button openContact;
    private RecyclerView blockedList;

    private ContactAdapter contactAdapter;
    private ArrayList<BlockData> blockList;

    public BlockContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View blockView = inflater.inflate(R.layout.fragment_block_contact, container, false);

        blockList = new ArrayList<>();

        init(blockView);
        listener();

        return blockView;
    }

    private void init(View view) {

        openContact = view.findViewById(R.id.btn_open_contact);
        blockedList = view.findViewById(R.id.block_list);

        getLoaderManager().initLoader(1, null, this);
    }

    private void listener() {

        openContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactList(contactAdapter);
            }
        });

        blockList = MasterCaller.getBlockList();

        if (blockList != null && blockList.size() > 0) {
            updateBlockList();
        }

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        Uri CONTACT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        return new CursorLoader(getActivity(), CONTACT_URI, null,
                null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();
        contactAdapter = new ContactAdapter(getActivity(), data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

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

    private void showContactList(ContactAdapter adapter) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.contact_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        Button select = dialog.findViewById(R.id.ok_button);
        RecyclerView cList = dialog.findViewById(R.id.contact_list);

        cList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        cList.setLayoutManager(layoutManager);
        adapter.setClickListener(this);
        cList.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(cList.getContext(),
                layoutManager.getOrientation());
        cList.addItemDecoration(dividerItemDecoration);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickEvent(dialog);
            }
        });

        dialog.show();
    }

    private void clickEvent(Dialog dialog) {

        if (blockList != null && blockList.size() > 0) {

            MasterCaller.clearBlockList();
            MasterCaller.insertBlockList(blockList);
            updateBlockList();
        }

        dialog.dismiss();
    }

    @Override
    public void onLongClick(View view, int position) {

        BlockData data = blockList.get(position);

        removeAddData(data, "remove");
        MasterCaller.clearBlockList();
        MasterCaller.insertBlockList(blockList);
        updateBlockList();
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

    private void updateBlockList() {
        BlockedListAdapter blockedListAdapter = new BlockedListAdapter(getActivity(), blockList);
        blockedList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        blockedList.setLayoutManager(layoutManager);
        blockedListAdapter.setClickListener(this);
        blockedList.setAdapter(blockedListAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(blockedList.getContext(),
                layoutManager.getOrientation());
        blockedList.addItemDecoration(dividerItemDecoration);
    }
}
