package uj.ac.monitus_mobile.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SharedMemory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uj.ac.monitus_mobile.MainActivity;
import uj.ac.monitus_mobile.R;
import uj.ac.monitus_mobile.retrofit.MonitUsServiceGenerator;
import uj.ac.monitus_mobile.retrofit.RetrofitInterface;
import uj.ac.monitus_mobile.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
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

    //key for student_id
    public static final String STUDENT_ID = "student_id_key";

    /* *********key constants end********* */

    //Variable for sharedPreferences
    SharedPreferences sharedPreferences;
    private String name, surname, username;
    private int grade, student_id;
    private boolean loggedIn_Status;

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    LoginResult result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new LoginViewModel();

        //getting stored student preferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        //set preferences if they exist, if not set to null
        name = sharedPreferences.getString(NAME_KEY, null);
        surname = sharedPreferences.getString(SURNAME_KEY, null);
        username = sharedPreferences.getString(USERNAME_KEY, null);
        grade = sharedPreferences.getInt(GRADE_KEY,0);
        loggedIn_Status = sharedPreferences.getBoolean(LOGGEDIN_KEY, false);
        student_id = sharedPreferences.getInt(STUDENT_ID, 0);

        //create retrofitInterface object
        RetrofitInterface service = MonitUsServiceGenerator.createService(RetrofitInterface.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //create call class object to hold entered user details
                Call<LoginResult> call = service.executeLogin(usernameEditText.getText().toString(),
                                                                        passwordEditText.getText().toString());

                //execute HTTP request
                call.enqueue(new Callback<LoginResult>() {
                    //If HTTP request was successful
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.code() == 200){
                            result = response.body();
                            if(result == null){
                                showLoginFailed("Wrong credentials");
                                loadingProgressBar.setVisibility(View.GONE);
                            }else{
                                loadingProgressBar.setVisibility(View.GONE);
                                setStudentPreferences(editor, result.getName(), result.getSurname(),
                                                     result.getUsername(), result.isLogged_in(), result.getGrade(), result.getId());
                            }
                        }else if (response.code() == 400){
                            showLoginFailed("Wrong credentials");
                            loadingProgressBar.setVisibility(View.GONE);
                        }
                    }

                    //If HTTP request was unsuccessful
                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        loadingProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    //Method creates preferences file for managing user session
    private void setStudentPreferences(SharedPreferences.Editor editor, String name, String surname,
                                       String username, boolean loggedIn, int grade, int student_id) {
        //add user details to preferences
        editor.putString(NAME_KEY, name);
        editor.putString(SURNAME_KEY, surname);
        editor.putString(USERNAME_KEY, username);
        editor.putBoolean(LOGGEDIN_KEY, loggedIn);
        editor.putInt(GRADE_KEY, grade);
        editor.putInt(STUDENT_ID, student_id);

        //apply changes with key and value pairs
        editor.apply();

        //move user to mainActivity dashboard
        String welcome = getString(R.string.welcome) + name;
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        finish();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(loggedIn_Status != false){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

}