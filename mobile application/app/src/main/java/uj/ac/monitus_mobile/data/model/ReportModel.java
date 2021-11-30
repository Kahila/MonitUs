package uj.ac.monitus_mobile.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class stores data recieved from
 * calculated subject mark from assessment mark
 */
public class ReportModel implements Parcelable {
    private String subject;
    private double Mark;
    private int subject_id, Assessment_weighting;

    public ReportModel(String subject, double mark, int subject_id, int Assessment_weighting) {
        this.subject = subject;
        Mark = Math.round(mark);
        this.subject_id = subject_id;
        this.Assessment_weighting = Assessment_weighting;
    }

    protected ReportModel(Parcel in) {
        subject = in.readString();
        Mark = in.readDouble();
        subject_id = in.readInt();
        Assessment_weighting = in.readInt();
    }

    public static final Creator<ReportModel> CREATOR = new Creator<ReportModel>() {
        @Override
        public ReportModel createFromParcel(Parcel in) {
            return new ReportModel(in);
        }

        @Override
        public ReportModel[] newArray(int size) {
            return new ReportModel[size];
        }
    };

    public String getSubject() {
        return subject;
    }

    public double getMark() {
        return Mark;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public int getAssessment_weighting() {
        return Assessment_weighting;
    }

    public double getPercentage() {return Math.round((Mark / Assessment_weighting) * 100);}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(subject);
        parcel.writeDouble(Mark);
        parcel.writeInt(subject_id);
        parcel.writeInt(Assessment_weighting);
    }
}
