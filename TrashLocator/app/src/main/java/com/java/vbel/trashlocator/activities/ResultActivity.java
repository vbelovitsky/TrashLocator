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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.java.vbel.trashlocator.adapters.CategoryAdapter;
import com.java.vbel.trashlocator.dto.CategoryItem;
import com.java.vbel.trashlocator.dto.PointSend;
import com.java.vbel.trashlocator.fragments.CategoryFragment;
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

public class ResultActivity extends AppCompatActivity implements CategoryAdapter.GetCategoryFromDialog {

    private double[] coordinates;
    private ArrayList<String> labelsArray;
    private String currentPhotoPath;

    private long categoryId;
    private String categoryTitle;

    private String strUser;
    private String strDate;
    private String strCoordinates;
    private String strLabels;

    private String BASE_TEST_URL = "http://192.168.1.56:8080";

    private ConstraintLayout resultLayout;
    private TextView resultText;
    private ImageButton resultButton;
    private CheckBox checkBox;
    private TextView categoryText;

    private boolean COMPLETED = false;

    //Category fragment
    private CategoryFragment categoryFragment;
    private ArrayList<CategoryItem> categoryItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Подготовка данных для диалога с категориями
        categoryFragment = new CategoryFragment();
        prepareDefaultCategories();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("categoryItems", categoryItems);
        categoryFragment.setArguments(bundle);

        //get data from intent
        coordinates = getIntent().getDoubleArrayExtra("coordinates");
        labelsArray = getIntent().getStringArrayListExtra("labels");
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

        categoryText = findViewById(R.id.categoryText);
        categoryText.append(categoryTitle);
        Button categoryButton = findViewById(R.id.changeCategoryButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryFragment.show(getSupportFragmentManager(), "category");
            }
        });

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

    private void prepareDefaultCategories(){
        long[] categoryIds = {1, 2, 3};
        String[] categoryTitles = {"Мелкий мусор", "Куча мусора", "Свалка", };
        String[] categoryDescriptions = {"Немного мусора вне урны", "Большое количество скопившегося мусора", "Огромная куча бытовых или строительных отходов"};
        long[] categoryImages = {R.drawable.trash_small, R.drawable.trash_mid, R.drawable.trash_big};

        //Инициализируем дефолтные значения
        categoryId = 1;
        categoryTitle = categoryTitles[0];

        //Заполняем массив категорий
        for(int i = 0; i < categoryIds.length; i++){
            CategoryItem categoryItem = new CategoryItem();
            categoryItem.setId(categoryIds[i]);
            categoryItem.setTitle(categoryTitles[i]);
            categoryItem.setDescription(categoryDescriptions[i]);
            categoryItem.setImage(categoryImages[i]);
            categoryItems.add(categoryItem);
        }
    }

    @Override
    public void getCategoryFromDialog(Long id, String title) {
        Toast.makeText(ResultActivity.this, "Категория:  "+title, Toast.LENGTH_SHORT).show();
        categoryId = id;
        categoryTitle = title;
        categoryText.setText("Категория:\n" + categoryTitle);
    }

    //region useless category request
//    private void prepareCategoryData(){
//        NetworkService.getInstance(BASE_TEST_URL)
//                .getTestApi()
//                .getAllCategories()
//                .enqueue(new Callback<List<CategoryItem>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<List<CategoryItem>> call, @NonNull Response<List<CategoryItem>> response) {
//
//                        //Костыльно, нужен рефакторинг
//                        try {
//                            long[] categoryImages = {R.drawable.trash_small, R.drawable.trash_mid, R.drawable.trash_big};
//                            for (int i = 0; i < response.body().size(); i++) {
//                                CategoryItem categoryItem = response.body().get(i);
//                                try {
//                                    categoryItem.setImage(categoryImages[i]);
//                                } catch (IndexOutOfBoundsException e) {
//                                    categoryItem.setImage(R.drawable.trash_default);
//                                }
//
//                                categoryItems.add(categoryItem);
//                            }
//                        } catch (NullPointerException e){
//                            prepareDefaultCategories();
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<List<CategoryItem>> call, @NonNull Throwable t) {
//                        prepareDefaultCategories();
//                        if(t.getClass() == UnknownHostException.class)
//                            Toast.makeText(MainActivity.this, "Не удалось загрузить категории", Toast.LENGTH_SHORT).show();
//                        else
//                            Toast.makeText(MainActivity.this, "Ошибка поключения к серверу", Toast.LENGTH_SHORT).show();
//                        t.printStackTrace();
//                    }
//                });
//
//    }
    //endregion

}
