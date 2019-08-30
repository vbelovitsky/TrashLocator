package com.java.vbel.trashlocator.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.java.vbel.trashlocator.R;
import com.java.vbel.trashlocator.dto.PointMarker;
import com.java.vbel.trashlocator.network.NetworkService;
import com.java.vbel.trashlocator.temporary.Kmeans;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    double[][] latLngArray;

    private String BASE_TEST_URL = "http://192.168.1.56:8080";

    //vars and widgets
    private GoogleMap mMap;
    private Marker mMarker;
    private ImageView mGps;

    private EditText editNumClusters;
    private Button kMeansButton;
    private TextView pointCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        editNumClusters = findViewById(R.id.editNumClusters);
        kMeansButton = findViewById(R.id.kMeansButton);
        pointCountText = findViewById(R.id.pointCount);


        editNumClusters.addTextChangedListener(editTextWatcher);


        kMeansButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int numClusters = Integer.parseInt(editNumClusters.getText().toString().trim());
                    if (latLngArray != null) {
                        //Запускаем алгоритм
                        if (latLngArray.length < numClusters)
                            Toast.makeText(AdminActivity.this, "Число больше, чем количество отметок", Toast.LENGTH_SHORT).show();
                        else {
                            kMeansRun(numClusters);
                        }
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(AdminActivity.this, "Неправильный ввод!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initMap();
    }

    private void kMeansRun(int numClusters){
        Kmeans kmeans = new Kmeans(latLngArray, numClusters);
        double[][] tempArray = kmeans.getCentersArray();
        if (tempArray != null) {
            List<PointMarker> greenMarkers = new ArrayList<>();
            for (int i = 0; i < tempArray.length; i++) {
                PointMarker pointMarker = new PointMarker();
                pointMarker.setId(1000000000);
                pointMarker.setCompleted(false);
                pointMarker.setLat(tempArray[i][0]);
                pointMarker.setLng(tempArray[i][1]);

                greenMarkers.add(pointMarker);

                System.out.println("WOOOOW " + tempArray[i][0] + " " + tempArray[i][1]);
            }
            setGreenMarkers(greenMarkers);
            kMeansButton.setEnabled(false);
        }else{
            Toast.makeText(AdminActivity.this, "Ошибка алгоритма", Toast.LENGTH_SHORT).show();
        }
    }

//    private void kMeansRun(final int numClusters){
//        Thread asyncThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final Kmeans kmeans = new Kmeans(latLngArray, numClusters);
//                AdminActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        double[][] tempArray = kmeans.getCentersArray();
//                        if (tempArray!=null){
//                            List<PointMarker> greenMarkers = new  ArrayList<>();
//                            for (int i = 0; i < tempArray.length; i++){
//                                PointMarker pointMarker = new PointMarker();
//                                pointMarker.setId(1000000000);
//                                pointMarker.setCompleted(false);
//                                pointMarker.setLat(tempArray[i][0]);
//                                pointMarker.setLng(tempArray[i][1]);
//                            }
//                            setMarkers(greenMarkers);
//                        }
//                    }
//                });
//            }
//        });
//        asyncThread.start();
//    }


    private TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String tempText = editNumClusters.getText().toString().trim();
            kMeansButton.setEnabled(!tempText.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    //region Map
    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        //getPoints();  Actually I need this.
        generatePoints();
    }

    private void getPoints() {
        NetworkService.getInstance(BASE_TEST_URL)
                .getTestApi()
                .getAllPoints()
                .enqueue(new Callback<List<PointMarker>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<PointMarker>> call, @NonNull Response<List<PointMarker>> response) {

                        setMarkers(response.body());
                        //latLngArray = ...

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<PointMarker>> call, @NonNull Throwable t) {
                        if (t.getClass() == UnknownHostException.class)
                            Toast.makeText(AdminActivity.this, "Check Internet connection!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(AdminActivity.this, "Error occurred while getting request!", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
    }

    private void generatePoints() {
        int generateLength = 100;
        List<PointMarker> generatedPointArray = new ArrayList<>();
        double[][] latLngTempArray = new double[generateLength][2];

        for (int i = 0; i < generateLength; i++) {
            double lat = 55 + Math.random();
            double lng = 37 + Math.random();

            latLngTempArray[i][0] = lat;
            latLngTempArray[i][1] = lng;

            PointMarker pointMarker = new PointMarker();
            pointMarker.setId(i);
            pointMarker.setCompleted(false);
            pointMarker.setLat(lat);
            pointMarker.setLng(lng);
            generatedPointArray.add(pointMarker);
        }

        pointCountText.setText(String.valueOf(generateLength));
        //Set markers on Map and initialize lat/lng array for kMeans
        setMarkers(generatedPointArray);
        latLngArray = latLngTempArray;
    }

    private void setMarkers(List<PointMarker> points) {
        for (PointMarker point : points) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(point.getLat(), point.getLng())));
            marker.setTag(point.getId());
        }
        //mMap.setOnMarkerClickListener(this);
    }

    private void setGreenMarkers(List<PointMarker> points) {
        for (PointMarker point : points) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(point.getLat(), point.getLng()))
                    //.icon(bitmapDescriptorFromVector(AdminActivity.this, R.drawable.green_marker_icon))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            );
            marker.setTag(point.getId());
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    //endregion

}
