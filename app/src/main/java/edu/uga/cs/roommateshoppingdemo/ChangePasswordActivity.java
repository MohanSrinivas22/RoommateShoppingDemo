package edu.uga.cs.roommateshoppingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthKt;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ChangePasswordActivity";
    protected EditText newPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        Log.d( DEBUG_TAG, "ChangePasswordActivity.onCreate()" );

        newPasswordEditText = findViewById(R.id.editTextTextPassword2);

        Button saveNewPassword = findViewById(R.id.button9);
        Button cancelButton = findViewById(R.id.button8);

        saveNewPassword.setOnClickListener(new SaveNewPasswordClickListener());
        cancelButton.setOnClickListener(new CancelButtonClickListener());
    }

    private class SaveNewPasswordClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view){
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            final String newPassword = newPasswordEditText.getText().toString().trim();
            if(newPassword.isEmpty()) {
                Toast.makeText(ChangePasswordActivity.this, "New password should not be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if(currentUser != null) {
                currentUser.updatePassword(newPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangePasswordActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ChangePasswordActivity.this, "Failed to update password: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            } else{
                Toast.makeText(ChangePasswordActivity.this, "No authenticated user found.", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish();
        }
    }

    private class CancelButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view){
            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish();
        }
    }
}