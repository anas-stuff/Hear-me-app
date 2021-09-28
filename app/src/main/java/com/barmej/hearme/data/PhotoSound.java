package com.barmej.hearme.data;

import android.net.Uri;

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
}
