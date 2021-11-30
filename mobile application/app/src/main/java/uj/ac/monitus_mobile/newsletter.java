package uj.ac.monitus_mobile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uj.ac.monitus_mobile.data.model.NewsletterModel;
import uj.ac.monitus_mobile.retrofit.MonitUsServiceGenerator;
import uj.ac.monitus_mobile.retrofit.RetrofitInterface;
import uj.ac.monitus_mobile.ui.newsletters.NewsletterAdapter;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class newsletter extends AppCompatActivity {
    //create new retrofit service
    RetrofitInterface service = MonitUsServiceGenerator.createService(RetrofitInterface.class);

    //Recycler view instance
    private RecyclerView newsletterRV;

    //newsletterAdapter instance
    private NewsletterAdapter newsletterAdapter;

    //array of newsletter headlines
    List<NewsletterModel> newsletterArray;

    private void failedFetchRequest(){
        Toast.makeText(getApplicationContext(),"Sorry,something went wrong", Toast.LENGTH_LONG);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsletter);
        newsletterRV = findViewById(R.id.recyclerview_newsletters);

        //initialize newsletter adapter and add data
        newsletterAdapter = new NewsletterAdapter(this,newsletterArray);

        /*layoutManager for recyclerview
          setting the orientation of cards to be vertical
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        //set layoutmanager and adapter to recycler
        newsletterRV.setLayoutManager(linearLayoutManager);
        newsletterRV.setAdapter(newsletterAdapter);

        //create call class to get newsletters
        Call<List<NewsletterModel>> newslettersCall = service.listNewsletters();

        //execute HTTP request
        newslettersCall.enqueue(new Callback<List<NewsletterModel>>() {
            @Override
            public void onResponse(Call<List<NewsletterModel>> call, Response<List<NewsletterModel>> response) {
                if(response.code() == 200){
                    newsletterArray = new ArrayList<NewsletterModel>();
                    for(NewsletterModel newsletter : response.body()){
                        newsletterArray.add(newsletter);
                    }
                    displayNewsletters();
                }
            }

            @Override
            public void onFailure(Call<List<NewsletterModel>> call, Throwable t) {
                failedFetchRequest();
            }
        });
    }

    private void displayNewsletters() {
        if(newsletterArray.size() == 0){setContentView(R.layout.error_not_found);}
        else{
            //set layoutmanager and adapter to recycler
            newsletterAdapter = new NewsletterAdapter(this,newsletterArray);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            newsletterRV.setLayoutManager(linearLayoutManager);
            newsletterRV.setAdapter(newsletterAdapter);
        }
    }
}