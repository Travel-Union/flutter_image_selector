package com.travelunion.image_selector.utils;

import java.util.ArrayList;

public interface ImageClickListener {
    void onPicClicked(ImageAdapter.ImageHolder holder, int position, ArrayList<GalleryImage> pics);
}
