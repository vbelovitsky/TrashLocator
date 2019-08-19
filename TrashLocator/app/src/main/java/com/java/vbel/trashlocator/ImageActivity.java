package com.java.vbel.trashlocator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.ml.vision.FirebaseVision;
//import com.google.firebase.ml.vision.common.FirebaseVisionImage;
//import com.google.firebase.ml.vision.text.FirebaseVisionText;
//import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;


public class ImageActivity extends AppCompatActivity {

    private ImageView image;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        image = findViewById(R.id.mainImage);

        currentPhotoPath = getIntent().getStringExtra("imageURI");

        final Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

        Bitmap rotatedBitmap =  rotateImage(bitmap);
        image.setImageBitmap(rotatedBitmap);

        ImageButton leftButton = findViewById(R.id.imageButtonLeft);
        ImageButton checkButton = findViewById(R.id.imageButtonCheck);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//
//        checkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getMLTextFromImage(bitmap);
//            }
//        });

    }

    private Bitmap rotateImage(Bitmap bitmap){
        ExifInterface exifInterface = null;
        try{
            exifInterface = new ExifInterface(currentPhotoPath);
        } catch (IOException e){
            e.printStackTrace();
        }
        String orientString = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

//    public void getMLTextFromImage(Bitmap bitmap) {
//        final String ERROR_MESSAGE = "No text found((";
//        String HARD_ERROR_MESSAGE = "Error with Samsung storage, my bad((";
//
//        if (bitmap != null) {
//            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
//            FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
//                    .getOnDeviceTextRecognizer();
//
//            textRecognizer.processImage(image)
//                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
//                        @Override
//                        public void onSuccess(FirebaseVisionText result) {
//                            String recognizedText = result.getText();
//                            if (recognizedText.equals("")) recognizedText = ERROR_MESSAGE;
//                            intentToTextActivity(recognizedText);
//                        }
//                    })
//                    .addOnFailureListener(
//                            new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    intentToTextActivity(ERROR_MESSAGE);
//                                }
//                            });
//        } else {
//            intentToTextActivity(HARD_ERROR_MESSAGE);
//        }
//    }
//
//
//    private void intentToTextActivity(String recognizedText) {
//        Intent textActivityIntent = new Intent(ImageActivity.this, TextActivity.class);
//        textActivityIntent.putExtra("recognizedText", recognizedText);
//        startActivity(textActivityIntent);
//    }

}
