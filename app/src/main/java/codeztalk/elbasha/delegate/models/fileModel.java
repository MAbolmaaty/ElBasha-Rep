package codeztalk.elbasha.delegate.models;

import android.net.Uri;


public class fileModel {

    private String fileType;
    private String imagePath;
    private Uri imageUri;

    public fileModel() {

    }

    public fileModel(String fileType, String imagePath) {
        this.fileType = fileType;
        this.imagePath = imagePath;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
