package com.barmej.hearme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.barmej.hearme.adapter.PhotoSoundAdapter;
import com.barmej.hearme.data.PhotoSound;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<PhotoSound> mItems;
    private PhotoSoundAdapter mAdapter;
    Menu mMenu;
    private RecyclerView.LayoutManager mGridLayoutManager, mListLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mItems = new ArrayList<>();
        mAdapter = new PhotoSoundAdapter(mItems);

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
}