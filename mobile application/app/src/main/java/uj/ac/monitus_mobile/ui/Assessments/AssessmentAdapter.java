package uj.ac.monitus_mobile.ui.Assessments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uj.ac.monitus_mobile.R;
import uj.ac.monitus_mobile.assignment;
import uj.ac.monitus_mobile.data.model.PDFassessmentModel;
import uj.ac.monitus_mobile.retrofit.MonitUsServiceGenerator;
import uj.ac.monitus_mobile.retrofit.RetrofitInterface;

import static androidx.core.app.ActivityCompat.getReferrer;
import static androidx.core.app.ActivityCompat.startActivityForResult;


/* Java class sets data held in pdfassessmentModel
   to items of assessment Recyclerview
 */
public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.ViewHolder> {
    public static final String SHARED_PREFS = "shared_prefs";
    private Context context;
    private List<PDFassessmentModel> pdFassessmentModelList;
    RetrofitInterface service;
    private String assessment_name;
    private static final int PICK_FILE = 2;
    private callBackInterface mCallBack;

    SharedPreferences pref;

    public interface callBackInterface{
        /**
         * Callback invoked when submit clicked
         */
        void onHandleSubmit(int assessment_id);
    }

    public AssessmentAdapter(Context context, List<PDFassessmentModel> pdFassessmentModelList) {
        this.context = context;
        this.pdFassessmentModelList = pdFassessmentModelList;
        pref = context.getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        service = MonitUsServiceGenerator.createService(RetrofitInterface.class);
        mCallBack = (callBackInterface) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView assessmentNameTV, assessmentDueDateTV, assessmentNoteTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentNameTV = itemView.findViewById(R.id.assessment_name_textview);
            assessmentDueDateTV = itemView.findViewById(R.id.assessment_duedate_textview);

            itemView.findViewById(R.id.assessment_download_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //execute task to download pdf of assignment
                    new DownloadAssessment().execute(pdFassessmentModelList.get(getAdapterPosition()).getPDF_File());
                }
            });

            itemView.findViewById(R.id.assessment_submit_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDate date = LocalDate.parse(pdFassessmentModelList.get(getAdapterPosition()).getDueDate());
                        if(LocalDate.now().isAfter(date)) {
                            new AlertDialog.Builder(context)
                                .setTitle("Late submission")
                                .setMessage("Submission past due")
                                .show();
                        }else{
                            if(mCallBack != null) {
                                mCallBack.onHandleSubmit(pdFassessmentModelList.get(getAdapterPosition()).getAssessment_id());
                            }
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public AssessmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_rowlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.ViewHolder holder, int position) {
        PDFassessmentModel pdf = pdFassessmentModelList.get(position);
        assessment_name = pdf.getAssessment_Name();
        holder.assessmentNameTV.setText(assessment_name);
        holder.assessmentDueDateTV.setText("Due: "+pdf.getDueDate());
    }

    @Override
    public int getItemCount() {
        int a;
        if(pdFassessmentModelList != null){
            a = pdFassessmentModelList.size();
        }else{
            a = 0;
        }
        return a;
    }

    /**
     * Inner class to run thread for
     * downloading assessment in background
     */
    private class DownloadAssessment extends AsyncTask<String, Integer, String> {

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
                    Toast.makeText(context.getApplicationContext(),"Could not download file",Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }

        //helper method writes resultant file to external disk storage
        private boolean writeResponseToDisk(ResponseBody body) {
            File assessmentFile = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), assessment_name + ".pdf"
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
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), assessment_name + ".pdf"
        );
        Uri path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", assessmentFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setDataAndType(path, "application/pdf");
        try {
            context.startActivity(intent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(context, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
        }
    }

}
