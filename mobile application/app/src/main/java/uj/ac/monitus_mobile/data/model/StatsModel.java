package uj.ac.monitus_mobile.data.model;

/**
 * Model class contains all reported data
 * retireved from database on marks for subject
 */
public class StatsModel {
    private int student_id;
    private double Percentage;

    public StatsModel(double Percentage, int student_id) {
        this.Percentage = Math.round(Percentage);
        this.student_id = student_id;
    }

    public double getPercentage() {
        return Percentage;
    }

    public int getStudent_id() {
        return student_id;
    }
}
