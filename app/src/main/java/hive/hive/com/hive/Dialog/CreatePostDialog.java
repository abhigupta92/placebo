package hive.hive.com.hive.Dialog;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;

import hive.hive.com.hive.Activities.MainActivity;
import hive.hive.com.hive.R;
import hive.hive.com.hive.Utils.ConnectionUtils;
import hive.hive.com.hive.Utils.LocationUtils;
import hive.hive.com.hive.Utils.MediaUtils;
import hive.hive.com.hive.Utils.UserSessionUtils;

import static hive.hive.com.hive.Utils.LocationUtils.getLatitude;
import static hive.hive.com.hive.Utils.LocationUtils.getLongitude;
import static hive.hive.com.hive.Utils.LocationUtils.locationValue;

/**
 * Created by abhishekgupta on 19/10/16.
 */

public class CreatePostDialog extends DialogFragment implements View.OnClickListener {

    UserSessionUtils userSession;

    private static final int REQUEST_IMAGE_CAPTURE = 1888;
    ImageView imgView;
    EditText etTitle, etContent;
    Spinner spCategory;
    Button bSubmit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_post, container, false);

        getDialog().setTitle("Create Post");

        userSession = MainActivity.getUserSession();

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        imgView = (ImageView) view.findViewById(R.id.img_view_dialog_create_post);
        spCategory = (Spinner) view.findViewById(R.id.spinnerCategory_dialog_create_post);
        etTitle = (EditText) view.findViewById(R.id.etTitle_dialog_create_post);
        etContent = (EditText) view.findViewById(R.id.etContent_dialog_create_post);
        bSubmit = (Button) view.findViewById(R.id.bSubmit_dialog_create_post);

        bSubmit.setOnClickListener(this);
        imgView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bSubmit_dialog_create_post:

                LocationUtils.getCurrentLocation((FragmentActivity) getActivity(), locationValue);
                Log.d("LOCATION : ", getLatitude() + " : " + getLongitude());

                Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();
                ByteArrayOutputStream bao = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);

                ContentValues contentValues = new ContentValues();
                contentValues.put("profileID", userSession.getUserId());
                contentValues.put("name", userSession.getUserName());
                contentValues.put("title", etTitle.getText().toString());
                contentValues.put("content", etContent.getText().toString());
                contentValues.put("category", spCategory.getSelectedItem().toString());

                ConnectionUtils.setBitmap(bitmap);

                boolean postSuccess = ConnectionUtils.postToHive(contentValues);

                CreatePostDialogListener activity = (CreatePostDialogListener) getActivity();
                if (postSuccess) {
                    activity.onPostCreated();
                } else {
                    activity.onPostConnectionErrors();
                }
                break;
            case R.id.img_view_dialog_create_post:

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = MediaUtils.addWaterMark(imageBitmap);
            imgView.setImageBitmap(imageBitmap);
        }
    }

    public interface CreatePostDialogListener {
        void onPostCreated();

        void onPostConnectionErrors();
    }
}
