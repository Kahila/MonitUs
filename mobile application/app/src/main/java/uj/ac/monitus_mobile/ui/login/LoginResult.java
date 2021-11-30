package uj.ac.monitus_mobile.ui.login;

import com.google.gson.annotations.SerializedName;

/**
 * Authentication result : success (user details) or error message.
 * Class stores successful user login details (name, surname)
 */
public class LoginResult {
    //unique user id in database
    private int id;

    //user logged in status
    @SerializedName("loggen_in")
    private boolean logged_in = false;

    //user personal credentials
    private String username, name, surname;

    //user grade (8,9,...)
    private int grade;

    //user subtype discriminator (student,teacher or receptionist)
    private int rank;

    public int getId() {
        return id;
    }

    public boolean isLogged_in() {
        return logged_in;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getGrade() {
        return grade;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}