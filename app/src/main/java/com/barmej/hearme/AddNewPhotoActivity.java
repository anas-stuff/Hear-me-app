package com.barmej.hearme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AddNewPhotoActivity extends AppCompatActivity {

    private ImageView mNewPhotoIv, mNewSoundIv;

    private Uri mSelectedPhotoUri;
    private Uri mSelectedSoundUri;
    private final static int READ_PHOTO_FROM_GALLERY_PERMISSION = 100;
    private final static int READ_SOUND_FROM_GALLERY_PERMISSION = 200;
    private final static int PICK_IMAGE = 105;
    private final static int PICK_SOUND = 205;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_photo_ativity);

        mNewPhotoIv = findViewById(R.id.image_view_new_photo);
        mNewSoundIv = findViewById(R.id.image_view_sound_added);

        Button selectPhotoBt = findViewById(R.id.button_select_photo);
        Button selectSoundBt = findViewById(R.id.button_select_sound);
        Button submitBt = findViewById(R.id.button_submit);

        selectPhotoBt.setOnClickListener(listener -> selectPhoto());
        selectSoundBt.setOnClickListener(listener -> selectSound());
        submitBt.setOnClickListener(listener -> submit());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == READ_PHOTO_FROM_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                firePickPhotoIntent();
            else
                Toast.makeText(this, R.string.read_permission_needed_to_access_files, Toast.LENGTH_LONG).show();
        } else if(requestCode == READ_SOUND_FROM_GALLERY_PERMISSION)
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                firePickSoundIntent();
            else
                Toast.makeText(this, R.string.read_permission_needed_to_access_files, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                setSelectedPhoto(data.getData());

                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else
                Toast.makeText(this, R.string.failed_to_get_photo, Toast.LENGTH_LONG).show();
        } else if(requestCode == PICK_SOUND)
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                setSelectedSound(data.getData());

                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else
                Toast.makeText(this, R.string.failed_to_get_sound, Toast.LENGTH_LONG).show();

    }

    private void selectPhoto(){
        // Check permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_PHOTO_FROM_GALLERY_PERMISSION);
        } else {
            firePickPhotoIntent();
        }
    }

    private void setSelectedPhoto(Uri data){
        mNewPhotoIv.setImageURI(data);
        mSelectedPhotoUri = data;
    }

    private  void firePickPhotoIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }

    private void selectSound(){
        // Check permission
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_SOUND_FROM_GALLERY_PERMISSION);
        } else {
            firePickSoundIntent();
        }
    }

    private void setSelectedSound(Uri data){
        mNewSoundIv.setVisibility(View.VISIBLE);
        mSelectedSoundUri = data;
    }

    private void firePickSoundIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_audio)), PICK_SOUND);
    }

    private void submit(){
        if(mSelectedPhotoUri != null && mSelectedSoundUri != null){
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_PHOTO_URI, mSelectedPhotoUri);
            intent.putExtra(Constants.EXTRA_SOUND_URI, mSelectedSoundUri);
            setResult(RESULT_OK, intent);
            finish();
        } else
            Toast.makeText(this, R.string.select_picture_and_sound, Toast.LENGTH_LONG).show();
    }
}