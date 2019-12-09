package com.smser.smssender;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.smser.smssender.comman.Constants;
import com.smser.smssender.comman.Utilities;
import com.smser.smssender.dbmanager.dbidentifier.MasterCaller;
import com.smser.smssender.definer.MainApp;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SmsTemplateFragment extends Fragment implements View.OnClickListener, Constants {

    private Switch tempFirst, tempSecond, tempThird, tempWhatsApp;
    private EditText templateText;
    private TextView charCount/*, imgName, videoName*/;
    private Button tempSave/*, selectImg, selectVideo*/;
    private EditText senderId, userId, password;

    private int PICK_IMAGE_MULTIPLE = 1;
    private int PICK_VIDEO_MULTIPLE = 2;

    public SmsTemplateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View templateView = inflater.inflate(R.layout.fragment_sms_template, container, false);

        init(templateView);
        listener();
        dataSetter();

        return templateView;
    }

    private void init(View view) {

        tempFirst = view.findViewById(R.id.switch_temp_first);
        tempSecond = view.findViewById(R.id.switch_temp_second);
        tempThird = view.findViewById(R.id.switch_temp_third);
        tempWhatsApp = view.findViewById(R.id.switch_temp_whats);

        templateText = view.findViewById(R.id.edt_template);

        charCount = view.findViewById(R.id.char_count);
        userId = view.findViewById(R.id.user_name);
        password = view.findViewById(R.id.password);
        senderId = view.findViewById(R.id.sender_id);
        tempSave = view.findViewById(R.id.btn_save_sms);

//        imgName = view.findViewById(R.id.img_name);
//        videoName = view.findViewById(R.id.video_name);
//        selectImg = view.findViewById(R.id.btn_select_img);
//        selectVideo = view.findViewById(R.id.btn_select_video);
    }

    private void listener() {

        tempFirst.setOnClickListener(this);
        tempSecond.setOnClickListener(this);
        tempThird.setOnClickListener(this);
        tempWhatsApp.setOnClickListener(this);

        tempSave.setOnClickListener(this);
//        selectImg.setOnClickListener(this);
//        selectVideo.setOnClickListener(this);

        templateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String msgLength = "160/" + s.length();
                charCount.setText(msgLength);
            }
        });

        senderId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainApp.storeValue(SENDERID, s.toString());
            }
        });

        userId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainApp.storeValue(USERNAME, s.toString());
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                MainApp.storeValue(PASSWORD, s.toString());
            }
        });
    }

    private void dataSetter() {

        switch (MainApp.getValue(LASTOPTION)) {
            case FIRSTSWITCH:
                tempFirst.performClick();
                break;
            case SECONDSWITCH:
                tempSecond.performClick();
                break;
            case THIRDSWITCH:
                tempThird.performClick();
                break;
            case WHATSAPPSWITCH:
                tempWhatsApp.performClick();
                break;
        }

        userId.setText(MainApp.getValue(USERNAME));
        password.setText(MainApp.getValue(PASSWORD));
        senderId.setText(MainApp.getValue(SENDERID));

        /*if (!MainApp.getValue(IMGPATH).equals("")) {
            imgName.setText(IMGTEXT);
        }
        if (!MainApp.getValue(VIDEOPATH).equals("")) {
            videoName.setText(VIDEOTEXT);
        }*/
    }

    private void msgShower(int switcher) {

        switch (switcher) {
            case 1:
                templateText.setText(MainApp.getValue(FIRSTSWITCH));
                break;
            case 2:
                templateText.setText(MainApp.getValue(SECONDSWITCH));
                break;
            case 3:
                templateText.setText(MainApp.getValue(THIRDSWITCH));
                break;
            case 4:
                templateText.setText(MainApp.getValue(WHATSAPPSWITCH));
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.switch_temp_first:

                if (tempFirst.isChecked()) {
                    tempSecond.setChecked(false);
                    tempThird.setChecked(false);
                    tempWhatsApp.setChecked(false);
                    msgShower(1);
                } else {
                    tempFirst.setChecked(true);
                }
                break;
            case R.id.switch_temp_second:

                if (tempSecond.isChecked()) {
                    tempFirst.setChecked(false);
                    tempThird.setChecked(false);
                    tempWhatsApp.setChecked(false);
                    msgShower(2);
                } else {
                    tempSecond.setChecked(true);
                }
                break;
            case R.id.switch_temp_third:

                if (tempThird.isChecked()) {
                    tempSecond.setChecked(false);
                    tempFirst.setChecked(false);
                    tempWhatsApp.setChecked(false);
                    msgShower(3);
                } else {
                    tempThird.setChecked(true);
                }
                break;
            case R.id.switch_temp_whats:

                if (tempWhatsApp.isChecked()) {
                    tempSecond.setChecked(false);
                    tempThird.setChecked(false);
                    tempFirst.setChecked(false);
                    msgShower(4);
                } else {
                    tempWhatsApp.setChecked(true);
                }
                break;
            /*case R.id.btn_select_img:
                Intent intent = new Intent();
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
                break;
            case R.id.btn_select_video:
                Intent intent1 = new Intent();
                intent1.setType("video/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Select Video"), PICK_VIDEO_MULTIPLE);
                break;*/
            case R.id.btn_save_sms:

                if (tempFirst.isChecked()) {
                    MainApp.storeValue(FIRSTSWITCH, templateText.getText().toString());
                    MainApp.storeValue(LASTOPTION, FIRSTSWITCH);
                }

                if (tempSecond.isChecked()) {
                    MainApp.storeValue(SECONDSWITCH, templateText.getText().toString());
                    MainApp.storeValue(LASTOPTION, SECONDSWITCH);
                }

                if (tempThird.isChecked()) {
                    MainApp.storeValue(THIRDSWITCH, templateText.getText().toString());
                    MainApp.storeValue(LASTOPTION, THIRDSWITCH);
                }

                if (tempWhatsApp.isChecked()) {
                    MainApp.storeValue(WHATSAPPSWITCH, templateText.getText().toString());
                    MainApp.storeValue(LASTOPTION, WHATSAPPSWITCH);
                }

                Utilities.showToast(getActivity(), getString(R.string.saved));
                MasterCaller.clearSentList();

//                openWhatsApp();
                break;
        }
    }

    private void openWhatsApp() {

        try {
            MainApp.storeValue(WHATSTOTAL, "" + (1 + Utilities.numberConverter(MainApp.getValue(WHATSTOTAL))));
            MainApp.storeValue(WHATSDAILYTOTAL, "" + (1 + Utilities.numberConverter(MainApp.getValue(WHATSDAILYTOTAL))));

//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=+918879937831&text=" + msg));
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MainApp.getValue("imgPath")));
//            context.startActivity(intent);

