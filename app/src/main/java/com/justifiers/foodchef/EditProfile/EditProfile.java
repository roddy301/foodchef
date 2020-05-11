package com.justifiers.foodchef.EditProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.justifiers.foodchef.BottomNavigationView.SearchFragment;
import com.justifiers.foodchef.LoginAndSignUp.LoginFragment;
import com.justifiers.foodchef.MainActivity;
import com.justifiers.foodchef.R;

import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    // declare variables here
    ImageButton changeUploadImage;
    ImageView showImage;
    ImageButton closeButton;
    TextInputEditText editProfileReadName;
    TextInputEditText editProfileReadEmail;
    TextInputEditText editProfileChangePassword;
    TextInputEditText editProfileChangeConfirmPassword;
    TextInputLayout editProfileChangePasswordLayout;
    TextInputLayout editProfileConfirmPasswordLayout;
    Button log_out;
    TextView done;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference mStorageRef;


    private static final String TAG = EditProfile.class.getSimpleName();
    private int FETCH_IMAGE_REQUEST = 1;
    private String selectedPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // initializing the variables
        editProfileReadName = findViewById(R.id.edit_profile_name);
        editProfileReadEmail = findViewById(R.id.edit_profile_email);
        editProfileChangePassword = findViewById(R.id.edit_profile_password);
        editProfileChangeConfirmPassword = findViewById(R.id.edit_profile_confirm_password);
        editProfileChangePasswordLayout = findViewById(R.id.edit_profile_password_layout);
        editProfileConfirmPasswordLayout = findViewById(R.id.edit_profile_confirm_password_layout);

        log_out = findViewById(R.id.edit_profile_log_out_button);
        done = findViewById(R.id.edit_profile_done);

        showImage = findViewById(R.id.edit_profile_image);
        closeButton = findViewById(R.id.edit_profile_close_icon);
        changeUploadImage = findViewById(R.id.edit_profile_change_icon);
        // listens to upload image button
        changeUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calls the select image function to choose image from device storage
                selectImage();
            }
        });
        // listens to close button if user nothing wants to save
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // returns to previous fragment on back press
                EditProfile.super.onBackPressed();
            }
        });

        // initializing firebase Authentication instance
        firebaseAuth = FirebaseAuth.getInstance();
        // fetches the reference for current user who has logged in
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // gets reference to user collection
        databaseReference = firebaseDatabase.getReference("User");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                editProfileReadName.setText(name);
                editProfileReadEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // listens to log out button
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // listens to done text view and saves the details of what user has saved details about
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = editProfileChangePassword.getText().toString();
                String confirm_password = editProfileChangeConfirmPassword.getText().toString();
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm_password) && (password.equals(confirm_password))) {
                    if (user != null) {
                        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Password could not changed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }
                if (!password.equals(confirm_password)) {
                    editProfileChangePasswordLayout.setPasswordVisibilityToggleEnabled(false);
                    editProfileConfirmPasswordLayout.setPasswordVisibilityToggleEnabled(false);
                    editProfileChangePassword.setError("Passwords do not Match");
                    editProfileChangeConfirmPassword.setError("Password not Match");
                }
                if (TextUtils.isEmpty(password)) {
                    editProfileChangePassword.setError("Password field can't be left empty");
                } else {
                    if (password.length() < 8) {
                        editProfileChangePasswordLayout.setPasswordVisibilityToggleEnabled(false);
                        editProfileChangePassword.setError("Password must be more than 8 characters");
                    }
                }
                if (TextUtils.isEmpty(confirm_password)) {
                    editProfileConfirmPasswordLayout.setPasswordVisibilityToggleEnabled(false);
                    editProfileChangeConfirmPassword.setError("Confirm Password can't be left empty");
                }
            }
        });

    }

    // choose images from device storage from device storage
    public void selectImage() {
        // creates intent object to initiate action
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a picture"), FETCH_IMAGE_REQUEST);

    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FETCH_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                showImage.setImageBitmap(imageBitmap);
                // Saving the user profile picture in firebase Storage location
                mStorageRef = FirebaseStorage.getInstance().getReference().child("profile_picture/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                mStorageRef.putFile(imageUri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error Occurred While Fetching Image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}