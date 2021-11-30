package uj.ac.monitus_mobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uj.ac.monitus_mobile.data.model.NewsletterModel;
import uj.ac.monitus_mobile.data.model.SubjectModel;
import uj.ac.monitus_mobile.retrofit.MonitUsServiceGenerator;
import uj.ac.monitus_mobile.retrofit.RetrofitInterface;
import uj.ac.monitus_mobile.ui.newsletters.NewsletterAdapter;
import uj.ac.monitus_mobile.ui.subjects.SubjectsAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static uj.ac.monitus_mobile.MainActivity.GRADE_KEY;

public class subjects extends AppCompatActivity implements SubjectsAdapter.OnSubjectListener {
    /*constant keys for saving user preferences*/
    public static final String SHARED_PREFS = "shared_prefs";

    //key for storing name
    public static final String STUDENT_ID = "student_id_key";
    /* **************Key constants************** */

    //SharedPreferences
    SharedPreferences sharedPreferences;

    //student grade subjects to fetch for
    private int student_id;

    //create retrofit interface
    RetrofitInterface service = MonitUsServiceGenerator.createService(RetrofitInterface.class);

    //RecyclerView instance
    private RecyclerView subjectRV;

    //get subject adapter
    SubjectsAdapter subjectAdapter;

    //array of subjects
    List<SubjectModel> subjectArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        student_id = sharedPreferences.getInt(STUDENT_ID,0);

        subjectRV = findViewById(R.id.recyclerview_subjects);

        //populate arraylist with subjects
        subjectArray = new ArrayList<>();

        //initialize subjects adapter and add data
        subjectAdapter = new SubjectsAdapter(subjectArray,this,this);

        /*layoutManager for recyclerview
          setting the orientation of cards to be vertical
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //set layoutmanager and adapter to recycler
        subjectRV.setLayoutManager(linearLayoutManager);
        subjectRV.setAdapter(subjectAdapter);

        /* ********** get data from service ************ */

        //create call class to get subjects by grade
        Call<List<SubjectModel>> subjectCall = service.listSubjects(student_id);

        //execute HTTP request
        subjectCall.enqueue(new Callback<List<SubjectModel>>() {
            @Override
            public void onResponse(Call<List<SubjectModel>> call, Response<List<SubjectModel>> response) {
                if(response.code() == 200){
                    subjectArray = new ArrayList<SubjectModel>();
                    for(SubjectModel subject : response.body()){
                        subject.setSubject_image(R.mipmap.ic_logo);
                        subjectArray.add(subject);
                    }
                    displaySubjects();
                }
            }

            @Override
            public void onFailure(Call<List<SubjectModel>> call, Throwable t) {
                Toast.makeText(subjects.this,t.getMessage(),Toast.LENGTH_LONG).show();
                failedFetchRequest();
            }
        });

    }

    private void failedFetchRequest() {
        Toast.makeText(getApplicationContext(),"Sorry,something went wrong", Toast.LENGTH_LONG);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void displaySubjects() {
        //set layoumanager and adapter to recycler with new data
        subjectAdapter = new SubjectsAdapter(subjectArray,this,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        subjectRV.setLayoutManager(linearLayoutManager);
        subjectRV.setAdapter(subjectAdapter);
    }

    //navigate to clicked subject activity
    @Override
    public void onSubjectClick(int position) {
        //get specific subject selected and redirect to activity

        //goto statistics activity
        if(getIntent().getStringExtra("uj.ac.monitus_mobile.statistics") != null){
            Intent intent = new Intent(this,statistics_template.class);
            intent.putExtra("selected_subject", subjectArray.get(position));
            startActivity(intent);
        }
        //goto submission activity
        else if(getIntent().getStringExtra("uj.ac.monitus_mobile.submit") != null){
            Intent intent = new Intent(this,assignment.class);
            intent.putExtra("selected_subject",subjectArray.get(position));
            startActivity(intent);
        }
        //goto marks activity
        else if(getIntent().getStringExtra("uj.ac.monitus_mobile.marks") != null){
            Intent intent = new Intent(this,marks_template.class);
            intent.putExtra("selected_subject",subjectArray.get(position));
            startActivity(intent);
        //redirect to default assignment class
        }else{
            Intent intent = new Intent(this,assignment.class);
            intent.putExtra("selected_subject",subjectArray.get(position));
            startActivity(intent);
        }
    }
}