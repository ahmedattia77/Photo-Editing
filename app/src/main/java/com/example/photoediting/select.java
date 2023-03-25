package com.example.photoediting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class select extends AppCompatActivity {

    public static final String URI_PHOTO = "uri";
    private FloatingActionButton camera , gallery;
    private Uri photoPicked;
    private ViewPager2 viewPager2;
    private ArrayList<SliderItem> sliderItemArrayList;
    private Handler slierHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

         camera = findViewById(R.id.choose_camera_fb);
         gallery = findViewById(R.id.choose_gallery_fb);
         viewPager2 = findViewById(R.id.choose_viewPager_vp);

         requestPermissions();

         sliderItemArrayList = new ArrayList<>();
         sliderItemArrayList.add(new SliderItem(R.drawable.one));
         sliderItemArrayList.add(new SliderItem(R.drawable.tow));
         sliderItemArrayList.add(new SliderItem(R.drawable.three));
         sliderItemArrayList.add(new SliderItem(R.drawable.four));

         viewPager2.setAdapter(new SliderAdapter(sliderItemArrayList , viewPager2));
         viewPager2.setClipToPadding(false);
         viewPager2.setClipChildren(false);
         viewPager2.setOffscreenPageLimit(3);
         viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(60));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float v = Math.abs(position);
                page.setScaleY(0.85f + v * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        slierHandler = new Handler();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slierHandler.removeCallbacks(runnable);
                slierHandler.postDelayed(runnable , 2500);
            }
        });



        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openGallery();
            }
        });


    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    private void openCamera(){

        ImagePicker.Companion.with(this)
                .crop()
                .cameraOnly()
                .start();
    }

    private void openGallery(){
        ImagePicker.Companion.with(this)
                .crop()
                .galleryOnly()
                .start();
    }

    private void requestPermissions() {

        Dexter.withActivity(this)

                .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,

                        android.Manifest.permission.READ_EXTERNAL_STORAGE)

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
//                            Toast.makeText(select.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).withErrorListener(error -> {
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                .onSameThread().check();
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(select.this);

        builder.setTitle("Need Permissions");

        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {

            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            assert data != null;

            photoPicked = data.getData();
            Intent intent = new Intent(getApplicationContext(), Editing.class);
            intent.putExtra(URI_PHOTO, photoPicked.toString());
            startActivity(intent);
        }

    }
}