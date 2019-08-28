package com.java.vbel.trashlocator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.java.vbel.trashlocator.dto.PointSend;
import com.java.vbel.trashlocator.network.NetworkService;
import com.java.vbel.trashlocator.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    private double[] coordinates;
    private ArrayList<String> labelsArray;
    private String currentPhotoPath;

    private long categoryId;
    private String categoryTitle;

    private String strUser;
    private String strDate;
    private String strCoordinates;
    private String strCategory;
    private String strLabels;

    private String BASE_TEST_URL = "https://server-trash-optimizator.herokuapp.com/";

    private ConstraintLayout resultLayout;
    private TextView resultText;
    private ImageButton resultButton;
    private CheckBox checkBox;

    private boolean COMPLETED = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //get data from intent
        coordinates = getIntent().getDoubleArrayExtra("coordinates");
        labelsArray = getIntent().getStringArrayListExtra("labels");
        categoryId = getIntent().getLongExtra("categoryId", 0);
        categoryTitle = getIntent().getStringExtra("categoryTitle");
        currentPhotoPath = getIntent().getStringExtra("imageURI");

        prepareStringData(coordinates, labelsArray);

        resultLayout = findViewById(R.id.resultLayout);

        resultText = findViewById(R.id.requestMessageText);

        TextView userText = findViewById(R.id.userText);
        userText.append(strUser);

        TextView dateText = findViewById(R.id.dateText);
        dateText.append(strDate);

        TextView coordinatesText = findViewById(R.id.coordinatesText);
        coordinatesText.append(strCoordinates);
        coordinatesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCoordinatesToClipBoard();
            }
        });

        TextView categoryText = findViewById(R.id.categoryText);
        categoryText.append(strCategory);

        TextView labelsText = findViewById(R.id.labelsText);
        labelsText.append("\n" + strLabels);

        checkBox = findViewById(R.id.checkboxForImage);

        resultButton = findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPoint();
            }
        });
    }

    private void prepareStringData(double[] coordinates, ArrayList<String> labelsArray){

        strUser = String.valueOf(0); //actually I need User id here

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        strDate =  formatter.format(date);

        strCoordinates = "\n" + coordinates[0]+ ", " + coordinates[1];

        strCategory = categoryTitle; //actually I need category here

        if (labelsArray != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < labelsArray.size(); i++) {
                stringBuilder.append(labelsArray.get(i));
                if (i != labelsArray.size() - 1) stringBuilder.append(", ");
            }
            strLabels = stringBuilder.toString();
        }else strLabels = "Nothing found";
    }

    private void copyCoordinatesToClipBoard(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String mainText = strCoordinates.substring(1);
        String label = "TrashLocator";
        ClipData clip = ClipData.newPlainText(label, mainText);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(ResultActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void sendPoint(){

        PointSend newPoint = new PointSend();

        newPoint.setUserId(1);
        newPoint.setDate(strDate);
        newPoint.setLat(coordinates[0]);
        newPoint.setLng(coordinates[1]);
        newPoint.setCategoryId(categoryId);

        String stringImage = "";
        //Из фото (битмапа) в стринг base64
        if(checkBox.isChecked()){
            stringImage = imageToString(currentPhotoPath);
            System.out.println(stringImage);
        }
        newPoint.setImage(stringImage);

        NetworkService.getInstance(BASE_TEST_URL)
                .getTestApi()
                .postPoint(newPoint)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                        COMPLETED = true;
                        resultText.setText("Ваш запрос отправлен");
                        resultLayout.setBackgroundColor(getResources().getColor(R.color.colorLightGreen));

                        //Обновление MainActivity при нажатии круглой кнопки
                        resultButton.setImageDrawable(getDrawable(R.drawable.out));
                        resultButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });

                    }
                    @Override
                    public void onFailure(@NonNull Call<Void> call,@NonNull Throwable t) {
                        resultText.setText("Ошибка отправки");
                        resultLayout.setBackgroundColor(getResources().getColor(R.color.colorLightRed));
                        t.printStackTrace();
                    }
                });
    }

    private String imageToString(String filePath){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed(){
        if(COMPLETED){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else super.onBackPressed();
    }


}
