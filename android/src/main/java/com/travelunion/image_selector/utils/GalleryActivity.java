package com.travelunion.image_selector.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.travelunion.image_selector.R;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements AsyncResponse, ImageClickListener {
    RecyclerView folderRecycler;
    TextView empty;
    Button confirmButton;
    Button cancelButton;
    ArrayList<GalleryImage> selectedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);

        empty = findViewById(R.id.empty);

        folderRecycler = findViewById(R.id.folderRecycler);
        folderRecycler.addItemDecoration(new MarginDecoration(this));
        folderRecycler.hasFixedSize();

        confirmButton = findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putParcelableArrayListExtra("images", selectedImages);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        selectedImages = new ArrayList<>();

        boolean permissionGranted;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                permissionGranted = true;
            } else {
                permissionGranted = extras.getBoolean("permissionGranted");
            }
        } else {
            permissionGranted = (boolean) savedInstanceState.getSerializable("permissionGranted");
        }

        if(!permissionGranted) {
            empty.setVisibility(View.VISIBLE);
            empty.setText(R.string.gallery_permission_denied);

            return;
        } else {
            empty.setVisibility(View.INVISIBLE);
            empty.setText(R.string.gallery_empty);
        }

        getImages();
    }

    public void getImages() {
        GetImagesTask task = new GetImagesTask(getContentResolver());
        task.delegate = this;
        task.execute();
    }

    @Override
    public void processFinish(ArrayList<GalleryImage> output) {
        if(output == null || output.isEmpty()){
            empty.setVisibility(View.VISIBLE);
        } else {
            RecyclerView.Adapter folderAdapter = new ImageAdapter(output, this, this);
            folderRecycler.setAdapter(folderAdapter);
        }
    }

    @Override
    public void onPicClicked(ImageAdapter.ImageHolder holder, int position, ArrayList<GalleryImage> pics) {
        if(selectedImages.contains(pics.get(position))) {
            selectedImages.remove(pics.get(position));
        } else {
            selectedImages.add(pics.get(position));
        }
    }
}
