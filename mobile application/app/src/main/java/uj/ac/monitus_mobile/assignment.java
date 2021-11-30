package uj.ac.monitus_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uj.ac.monitus_mobile.FileIO.FileUtil;
import uj.ac.monitus_mobile.data.model.PDFassessmentModel;
import uj.ac.monitus_mobile.data.model.SubjectModel;
import uj.ac.monitus_mobile.retrofit.MonitUsServiceGenerator;
import uj.ac.monitus_mobile.retrofit.RetrofitInterface;
import uj.ac.monitus_mobile.ui.Assessments.AssessmentAdapter;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class assignment extends AppCompatActivity implements AssessmentAdapter.callBackInterface{
    /* ***********Preferences constants*********** */
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String STUDENT_ID = "student_id_key";
    /* ******************************************** */

    SharedPreferences sharedPreferences;

    //external read code
    private final int EXTERNAL_STORAGE_PERMISSION_CODE = 23;
    private final int SUBMIT = 2;

    //get retrofit instance
    RetrofitInterface service = MonitUsServiceGenerator.createService(RetrofitInterface.class);

    //Recycler view instance
    private RecyclerView assessmentRV;

    //assessmentAdapter instance
    private AssessmentAdapter assessmentAdapter;

    //subject variables
    SubjectModel subject;

    //list of assessments
    List<PDFassessmentModel> pdfassessmentModelList;

    //assessment id to submit to
    public int assessment_id;

    private Uri fileToSubmit;

    public int student_id;

    //progressbar
    //ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        assessmentRV = findViewById(R.id.recyclerview_assessment);

        /*progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching assessments...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();*/
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //Integer intStudent_id = sharedPreferences.getInt(STUDENT_ID, 0);
        student_id = sharedPreferences.getInt(STUDENT_ID, 0);

        //initialize adapter to set data
        assessmentAdapter = new AssessmentAdapter(this,pdfassessmentModelList);
        //mIntentListener = assessmentAdapter;

        //set the layoutmanager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        //set layoutmanager and adapter to recycler
        assessmentRV.setLayoutManager(linearLayoutManager);
        assessmentRV.setAdapter(assessmentAdapter);

        //get selected subject
        if(getIntent().hasExtra("selected_subject")){
            subject = getIntent().getParcelableExtra("selected_subject");
        }

        //get permissions
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);

        new LoadAssessmentData().execute(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SUBMIT && resultCode == RESULT_OK){
            importFile(data.getData());
            fileToSubmit = data.getData();
            new SubmitAssessment().execute(0);
        }
    }

    private void importFile(Uri data) {
        String fileName = getFileName(data);
    }

    private String getFileName(Uri uri) {
        // Obtain a cursor with information regarding this uri
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            throw new IllegalArgumentException("Can't obtain file name, cursor is empty");
        }

        cursor.moveToFirst();

        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));

        cursor.close();

        return fileName;
    }

    private void displayAssessments() {
        if(pdfassessmentModelList.size() == 0){setContentView(R.layout.error_not_found);}
        else{
            //initialize adapter to set data
            assessmentAdapter = new AssessmentAdapter(this,pdfassessmentModelList);

            //set the layoutmanager
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

            //set layoutmanager and adapter to recycler
            assessmentRV.setLayoutManager(linearLayoutManager);
            assessmentRV.setAdapter(assessmentAdapter);
        }
    }

    @Override
    public void onHandleSubmit(int assessment_id) {
        //execute task to upload pdf to assessment
        this.assessment_id = assessment_id;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent,SUBMIT);
    }

    /**
     * Inner class to run thread for
     * retrieving assessments in background
     */
    private class LoadAssessmentData extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            Call<List<PDFassessmentModel>> assessmentCall = service.listAssessments(subject.getSubject_id());

            assessmentCall.enqueue(new Callback<List<PDFassessmentModel>>() {
                @Override
                public void onResponse(Call<List<PDFassessmentModel>> call, Response<List<PDFassessmentModel>> response) {
                    if(response.code() == 200){
                        pdfassessmentModelList = new ArrayList<>();
                        if(response.body() != null){
                            pdfassessmentModelList.addAll(response.body());
                            displayAssessments();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<PDFassessmentModel>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"couldn't get assessments", Toast.LENGTH_LONG).show();
                }
            });
            return "Completed task";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressDialog.hide();
        }
    }

    /**
     * Inner class to run thread for
     * uploading assessment in background
     */
    private class SubmitAssessment extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            File file = null;
            try {
                file = FileUtil.from(getApplicationContext(), fileToSubmit);
                Log.d("file", "File...:::: uti - "+file .getPath()+" file -" + file + " : " + file .exists());
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileToSubmit)), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");
            int Student_id = student_id;
            int Assessment_id = assessment_id;


            Call<ResponseBody> call = service.submit(body, name, Student_id, Assessment_id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.v("upload", "success");
                    Toast.makeText(getApplicationContext(),"Successfully submitted!",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("upload error:", t.getMessage());
                    Toast.makeText(getApplicationContext(),"Submission failed, try again",Toast.LENGTH_LONG).show();
                }
            });

            return "Completed Task";
        }
    }
}