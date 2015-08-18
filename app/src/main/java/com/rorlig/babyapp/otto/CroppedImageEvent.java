package com.rorlig.babyapp.otto;

/**
 * @author gaurav gupta
 */
public class CroppedImageEvent {

    private final String imageUri;

    public CroppedImageEvent(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }
}
