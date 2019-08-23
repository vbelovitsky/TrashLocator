//package com.java.vbel.trashlocator.activities;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//
//import androidx.annotation.NonNull;
//import androidx.exifinterface.media.ExifInterface;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.ml.vision.FirebaseVision;
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
//import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
//import com.java.vbel.trashlocator.R;
//
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class ImageActivity extends AppCompatActivity {
//
//    private ImageView image;
//    private String currentPhotoPath;
//    private double[] coordinates;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image);
//
//        FirebaseApp.initializeApp(this);
//        FirebaseApp.getInstance();
//
//        image = findViewById(R.id.mainImage);
//        currentPhotoPath = getIntent().getStringExtra("imageURI");
//        final Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
//        final Bitmap rotatedBitmap =  rotateImage(bitmap);
//        image.setImageBitmap(rotatedBitmap);
//
//
//        coordinates = getIntent().getDoubleArrayExtra("coordinates");
//
//
//        ImageButton leftButton = findViewById(R.id.imageButtonLeft);
//        ImageButton checkButton = findViewById(R.id.imageButtonCheck);
//
//        leftButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        checkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getMLLabelsFromImage(rotatedBitmap);
//            }
//        });
//    }
//
//    //region useless rotation
//    private Bitmap rotateImage(Bitmap bitmap){
//        ExifInterface exifInterface = null;
//        try{
//            exifInterface = new ExifInterface(currentPhotoPath);
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        String orientString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
//        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;
//        Matrix matrix = new Matrix();
//        switch (orientation){
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                matrix.setRotate(90);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                matrix.setRotate(180);
//                break;
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                matrix.setRotate(270);
//                break;
//            default:
//        }
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }
//    //endregion
//
//    public void getMLLabelsFromImage(Bitmap bitmap) {
//        final String ERROR_MESSAGE = "No label found((";
//        final String HARD_ERROR_MESSAGE = "Error with Samsung storage, my bad((";
//
//        if (bitmap != null) {
//            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
//            FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
//            labeler.processImage(image)
//                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
//                        @Override
//                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
//
//                            StringBuilder stringBuilder = new StringBuilder();
//                            ArrayList<String> labelArray = new ArrayList<>();
//                            for (FirebaseVisionImageLabel label: labels) {
//                                String text = label.getText();
//                                String entityId = label.getEntityId();
//                                float confidence = label.getConfidence();
//
//                                stringBuilder.append(text).append(" ");
//                                labelArray.add(text);
//                            }
//                            showMessage(stringBuilder.toString(), labelArray);
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            showMessage(ERROR_MESSAGE);
//                        }
//                    });
//        } else {
//            showMessage(HARD_ERROR_MESSAGE);
//        }
//    }
//
//    private void showMessage(String data){
//        Toast.makeText(ImageActivity.this, data, Toast.LENGTH_LONG).show();
//        Intent resultActivityIntent = new Intent(ImageActivity.this, ResultActivity.class);
//        resultActivityIntent.putExtra("coordinates", coordinates);
//        startActivity(resultActivityIntent);
//    }
//
//    private void showMessage(String data, ArrayList<String> labelArray){
//        Toast.makeText(ImageActivity.this, data, Toast.LENGTH_LONG).show();
//        Intent resultActivityIntent = new Intent(ImageActivity.this, ResultActivity.class);
//        resultActivityIntent.putExtra("coordinates", coordinates);
//        resultActivityIntent.putExtra("labels", labelArray);
//        startActivity(resultActivityIntent);
//    }
//
//}
