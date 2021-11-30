package uj.ac.monitus_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uj.ac.monitus_mobile.data.model.ReportModel;
import uj.ac.monitus_mobile.retrofit.MonitUsServiceGenerator;
import uj.ac.monitus_mobile.retrofit.RetrofitInterface;
import uj.ac.monitus_mobile.ui.reportcard.ReportCardAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.anychart.core.Text;
import com.anychart.core.annotations.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class report_card extends AppCompatActivity implements ReportCardAdapter.OnReportListener{
    /*constant keys for saving user preferences*/
    public static final String SHARED_PREFS = "shared_prefs";

    //key for storing name
    public static final String NAME_KEY = "name_key";

    //key for storing surname
    public static final String SURNAME_KEY = "surname_key";

    //key for storing student id
    public static  final String STUDENT_ID = "student_id_key";
    /* ********** end key constants*********** */

    //Variable for sharedPreferences
    SharedPreferences sharedPreferences;

    //student preferences
    private String name, surname;
    private int student_id;

    private RetrofitInterface service = MonitUsServiceGenerator.createService(RetrofitInterface.class);
    private List<ReportModel> reportModelList;

    //reportcard adapter instance
    private ReportCardAdapter reportCardAdapter;

    //reportcard recyclerview
    private RecyclerView reportCardRV;

    //textviews
    TextView bestTV, worstTV, averageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_card);
        reportCardRV = findViewById(R.id.reportcard_recyclerview);
        /*reportCardAdapter = new ReportCardAdapter(reportModelList,this,this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        reportCardRV.setLayoutManager(linearLayoutManager);
        reportCardRV.setAdapter(reportCardAdapter);*/

        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        student_id = sharedPreferences.getInt(STUDENT_ID,0);

        TextView nameTV = findViewById(R.id.report_card_studentname);
        averageTV = findViewById(R.id.report_card_average);

        nameTV.setText(sharedPreferences.getString(NAME_KEY,null)+" "+sharedPreferences.getString(SURNAME_KEY,null));

        Call<List<ReportModel>> getReportCardCall = service.getreportCard(student_id);

        getReportCardCall.enqueue(new Callback<List<ReportModel>>() {
            @Override
            public void onResponse(Call<List<ReportModel>> call, Response<List<ReportModel>> response) {
                if(response.code() == 200){
                    reportModelList = new ArrayList<>();
                    reportModelList.addAll(response.body());
                    displayReportCard();
                }else{
                    setContentView(R.layout.error);
                }
            }

            @Override
            public void onFailure(Call<List<ReportModel>> call, Throwable t) {
                setContentView(R.layout.error_failure);
            }
        });
    }

    private void displayReportCard() {
        averageTV.setText("0");
        if(reportModelList.size() != 0){
            int total = 0;
            for(ReportModel r : reportModelList){
                total += r.getPercentage();
            }
            int studentAverage = (int) total/ reportModelList.size();
            averageTV.setText("Average: "+String.valueOf(studentAverage));
            reportCardAdapter = new ReportCardAdapter(reportModelList,this,this, studentAverage);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            reportCardRV.setLayoutManager(linearLayoutManager);
            reportCardRV.setAdapter(reportCardAdapter);
        }
    }

    //navigate to marks for specific subject
    @Override
    public void onReportClick(int position) {
        Intent intent = new Intent(this,marks_template.class);
        intent.putExtra("selected_report",reportModelList.get(position));
        startActivity(intent);
    }
}