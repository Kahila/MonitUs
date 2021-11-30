package uj.ac.monitus_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uj.ac.monitus_mobile.retrofit.MonitUsServiceGenerator;
import uj.ac.monitus_mobile.retrofit.RetrofitInterface;
import uj.ac.monitus_mobile.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    //intent Extra data
    public static final String EXTRA_STATISTICS_MESSAGE = "uj.ac.monitus_mobile.statistics";
    public static final String EXTRA_SUBMIT_MESSAGE = "uj.ac.monitus_mobile.submit";
    public static final String EXTRA_MARKS_MESSAGE = "uj.ac.monitus_mobile.marks";

    /*constant keys for saving user preferences*/
    public static final String SHARED_PREFS = "shared_prefs";

    //key for storing name
    public static final String NAME_KEY = "name_key";

    //key for storing surname
    public static final String SURNAME_KEY = "surname_key";

    //key for storing grade value
    public static final String GRADE_KEY = "grade_key";

    /* *********key constants end********* */

    //Variable for sharedPreferences
    SharedPreferences sharedPreferences;
    private String name, surname;
    private int grade;
    private boolean loggedIn_Status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize shared preferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        //get data from preferences
        name = sharedPreferences.getString(NAME_KEY, null);
        surname = sharedPreferences.getString(SURNAME_KEY, null);
        grade = sharedPreferences.getInt(GRADE_KEY, 0);

        //Initialize textview
        TextView dashboardName = findViewById(R.id.dashboard_names);
        TextView dashboardGrade = findViewById(R.id.dashboard_grade);
        dashboardName.setText(name + " " + surname);
        dashboardGrade.setText("Grade: " + grade);
    }

    public void logout(View view) {
        //edit values in preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //clear values in preferences
        editor.clear();

        //apply changes to preferences
        editor.apply();

        //redirect back to login screen
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //goto editing view
    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    //goto subject view
    public void subjects(View view) {
        Intent intent = new Intent(this, subjects.class);
        startActivity(intent);
    }

    //goto newsletter view
    public void  newsletters(View view) {
        Intent intent = new Intent(this, newsletter.class);
        startActivity(intent);
    }

    //goto report card view
    public void reportCard(View view){
        Intent intent = new Intent(this,report_card.class);
        startActivity(intent);
    }

    //goto statistics view
    public void Statistics(View view){
        //first send to subject view to get which subject
        Intent intent = new Intent(this, subjects.class);
        //send the request type to subject view
        intent.putExtra(EXTRA_STATISTICS_MESSAGE,"statistics");
        //Intent intent = new Intent(this,statistics_template.class);
        startActivity(intent);
    }

    //goto submissions
    public void Submit(View view){
        //first send to subject view to get which subject
        Intent intent = new Intent(this,subjects.class);
        //send the request type
        intent.putExtra(EXTRA_SUBMIT_MESSAGE,"submit");
        //Intent intent = new Intent(this,submit_template.class);
        startActivity(intent);
    }

    //goto marks
    public void Marks(View view){
        Intent intent = new Intent(this,subjects.class);
        //send request type
        intent.putExtra(EXTRA_MARKS_MESSAGE,"marks");
        //Intent intent = new Intent(this,marks_template.class);
        startActivity(intent);
    }

}