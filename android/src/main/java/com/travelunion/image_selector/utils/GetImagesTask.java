package com.travelunion.image_selector.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class GetImagesTask extends AsyncTask<Void, Void, ArrayList<GalleryImage>> {
    private ContentResolver imageResolver;
    public AsyncResponse delegate = null;

    GetImagesTask(ContentResolver imageResolver) {
        this.imageResolver = imageResolver;
    }

    @Override
    protected void onPostExecute(ArrayList<GalleryImage> result) {
        delegate.processFinish(result);
    }

    @Override
    protected ArrayList<GalleryImage> doInBackground(Void... voids) {
        try {
            ArrayList<GalleryImage> imagesArray = new ArrayList<>();
            Uri imageUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Cursor imageCursor =
                    imageResolver.query(imageUri, null, null, null,
                            android.provider.MediaStore.Images.Media.DATE_TAKEN + " DESC");
            if (imageCursor != null && imageCursor.moveToFirst()) {
                int displayNameColumn =
                        imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DISPLAY_NAME);
                int dataColumn =
                        imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATA);
                int sizeColumn =
                        imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.SIZE);
                int dateColumn =
                        imageCursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATE_TAKEN);
                do {
                    GalleryImage image = new GalleryImage();
                    image.setName(imageCursor.getString(displayNameColumn));
                    image.setPath(imageCursor.getString(dataColumn));
                    image.setDateTaken(imageCursor.getString(dateColumn));
                    image.setSize(imageCursor.getInt(sizeColumn));
                    imagesArray.add(image);
                } while (imageCursor.moveToNext());
            }

            try {
                imageCursor.close();
            } catch (Exception e) {}

            return imagesArray;
        } catch (Exception e) {
            Log.d("gallery_loader", e.toString());
        }
        return null;
    }
}
