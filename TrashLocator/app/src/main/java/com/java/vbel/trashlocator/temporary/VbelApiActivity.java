//package com.java.vbel.trashlocator.activities;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.java.vbel.trashlocator.R;
//import com.java.vbel.trashlocator.models.Message;
//import com.java.vbel.trashlocator.models.Paper;
//import com.java.vbel.trashlocator.models.Post;
//import com.java.vbel.trashlocator.network.NetworkService;
//
//import java.net.UnknownHostException;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.http.POST;
//
//public class VbelApiActivity extends AppCompatActivity {
//
//    private String BASE_VBEL_URL = "https://vbelserver.herokuapp.com/api/";
//    private String BASE_TEST_URL = "https://server-trash-optimizator.herokuapp.com/";
//
//    private TextView testText;
//    private EditText editPk;
//
//    private EditText editDeletePk;
//
//    private EditText editUpdatePk;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_vbel_api);
//
//        testText = findViewById(R.id.paperTitle);
//        editPk = findViewById(R.id.editPk);
//        final Button getButton = findViewById(R.id.getButton);
//
//        getButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendPaper();
//            }
//        });
//
//        editDeletePk = findViewById(R.id.editDeletePk);
//        final Button deleteButton = findViewById(R.id.deleteButton);
//
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deletePaper();
//            }
//        });
//
//        editUpdatePk = findViewById(R.id.editUpdatePk);
//        Button updateButton = findViewById(R.id.updateButton);
//
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updatePaper();
//            }
//        });
//
//    }
//
//    private void getPaper(){
//        NetworkService.getInstance(BASE_VBEL_URL)
//                .getVbelApi()
//                .getPaper(1)
//                .enqueue(new Callback<Paper>() {
//                    @Override
//                    public void onResponse(@NonNull Call<Paper> call, @NonNull Response<Paper> response) {
//                        Paper paper = response.body();
//                        testText.append(paper.getTitle());
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<Paper> call, @NonNull Throwable t) {
//
//                        if(t.getClass() == UnknownHostException.class) testText.append("Check Internet connection!");
//                        else testText.append("Error occurred while getting request!");
//                        t.printStackTrace();
//                    }
//                });
//    }
//
//    private void getPapers(){
//        NetworkService.getInstance(BASE_VBEL_URL)
//                .getVbelApi()
//                .getAllPapers()
//                .enqueue(new Callback<List<Paper>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<List<Paper>> call,@NonNull Response<List<Paper>> response) {
//                        List<Paper> papers = response.body();
//                        StringBuilder stringBuilder = new StringBuilder();
//                        for (Paper paper: papers){
//                            stringBuilder.append(paper.getTitle()).append(" ");
//                        }
//                        testText.append(stringBuilder.toString());
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<List<Paper>> call, @NonNull Throwable t) {
//                        if(t.getClass() == UnknownHostException.class) testText.append("Check Internet connection!");
//                        else testText.append("Error occurred while getting request!");
//                        t.printStackTrace();
//                    }
//                });
//    }
//
//    private void deletePaper(){
//        NetworkService.getInstance(BASE_VBEL_URL)
//                .getVbelApi()
//                .deletePaper(1)
//                .enqueue(new Callback<Message>() {
//                    @Override
//                    public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<Message> call,@NonNull Throwable t) {
//
//                    }
//                });
//    }
//
//    private void updatePaper(){
//        Paper updatedPaper = new Paper();
//        updatedPaper.setTitle("Megalul");
//        NetworkService.getInstance(BASE_VBEL_URL)
//                .getVbelApi()
//                .updatePaper(2, updatedPaper)
//                .enqueue(new Callback<Message>() {
//                    @Override
//                    public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
//                        testText.setText(response.body().getMessage());
//                    }
//                    @Override
//                    public void onFailure(@NonNull Call<Message> call,@NonNull Throwable t) {
//
//                    }
//                });
//    }
//
//    private void getTestPaper(){
//        NetworkService.getInstance(BASE_TEST_URL)
//                .getTestApi()
//                .getPaper()
//                .enqueue(new Callback<Paper>() {
//                    @Override
//                    public void onResponse(@NonNull Call<Paper> call, @NonNull Response<Paper> response) {
//                        Paper paper = response.body();
//                        testText.append(paper.getTitle());
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<Paper> call, @NonNull Throwable t) {
//
//                        if(t.getClass() == UnknownHostException.class) testText.append("Check Internet connection!");
//                        else testText.append("Error occurred while getting request!");
//                        t.printStackTrace();
//                    }
//                });
//    }
//
//    private void sendPaper(){
//        Paper newPaper = new Paper();
//        newPaper.setTitle("Neeeeew");
//        NetworkService.getInstance(BASE_VBEL_URL)
//                .getVbelApi()
//                .createPaper(newPaper)
//                .enqueue(new Callback<Message>() {
//                    @Override
//                    public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
//                        testText.setText(response.body().getMessage());
//                    }
//                    @Override
//                    public void onFailure(@NonNull Call<Message> call,@NonNull Throwable t) {
//
//                    }
//                });
//    }
//
//
//}
