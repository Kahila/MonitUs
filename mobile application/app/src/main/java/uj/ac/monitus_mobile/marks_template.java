package uj.ac.monitus_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uj.ac.monitus_mobile.data.model.MarksModel;
import uj.ac.monitus_mobile.data.model.ReportModel;
import uj.ac.monitus_mobile.data.model.SubjectModel;
import uj.ac.monitus_mobile.retrofit.MonitUsServiceGenerator;
import uj.ac.monitus_mobile.retrofit.RetrofitInterface;
import uj.ac.monitus_mobile.ui.Assessments.AssessmentAdapter;
import uj.ac.monitus_mobile.ui.marks.MarksAdapter;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class marks_template extends AppCompatActivity implements MarksAdapter.OnMarksListener{
    /*constant keys for saving user preferences*/
    public static final String SHARED_PREFS = "shared_prefs";

    //key for storing name
    public static final String STUDENT_ID = "student_id_key";
    /* **************Key constants************** */

    //create local variable
    private MarksAdapter marksAdapter;

    //marks recyclerview
    private RecyclerView marksRV;

    //retrofit instance
    private RetrofitInterface service = MonitUsServiceGenerator.createService(RetrofitInterface.class);

    //list of results
    List<MarksModel> marksModelList;

    //SharedPreferences
    SharedPreferences sharedPreferences;

    //student id
    private int student_id;

    //selected subject
    private SubjectModel subject;

    private String markedScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks_template);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        student_id = sharedPreferences.getInt(STUDENT_ID, 0);

        // linking variable to actual listview class
        marksRV = findViewById(R.id.marks_recyclerview);
        marksAdapter = new MarksAdapter(marksModelList, this);

        marksModelList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        marksRV.setLayoutManager(linearLayoutManager);
        marksRV.setAdapter(marksAdapter);

        //get selected subject
        if(getIntent().hasExtra("selected_subject")){
            subject = getIntent().getParcelableExtra("selected_subject");
        }else if(getIntent().hasExtra("selected_report")){
            ReportModel report = getIntent().getParcelableExtra("selected_report");
            subject = new SubjectModel("none",report.getSubject_id(),0,0,0,"");
        }

        new LoadMarksData().execute(0);
    }

    private void displayMarks(){
        if(marksModelList.size() == 0){setContentView(R.layout.error_not_found);}
        else{
            marksAdapter = new MarksAdapter(marksModelList,this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            marksRV.setLayoutManager(linearLayoutManager);
            marksRV.setAdapter(marksAdapter);
        }
    }

    @Override
    public void onMarkClick(int position) {
        markedScript = marksModelList.get(position).getAssessment_name();
        String script = null;
        script = marksModelList.get(position).getMarked_script();
        if(script != null){
            new DownloadScript().execute(script);
        }else{
            new AlertDialog.Builder(marks_template.this)
                    .setTitle("Download script")
                    .setMessage("There is no marked script for this submission")
                    .show();
        }
    }

    /**
     * Inner class to retrieve marks
     */
    private class LoadMarksData extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            Call<List<MarksModel>> marksCall = service.listMarks(student_id,subject.getSubject_id());

            marksCall.enqueue(new Callback<List<MarksModel>>() {
                @Override
                public void onResponse(Call<List<MarksModel>> call, Response<List<MarksModel>> response) {
                    if(response.code() == 200){
                        marksModelList = new ArrayList<MarksModel>();
                        if(response.body() != null){
                            marksModelList.addAll(response.body());
                            displayMarks();
                        }
                    }else{setContentView(R.layout.error);}
                }

                @Override
                public void onFailure(Call<List<MarksModel>> call, Throwable t) {
                    setContentView(R.layout.error_failure);
                }
            });
            return "Completed task";
        }
    }

    /**
     * Inner class to run thread for
     * downloading assessment in background
     */
    private class DownloadScript extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            Call<ResponseBody> downloadCall = service.downloadAssessment(params[0]);

            downloadCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //write data to disk
                    boolean success = writeResponseToDisk(response.body());
                    openPDF();
                    //Toast.makeText(context.getApplicationContext(), "assessment downloaded", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(marks_template.this,"Could not download file",Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }

        //helper method writes resultant file to external disk storage
        private boolean writeResponseToDisk(ResponseBody body) {
            File assessmentFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), markedScript + ".pdf"
            );

            //setup filestreams
            InputStream is = null;
            OutputStream os = null;

            try{
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                is = body.byteStream();
                os = new FileOutputStream(assessmentFile);

                while(true){
                    int read = is.read(fileReader);

                    if(read == -1){break;}

                    os.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }
                os.flush();

                return true;
            } catch (FileNotFoundException e) {
                return false;
            } catch (IOException e) {
                return false;
            } finally {
                try{
                    if(is != null){
                        is.close();
                    }
                    if(os != null){
                        os.close();
                    }
                } catch (IOException e) {
                    return false;
                }
            }
        }
    }

    private void openPDF() {
        File assessmentFile = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), markedScript + ".pdf"
        );
        Uri path = FileProvider.getUriForFile(marks_template.this, marks_template.this.getPackageName() + ".provider", assessmentFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setDataAndType(path, "application/pdf");
        try {
            marks_template.this.startActivity(intent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(marks_template.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
        }
    }
}