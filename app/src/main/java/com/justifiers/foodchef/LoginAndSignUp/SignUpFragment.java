package com.justifiers.foodchef.LoginAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.FirebaseDatabase;
import com.justifiers.foodchef.BottomNavigationView.ProfileFragment;
import com.justifiers.foodchef.BottomNavigationView.SearchFragment;
import com.justifiers.foodchef.R;
import com.justifiers.foodchef.SettingsActivity;


public class SignUpFragment extends Fragment {

    // Declare Variables here
    TextInputLayout sign_up_email_layout;
    TextInputLayout sign_up_name_layout;
    TextInputLayout sign_up_password_layout;
    TextInputEditText sign_up_email;
    TextInputEditText sign_up_name;
    TextInputEditText sign_up_password;
    Toolbar sToolbar;
    Button signUp;
    TextView login;
    TextView sign_up_login_here;
    FirebaseAuth firebaseAuth;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View signUpView = inflater.inflate(R.layout.sign_up_fragment, container, false);
        setHasOptionsMenu(true);
        firebaseAuth = FirebaseAuth.getInstance();
        sign_up_email_layout = signUpView.findViewById(R.id.sign_up_email_layout);
        sign_up_name_layout = signUpView.findViewById(R.id.sign_up_name_layout);
        sign_up_password_layout = signUpView.findViewById(R.id.sign_up_password_layout);
        sign_up_email = signUpView.findViewById(R.id.sign_up_email);
        sign_up_name = signUpView.findViewById(R.id.sign_up_name);
        sign_up_password = signUpView.findViewById(R.id.sign_up_password);
        signUp = signUpView.findViewById(R.id.sign_up_button);
        sign_up_login_here = signUpView.findViewById(R.id.sign_up_login_here);
        sToolbar = signUpView.findViewById(R.id.sign_up_toolbar);
        sToolbar.setTitle("Profile");
        ((AppCompatActivity) getActivity()).setSupportActionBar(sToolbar);

        sign_up_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (sign_up_password.getText().toString().length() > 0)
                    sign_up_password_layout.setPasswordVisibilityToggleEnabled(true);
                else {
                    sign_up_password_layout.setPasswordVisibilityToggleEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = sign_up_email.getText().toString();
                final String password = sign_up_password.getText().toString();
                final String name = sign_up_name.getText().toString();
                // check if the email is empty
                if(email.isEmpty()){
                    // throws error for empty email
                    sign_up_email.setError("Please enter your email Address");
                    sign_up_email.requestFocus();
                }else
                {
                    if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
                        sign_up_email.setError("Please enter a valid email address only");
                        sign_up_email.requestFocus();
                    }
                } if(password.isEmpty()){
                    sign_up_password_layout.setPasswordVisibilityToggleEnabled(false);
                    sign_up_password.setError("Please enter your password");
                    sign_up_password.requestFocus();
                } else {
                    if (password.length() < 8) {
                        sign_up_password_layout.setPasswordVisibilityToggleEnabled(false);
                        sign_up_password.setError("Please enter atleast 8 characters for password");
                        sign_up_password.requestFocus();
                    }
                }
                if (name.isEmpty()){
                    sign_up_name.setError("Please enter your name");
                    sign_up_name.requestFocus();
                }
                if(!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && password.length() > 8 && email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(getActivity(),"SignUp Failed!", Toast.LENGTH_SHORT).show();
                            } else {
                                User user = new User();
                                user.setName(name);
                                user.setEmail(email);
                                FirebaseDatabase.getInstance().getReference("User")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful() && getActivity() != null){
                                            Toast.makeText(getActivity(), "User Registered",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
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
        });

        sign_up_login_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new LoginFragment()).commit();
                }
            }
        });

        return signUpView;
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
