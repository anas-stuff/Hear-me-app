package com.barmej.hearme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.barmej.hearme.adapter.PhotoSoundAdapter;
import com.barmej.hearme.data.PhotoSound;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<PhotoSound> mItems;
    private PhotoSoundAdapter mAdapter;
    Menu mMenu;
    private RecyclerView.LayoutManager mGridLayoutManager, mListLayoutManager;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onStart() {
        super.onStart();
        mMediaPlayer = new MediaPlayer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mItems = new ArrayList<>();
        mAdapter = new PhotoSoundAdapter(mItems, position -> playSound(position), position -> deleteItem(position));

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mListLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mListLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // Add button click listener
        findViewById(R.id.floating_button_add).setOnClickListener(listener -> startAddNewPhotoActivity());
    }

    private void startAddNewPhotoActivity(){
        Intent intent = new Intent(this, AddNewPhotoActivity.class);
        startActivityForResult(intent, Constants.ADD_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.ADD_PHOTO){
            if(resultCode == RESULT_OK && data != null) {
                Uri photoUri = data.getParcelableExtra(Constants.EXTRA_PHOTO_URI);
                Uri soundUri = data.getParcelableExtra(Constants.EXTRA_SOUND_URI);

                addItem(new PhotoSound(photoUri, soundUri));
            } else {
                Toast.makeText(this, R.string.didnt_add_photo, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addItem(PhotoSound photoSound){
        mItems.add(photoSound); // Add item to array list
        mAdapter.notifyItemInserted(mItems.size() - 1); // Notify adapter
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         if(item.getItemId() == R.id.action_grid) {
             mRecyclerView.setLayoutManager(mGridLayoutManager);
             item.setVisible(false);
             mMenu.findItem(R.id.action_list).setVisible(true);
             return true;
         } else if(item.getItemId() == R.id.action_list) {
             mRecyclerView.setLayoutManager(mListLayoutManager);
             item.setVisible(false);
             mMenu.findItem(R.id.action_grid).setVisible(true);
             return true;
         }
         return super.onOptionsItemSelected(item);
    }

    private void playSound(int position){
        PhotoSound photoSound = mItems.get(position);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(this, photoSound.getSound());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, R.string.couldnt_play_sound, Toast.LENGTH_LONG).show();
        }
    }

    private void deleteItem(int position){
        // Dialog
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    mItems.remove(position); // Remove item from array list
                    mAdapter.notifyItemRemoved(position); // Notify adapter
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                    dialogInterface.dismiss(); // Hide dialog
                }).create();
        alertDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}