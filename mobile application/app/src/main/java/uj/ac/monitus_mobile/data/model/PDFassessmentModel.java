package uj.ac.monitus_mobile.data.model;

import android.os.Build;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import androidx.annotation.RequiresApi;

/* Java class stores data specific
 * PDF assessments
 */
public class PDFassessmentModel {
    private String Assessment_Name, DueDate, PDF_File;
    private int Assessment_id;

    public PDFassessmentModel(String Assessment_Name, String dueDate, String PDF_File, int Assessment_id) {
        this.Assessment_Name = Assessment_Name;
        this.DueDate = null;
        this.PDF_File = PDF_File;
        this.Assessment_id = Assessment_id;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(dueDate, inputFormatter);
            this.DueDate = outputFormatter.format(date);
        }
    }

    public String getAssessment_Name() {
        return Assessment_Name;
    }

    public String getDueDate() {
        String duedate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(this.DueDate, inputFormatter);
            duedate = outputFormatter.format(date);
            return duedate;
        }
        else{return DueDate;}
    }

    public String getPDF_File() {
        return PDF_File;
    }

    public int getAssessment_id() {return Assessment_id;}
}
