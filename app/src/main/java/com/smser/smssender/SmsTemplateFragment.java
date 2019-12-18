package com.smser.smssender;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//import com.fxn.pix.Options;
//import com.fxn.pix.Pix;
//import com.fxn.utility.ImageQuality;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.smser.smssender.comman.Constants;
import com.smser.smssender.comman.Utilities;
import com.smser.smssender.dbmanager.dbidentifier.MasterCaller;
import com.smser.smssender.definer.MainApp;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SmsTemplateFragment extends Fragment implements View.OnClickListener, Constants {

    private Switch tempFirst, tempSecond, tempThird, tempWhatsApp;
    private EditText templateText;
    private TextView charCount/*, imgName, videoName*/;
    private Button tempSave/*, selectImg, selectVideo*/;
    private EditText senderId, userId, password;

    private LinearLayout llUploadLayout;
    private ImageView ivTemplateImage;
    private Button btnUploadPicture;

    String whatsAppTemplateImg;

    private int PICK_IMAGE_MULTIPLE = 1;
    private int PICK_VIDEO_MULTIPLE = 2;

    private int PICK_WHATSAPP_TEMPLATE_IMAGE = 100;

    public SmsTemplateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        llUploadLayout = view.findViewById(R.id.ll_uploadImage);
        btnUploadPicture = view.findViewById(R.id.btn_uploadPicture);
        ivTemplateImage = view.findViewById(R.id.iv_templateImage);

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
        btnUploadPicture.setOnClickListener(this);
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

        whatsAppTemplateImg = MainApp.getValue(WHATSAPPS_TEMPLATE_IMAGE);
        if (whatsAppTemplateImg != null && whatsAppTemplateImg.trim().length() > 0) {
            setWhatsAppTemplateImage();
        }

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

                    llUploadLayout.setVisibility(View.VISIBLE);

                } else {
                    tempWhatsApp.setChecked(true);
                    llUploadLayout.setVisibility(View.VISIBLE);
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

            case R.id.btn_uploadPicture:

                ImagePicker.create(this)
                        .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                        .folderMode(true) // folder mode (false by default)
//                        .toolbarFolderTitle("Folder") // folder selection title
                        .toolbarImageTitle("Tap to select") // image selection title
//                        .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
//                        .includeVideo(true) // Show video on image picker
                        .single() // single mode
//                        .multi() // multi mode (default mode)
//                        .limit(10) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
//                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
//                        .origin(images) // original selected images, used in multi mode
//                        .exclude(images) // exclude anything that in image.getPath()
//                        .excludeFiles(files) // same as exclude but using ArrayList<File>
//                        .theme(R.style.CustomImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
                        .enableLog(false) // disabling log
                        .start(); // start image picker activity with request code

//                Options options = Options.init()
//                        .setCount(1)                                                         //Number of images to restict selection count
//                        .setFrontfacing(false)                                                //Front Facing camera on start
//                        .setImageQuality(ImageQuality.HIGH)                                  //Image Quality
////                        .setPreSelectedUrls(returnValue)                                     //Pre selected Image Urls
//                        .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)           //Orientaion
//                        .setRequestCode(PICK_WHATSAPP_TEMPLATE_IMAGE)
//                        .setPath("/pix/images");                                             //Custom Path For Image Storage
//
//                Pix.start(getActivity(), options);
//
////                Pix.start(context, Options.init().setRequestCode(100));

                break;

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

                    if (whatsAppTemplateImg != null && whatsAppTemplateImg.trim().length() > 0) {
                        MainApp.storeValue(WHATSAPPS_TEMPLATE_IMAGE, whatsAppTemplateImg);
                    }

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

    private String createDirectoryAndSaveFile(String srcPath, String fileName) {
        String dstPath = null;

        File direct = new File(Environment.getExternalStorageDirectory() + "/SMSSender");
        if (!direct.exists()) {
            direct.mkdirs();
        }

        try {
            dstPath = direct + File.separator + fileName;
            FileUtils.copyFile(new File(srcPath), new File(dstPath));

//            FileInputStream inStream = new FileInputStream(src);
//            FileOutputStream outStream = new FileOutputStream(dst);
//            FileChannel inChannel = inStream.getChannel();
//            FileChannel outChannel = outStream.getChannel();
//            inChannel.transferTo(0, inChannel.size(), outChannel);
//            inStream.close();
//            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dstPath;
    }

    private void setWhatsAppTemplateImage() {
        Bitmap bmImg = BitmapFactory.decodeFile(whatsAppTemplateImg);
        ivTemplateImage.setImageBitmap(bmImg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
//            List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            Image image = ImagePicker.getFirstImageOrNull(data);
            String imagePath = image.getPath();
            String imageName = image.getName();
            whatsAppTemplateImg = createDirectoryAndSaveFile(imagePath, imageName);

            if (whatsAppTemplateImg != null && whatsAppTemplateImg.trim().length() > 0) {
                setWhatsAppTemplateImage();
            }
        }

//        switch (requestCode) {
//            case (100): {
//                if (resultCode == Activity.RESULT_OK) {
//                    ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
//                    String url = "";
//
//                    if (returnValue != null) {
//                        url = returnValue.get(0);
//                        MainApp.storeValue(WHATSAPPS_TEMPLATE_IMAGE, url);
//
//                    } else {
//                        Toast.makeText(getActivity(), "Image not found", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//            break;
//        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        try {
//
//            ArrayList<Uri> mArrayUri = new ArrayList<>();
//
//            if ((requestCode == PICK_IMAGE_MULTIPLE || requestCode == PICK_VIDEO_MULTIPLE) && resultCode == RESULT_OK && null != data) {
//                // Get the Image from data
//
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                ArrayList<String> imagesEncodedList = new ArrayList<>();
//                String imageEncoded;
//                if (data.getData() != null) {
//
//                    Uri mImageUri = data.getData();
//                    // Get the cursor
//                    Cursor cursor = getActivity().getContentResolver().query(mImageUri, filePathColumn, null, null, null);
//                    // Move to first row
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    imageEncoded = cursor.getString(columnIndex);
//                    cursor.close();
//
//                    mArrayUri.add(mImageUri);
//
//                } else {
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//
//                        if (data.getClipData() != null) {
//
//                            ClipData mClipData = data.getClipData();
//
//                            for (int i = 0; i < mClipData.getItemCount(); i++) {
//
//                                ClipData.Item item = mClipData.getItemAt(i);
//                                Uri uri = item.getUri();
//                                mArrayUri.add(uri);
//                                // Get the cursor
//                                Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
//                                // Move to first row
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                imageEncoded = cursor.getString(columnIndex);
//                                imagesEncodedList.add(imageEncoded);
//                                cursor.close();
//                            }
//
//                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
//                        }
//                    }
//                }
//
//                if (requestCode == PICK_IMAGE_MULTIPLE) {
//                    if (mArrayUri.size() > 0) {
//                        MainApp.storeValue(IMGPATH, String.valueOf(mArrayUri.get(0)));
////                        imgName.setText(IMGTEXT);
//                    }
//                } else if (requestCode == PICK_VIDEO_MULTIPLE) {
//                    if (mArrayUri.size() > 0) {
//                        MainApp.storeValue(VIDEOPATH, String.valueOf(mArrayUri.get(0)));
////                        videoName.setText(VIDEOTEXT);
//                    }
//                }
//            } else if (resultCode == RESULT_OK && requestCode == PICK_WHATSAPP_TEMPLATE_IMAGE) {
//                ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
//                String url = "";
//
//                if (returnValue != null) {
//                    url = returnValue.get(0);
//                    MainApp.storeValue(WHATSAPPS_TEMPLATE_IMAGE, url);
//
//                } else {
//                    Toast.makeText(getActivity(), "Image not found", Toast.LENGTH_LONG).show();
//                }
//
//            } else {
//                Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
//        }
//
////        if (resultCode == RESULT_OK && data != null) {
////            if (requestCode == PICK_IMAGE_MULTIPLE) {
////                Uri selectedImageUri = data.getData();
////                MainApp.storeValue("imgPath", getImagePath(selectedImageUri));
////                Log.e("imgPath", MainApp.getValue("imgPath"));
////            } else if (requestCode == PICK_VIDEO_MULTIPLE) {
////                Uri selectedImageUri = data.getData();
////                MainApp.storeValue("videoPath", getVideoPath(selectedImageUri));
////                Log.e("videoPath", MainApp.getValue("videoPath"));
////            }
////        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

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
