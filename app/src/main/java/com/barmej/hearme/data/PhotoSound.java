package com.barmej.hearme.data;

import android.net.Uri;

import com.barmej.hearme.MainActivity;
import com.barmej.hearme.R;

import java.util.ArrayList;

public class PhotoSound {
    private Uri image, sound;
    
    public PhotoSound(Uri image, Uri sound){
        this.image = image;
        this.sound = sound;
    }

    // Getters
    public Uri getImage(){
        return image;
    }
    public Uri getSound(){
        return sound;
    }


    public static ArrayList<PhotoSound> getDefaultList() {
        ArrayList<PhotoSound> defaultList = new ArrayList<>();

        int[][] resources = {
                {R.drawable.pray, R.raw.pray_audio},
                {R.drawable.drink, R.raw.drink_audio},
                {R.drawable.eat, R.raw.eat_audio},
                {R.drawable.sleep, R.raw.sleep_audio},
                {R.drawable.bathroom, R.raw.bathroom_audio}
        };

        for(int[] res : resources)
            defaultList.add(new PhotoSound(resourceToUri(res[0]), resourceToUri(res[1])));

        return defaultList;
    }

    private static Uri resourceToUri(int res){
        return Uri.parse("android.resource://com.barmej.hearme/" + res);
    }
}
