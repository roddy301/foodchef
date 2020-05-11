package com.justifiers.foodchef.LoginAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.SingleLineTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.justifiers.foodchef.BottomNavigationView.ProfileFragment;
import com.justifiers.foodchef.BottomNavigationView.SearchFragment;
import com.justifiers.foodchef.R;
import com.justifiers.foodchef.SettingsActivity;

public class LoginFragment extends Fragment {

    // Declare the variables here
    Toolbar lToolbar;
    TextInputLayout login_email_field;
    TextInputEditText login_email;
    TextInputLayout login_password_field;
    TextInputEditText login_password;
    TextView signUp;
    Button login;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View loginView =  inflater.inflate(R.layout.login_fragment, container, false);
        setHasOptionsMenu(true);
        // initializing the variables here
        lToolbar = loginView.findViewById(R.id.lToolbar);
        lToolbar.setTitle("Profile");
        ((AppCompatActivity) getActivity()).setSupportActionBar(lToolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        login_email_field = loginView.findViewById(R.id.login_email_layout);
        login_email = loginView.findViewById(R.id.login_email);
        login_password_field = loginView.findViewById(R.id.login_password_layout);
        login_password = loginView.findViewById(R.id.login_password);
        login = loginView.findViewById(R.id.login_button);
        signUp = loginView.findViewById(R.id.login_sign_up);
        login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            // enables the password toggle when start entering the password
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (login_password.getText().toString().length() > 0)
                    login_password_field.setPasswordVisibilityToggleEnabled(true);
                else {
                    login_password_field.setPasswordVisibilityToggleEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // listens to login button on click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_email.getText() != null && login_password.getText() != null) {
                    final String email = login_email.getText().toString();
                    final String password = login_password.getText().toString();
                    firebaseAuth = FirebaseAuth.getInstance();
                    // check if the email is empty
                    if (email.isEmpty()) {
                        // throws error for empty email
                        login_email.setError("Please enter your email Address");
                        login_email.requestFocus();
                    } else {
                        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                            login_email.setError("Please enter a valid email address only");
                            login_email.requestFocus();
                        }
                    }
                    if (password.isEmpty()) {
                        login_password_field.setPasswordVisibilityToggleEnabled(false);
                        login_password.setError("Please enter your password");
                    } else {
                        if (password.length() < 8) {
                            login_password_field.setPasswordVisibilityToggleEnabled(false);
                            login_password.setError("Please enter atleast 8 characters for password");
                            login_password.requestFocus();
                        }
                    }
                    // checks if email and password fields are not empty
                    if (!email.isEmpty() && !password.isEmpty() && password.length() > 8 && email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        // signs-in with email and password
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful() && getActivity() != null) {
                                    Toast.makeText(getActivity(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                    if(getActivity() != null) {
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_container, new ProfileFragment())
                                                .commit();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        // listens to sign-up button on click
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null) {
                    // opens the sign-up fragment if user does not have existing account
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new SignUpFragment())
                            .commit();
                }
            }
        });

        // it checks whether user has logged in before, so if the user has logged first time
        // they will not be asked next time to login in again
        // and will be redirected to profile fragment
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null && getActivity() != null){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment())
                            .commit();
                }
            }
        };
        return loginView;
    }

    @Override
    public void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.tool_settings) {
            Intent i = new Intent(getActivity(), SettingsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
