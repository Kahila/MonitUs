package uj.ac.monitus_mobile.data.model;

import com.google.gson.annotations.SerializedName;

public class MarksModel {
    private String Assessment_Name, marked_script;
    private int score, submission_id;

    @SerializedName("total")
    private int Num_marks;

    @SerializedName("weight")
    private int weighting;

    public MarksModel(String assessment_name, int score, int Num_marks, int weighting, int submission_id, String marked_script) {
        this.Assessment_Name = assessment_name;
        this.Num_marks = Num_marks;
        this.score = score;
        this.weighting = weighting;
        this.submission_id = submission_id;
        this.marked_script = marked_script;
    }

    public int getSubmission_id() {
        return submission_id;
    }

    public String getMarked_script() {
        return marked_script;
    }

    public int getNum_marks() {
        return Num_marks;
    }

    public int getWeighting() {
        return weighting;
    }

    public String getAssessment_name() {
        return Assessment_Name;
    }

    public void setAssessment_name(String assessment_name) {
        Assessment_Name = assessment_name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        score = score;
    }
}
