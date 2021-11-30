package uj.ac.monitus_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {
    /*constant keys for saving user preferences*/
    public static final String SHARED_PREFS = "shared_prefs";

    //key for storing name
    public static final String NAME_KEY = "name_key";

    //key for storing surname
    public static final String SURNAME_KEY = "surname_key";

    //Key for storing username
    public static final String USERNAME_KEY = "username_key";
    /* **************Key constants************** */

    //Variable for sharedPreferences
    SharedPreferences sharedPreferences;
    private String name, surname, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        TextView nameEditText = findViewById(R.id.Profile_first_name);
        TextView surnameEditText = findViewById(R.id.Profile_last_name);

        nameEditText.setText(sharedPreferences.getString(NAME_KEY,null));
        surnameEditText.setText(sharedPreferences.getString(SURNAME_KEY,null));

    }
}