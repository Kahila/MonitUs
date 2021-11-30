package uj.ac.monitus_mobile.data;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class UserSession {
    /*constant keys for saving user preferences*/
    public static final String SHARED_PREFS = "shared_prefs";

    //key for storing name
    public static final String NAME_KEY = "name_key";

    //key for storing surname
    public static final String SURNAME_KEY = "surname_key";

    //Key for storing username
    public static final String USERNAME_KEY = "username_key";

    //key for storing logged in status
    public static final String LOGGEDIN_KEY = "loggedIn_key";

    //key for storing grade value
    public static final String GRADE_KEY = "grade_key";

    /* *********key constants end********* */

    //preferences instances
    SharedPreferences sharedPreferences;

    //preferences editor for setting variables
    SharedPreferences.Editor editor;

    //Variable for sharedPreferences
    private String name, surname, username;
    private int grade;
    private boolean loggedIn_Status;

    //Initialize all session variables
    public UserSession(SharedPreferences sharedPreferences, String name, String surname, String username
                       , boolean loggedIn_Status, int grade) {

        //create session from shared preferences
        this.sharedPreferences = sharedPreferences;

        //get editor to setup session variables
        editor = sharedPreferences.edit();

        //set instance session variables
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.loggedIn_Status = loggedIn_Status;
        this.grade = grade;

        //set session variable values
        editor.putString(NAME_KEY,name);
        editor.putString(SURNAME_KEY,surname);
        editor.putString(USERNAME_KEY,username);
        editor.putBoolean(LOGGEDIN_KEY,loggedIn_Status);
        editor.putInt(GRADE_KEY,grade);

        //commit changes
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString(NAME_KEY,null);
    }

    public String getSurname() {
        return sharedPreferences.getString(SURNAME_KEY, null);
    }

    public String getUsername() {
        return sharedPreferences.getString(USERNAME_KEY,null);
    }

    public int getGrade() {
        return sharedPreferences.getInt(GRADE_KEY,0);
    }

    public boolean isLoggedIn_Status() {
        return sharedPreferences.getBoolean(LOGGEDIN_KEY,false);
    }

    public void setName(String name) {
        this.name = name;
        editor.putString(NAME_KEY,name);
    }

    public void setSurname(String surname) {
        this.surname = surname;
        editor.putString(SURNAME_KEY,surname);
    }

    public void setUsername(String username) {
        this.username = username;
        editor.putString(USERNAME_KEY,username);
    }

    public void setGrade(int grade) {
        this.grade = grade;
        editor.putInt(GRADE_KEY,grade);
    }

    public void setLoggedIn_Status(boolean loggedIn_Status) {
        this.loggedIn_Status = loggedIn_Status;
        editor.putBoolean(LOGGEDIN_KEY,loggedIn_Status);
    }
}
