package uj.ac.monitus_mobile.data.model;

/*Java class stores data specific to subject
 *which is displayed in SubjectsAdapter recyclerview */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class SubjectModel implements Parcelable {
    @SerializedName("subject_id")
    private int subject_id;

    @SerializedName("subject")
    private String subject_name;

    @SerializedName("teacher_id")
    private int subject_teacher_id;

    @SerializedName("teacher_name")
    private String subject_teacher_name;

    private int subject_image;

    @SerializedName("grade_id")
    private int subject_grade;

    public SubjectModel(String subject_name, int subject_id, int subject_teacher_id, int subject_image, int subject_grade, String subject_teacher_name) {
        this.subject_name = subject_name;
        this.subject_image = subject_image;
        this.subject_teacher_id = subject_teacher_id;
        this.subject_grade = subject_grade;
        this.subject_id = subject_id;
        this.subject_teacher_name = subject_teacher_name;
    }

    protected SubjectModel(Parcel in) {
        subject_name = in.readString();
        subject_teacher_id = in.readInt();
        subject_image = in.readInt();
        subject_grade = in.readInt();
        subject_id = in.readInt();
        subject_teacher_name = in.readString();
    }

    public static final Creator<SubjectModel> CREATOR = new Creator<SubjectModel>() {
        @Override
        public SubjectModel createFromParcel(Parcel in) {
            return new SubjectModel(in);
        }

        @Override
        public SubjectModel[] newArray(int size) {
            return new SubjectModel[size];
        }
    };

    public String getSubject_teacher_name() {
        return subject_teacher_name;
    }

    public void setSubject_teacher_name(String subject_teacher_name) {
        this.subject_teacher_name = subject_teacher_name;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getSubject_teacher_id() {
        return subject_teacher_id;
    }

    public void setSubject_teacher_id(int subject_teacher_id) {
        this.subject_teacher_id = subject_teacher_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public int getSubject_image() {
        return subject_image;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public void setSubject_image(int subject_image) {
        this.subject_image = subject_image;
    }

    public int getSubject_grade() {
        return subject_grade;
    }

    public void setSubject_grade(int subject_grade) {
        this.subject_grade = subject_grade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(subject_name);
        parcel.writeInt(subject_teacher_id);
        parcel.writeInt(subject_image);
        parcel.writeInt(subject_grade);
        parcel.writeInt(subject_id);
        parcel.writeString(subject_teacher_name);
    }
}
