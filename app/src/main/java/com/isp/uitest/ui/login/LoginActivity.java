package com.isp.uitest.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isp.uitest.Class.Staff;
import com.isp.uitest.Class.Student;
import com.isp.uitest.Class.User;
import com.isp.uitest.DrawerActivity;
import com.isp.uitest.R;
import com.isp.uitest.ui.login.LoginViewModel;
import com.isp.uitest.ui.login.LoginViewModelFactory;

import java.util.HashMap;

import static com.isp.uitest.ui.login.ButtonEffect.buttonEffect;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private HashMap<String,HashMap> userMap;
    private final String sharedPrefFile = "com.example.android.mainsharedprefs";
    public static final String UKEY = "Username";
    public static final String PKEY = "Password";
    public static final String CKEY = "isChecked";
    public static boolean canSignin = false;
    SharedPreferences mPreferences;
    final DatabaseReference mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference usr_root = mRootDatabaseRef.child("User");
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    CheckBox rememberMe;
    ProgressBar loadingProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usr_root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userMap = (HashMap<String, HashMap>) snapshot.getValue();
                canSignin = true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }});

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        mPreferences = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        usernameEditText.setText(mPreferences.getString(UKEY,""));
        passwordEditText.setText(mPreferences.getString(PKEY,""));
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        rememberMe = findViewById(R.id.remember_me);
        rememberMe.setChecked(mPreferences.getBoolean(CKEY,false));
        buttonEffect(loginButton);

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

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                startActivity(intent);
                finish();
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
                System.out.println("edited");
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        if (!usernameEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals(""))
            loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                passwordEditText.getText().toString());

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
                System.out.println("Click");
                HashMap rawUser = canSignin == true?userMap.get(usernameEditText.getText().toString()):null;
                if ( rawUser != null ){
                    if (passwordEditText.getText().toString().equals(rawUser.get("password"))){
                        if (rawUser.get("role").equals("Student")) {
                            LoginInfo info = (LoginInfo)getApplication();
                            info.setUser(new Student((String)rawUser.get("name"),Long.toString((Long)rawUser.get("id")), (String)rawUser.get("password"), (String)rawUser.get("event"))); }
                        else if (rawUser.get("role").equals("Staff")) {
                            LoginInfo info = (LoginInfo)getApplication();
                            info.setUser(new Staff((String)rawUser.get("name"),Long.toString((Long)rawUser.get("id")), (String)rawUser.get("password"),(String)rawUser.get("event"))); }
                        Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                        startActivity(intent);
                    }
                    else {Toast.makeText(LoginActivity.this,"Wrong Password", Toast.LENGTH_LONG).show();};
                }
                else{Toast.makeText(LoginActivity.this,"User does not exist", Toast.LENGTH_LONG).show();}
            }
        });

       /* loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });*/
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    @Override
    protected void onPause(){
        super.onPause();
        System.out.println("paused");
        SharedPreferences.Editor pEditor = mPreferences.edit();
        if(rememberMe.isChecked()) {
            pEditor.putString(UKEY,usernameEditText.getText().toString());
            pEditor.putString(PKEY,passwordEditText.getText().toString());
            pEditor.putBoolean(CKEY,true);

        }
        else{pEditor.putString(UKEY,"");
            pEditor.putString(PKEY,"");
            pEditor.putBoolean(CKEY,false);
        }
        pEditor.apply();
    }


}