//            Log.e("openWhatsApp", MainApp.getValue("imgPath"));
//            Intent share = new Intent(Intent.ACTION_SEND);
//            share.setType("image/jpeg");
//            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(MainApp.getValue("imgPath")));
//            share.setPackage("com.whatsapp");//package name of the app
//            startActivity(Intent.createChooser(share, "Share Image"));

//            Intent sendIntent = new Intent();
////            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.setPackage("com.whatsapp");
//            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MainApp.getValue(IMGPATH)));
////            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
////            sendIntent.setType("text/plain");
//            sendIntent.setType("*/*");
//            startActivity(sendIntent);
////            startActivity(Intent.createChooser(sendIntent, "Share image using"));

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=+918879937831&image=" + MainApp.getValue(IMGPATH)));
            startActivity(intent);

//            try {
//                startActivity(whatsappIntent);
//            } catch (android.content.ActivityNotFoundException ex) {
//                Toast.makeText(getActivity(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
//            }

        } catch (Exception e) {
            Log.e("openWhatsApp", e.toString());
        }

        Log.e("openWhatsApp", "sent");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {

            ArrayList<Uri> mArrayUri = new ArrayList<>();

            if ((requestCode == PICK_IMAGE_MULTIPLE || requestCode == PICK_VIDEO_MULTIPLE) && resultCode == RESULT_OK && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                ArrayList<String> imagesEncodedList = new ArrayList<>();
                String imageEncoded;
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();
                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(mImageUri, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();

                    mArrayUri.add(mImageUri);

                } else {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                        if (data.getClipData() != null) {

                            ClipData mClipData = data.getClipData();

                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mArrayUri.add(uri);
                                // Get the cursor
                                Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imageEncoded = cursor.getString(columnIndex);
                                imagesEncodedList.add(imageEncoded);
                                cursor.close();
                            }

                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        }
                    }
                }

                if (requestCode == PICK_IMAGE_MULTIPLE) {
                    if (mArrayUri.size() > 0) {
                        MainApp.storeValue(IMGPATH, String.valueOf(mArrayUri.get(0)));
//                        imgName.setText(IMGTEXT);
                    }
                } else if (requestCode == PICK_VIDEO_MULTIPLE) {
                    if (mArrayUri.size() > 0) {
                        MainApp.storeValue(VIDEOPATH, String.valueOf(mArrayUri.get(0)));
//                        videoName.setText(VIDEOTEXT);
                    }
                }
            } else {
                Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

//        if (resultCode == RESULT_OK && data != null) {
//            if (requestCode == PICK_IMAGE_MULTIPLE) {
//                Uri selectedImageUri = data.getData();
//                MainApp.storeValue("imgPath", getImagePath(selectedImageUri));
//                Log.e("imgPath", MainApp.getValue("imgPath"));
//            } else if (requestCode == PICK_VIDEO_MULTIPLE) {
//                Uri selectedImageUri = data.getData();
//                MainApp.storeValue("videoPath", getVideoPath(selectedImageUri));
//                Log.e("videoPath", MainApp.getValue("videoPath"));
//            }
//        }

        super.onActivityResult(requestCode, resultCode, data);
    }

//    private String getImagePath(Uri uri) {
//        // just some safety built in
//        if (uri == null) {
//            return null;
//        }
//        // try to retrieve the image from the media store first
//        // this will only work for images selected from gallery
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
//        if (cursor != null) {
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String path = cursor.getString(column_index);
//            cursor.close();
//            return path;
//        }
//        // this is our fallback here
//        return uri.getPath();
//    }
//
//    private String getVideoPath(Uri uri) {
//        String[] projection = {MediaStore.Video.Media.DATA};
//        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
//        if (cursor != null) {
//            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else
//            return null;
//    }
}
