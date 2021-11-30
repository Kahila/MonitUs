package uj.ac.monitus_mobile;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uj.ac.monitus_mobile.data.model.StatsModel;
import uj.ac.monitus_mobile.data.model.SubjectModel;
import uj.ac.monitus_mobile.retrofit.MonitUsServiceGenerator;
import uj.ac.monitus_mobile.retrofit.RetrofitInterface;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import java.util.ArrayList;
import java.util.List;

public class statistics_template extends AppCompatActivity {
    /*constant keys for saving user preferences*/
    public static final String SHARED_PREFS = "shared_prefs";

    //key for storing name
    public static final String STUDENT_ID = "student_id_key";
    /* **************Key constants************** */

    //preferences
    SharedPreferences sharedPreferences;

    //student identification
    int student_id;

    TextView studentAVG;

    private RetrofitInterface service = MonitUsServiceGenerator.createService(RetrofitInterface.class);
    private SubjectModel subject;
    private List<StatsModel> statsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_template);
        sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        student_id = sharedPreferences.getInt(STUDENT_ID, 0);

        //student average
        studentAVG = findViewById(R.id.statistics_subjectaverage);

        //get selected subject
        if(getIntent().hasExtra("selected_subject")){
            subject = getIntent().getParcelableExtra("selected_subject");
            TextView subjectName = findViewById(R.id.statistics_subjectheadername);
            subjectName.setText(subject.getSubject_name());
        }

        Call<List<StatsModel>> statsCall = service.reportStats(subject.getSubject_id());

        statsCall.enqueue(new Callback<List<StatsModel>>() {
            @Override
            public void onResponse(Call<List<StatsModel>> call, Response<List<StatsModel>> response) {
                if(response.code() == 200){
                    statsList = new ArrayList<>();
                    statsList.addAll(response.body());
                    displayData();
                }else{
                    Toast.makeText(statistics_template.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<StatsModel>> call, Throwable t) {
                Toast.makeText(statistics_template.this,"Sorry something went wrong",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayData() {
        if(statsList.size() == 0){ displayEmptyData();}
        else {
            AnyChartView anyChartView = findViewById(R.id.any_chart_view);

            Cartesian cartesian = AnyChart.column();

            List<DataEntry> data = new ArrayList<>();
            int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0;

            for(StatsModel s : statsList){
                int percentage = (int) s.getPercentage();
                Double average = s.getPercentage();
                if(s.getStudent_id() == student_id) { studentAVG.setText(average.toString()); }

                if(isBetween(percentage, 0, 39)){
                    count1++;
                    continue;
                }else if(isBetween(percentage, 40, 49)){
                    count2++;
                    continue;
                }else if(isBetween(percentage, 50, 59)){
                    count3++;
                    continue;
                }else if(isBetween(percentage, 60, 69)){
                    count4++;
                    continue;
                }else if(isBetween(percentage, 70, 79)){
                    count5++;
                    continue;
                }else{
                    count6++;
                    continue;
                }
            }
            data.add(new ValueDataEntry("(0-39)", count1));
            data.add(new ValueDataEntry("(40-49)", count2));
            data.add(new ValueDataEntry("(50-59)", count3));
            data.add(new ValueDataEntry("(60-69)", count4));
            data.add(new ValueDataEntry("(70-79)", count5));
            data.add(new ValueDataEntry("(80-100)", count6));

            Column column = cartesian.column(data);
            column.color("SlateGray");

            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            cartesian.animation(true);
            cartesian.title("Average assessment mark distribution");

            cartesian.yScale().minimum(0d);

            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);

            cartesian.xAxis(0).title("Grade");
            cartesian.yAxis(0).title("Student frequency");

            anyChartView.setChart(cartesian);
        }
    }

    private void displayEmptyData() {
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("(0-39)", 0));
        data.add(new ValueDataEntry("(40-49)", 0));
        data.add(new ValueDataEntry("(50-59)", 0));
        data.add(new ValueDataEntry("(60-69)", 0));
        data.add(new ValueDataEntry("(70-79)", 0));
        data.add(new ValueDataEntry("(80-100)", 0));

        Column column = cartesian.column(data);
        column.color("SlateGray");

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Average assessment mark distribution");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Grade");
        cartesian.yAxis(0).title("Student frequency");

        anyChartView.setChart(cartesian);
    }

    private boolean isBetween(int x, int lower, int upper){
        return lower <= x && x <= upper;
    }
}