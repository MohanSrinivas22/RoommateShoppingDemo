package edu.uga.cs.roommateshoppingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ResetPasswordActivity";
    protected EditText emailIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        Log.d( DEBUG_TAG, "ChangePasswordActivity.onCreate()" );

        emailIdEditText = findViewById(R.id.editTextTextEmailAddress2);

        Button cancelButton = findViewById(R.id.cancelReset);
        Button saveButton = findViewById(R.id.saveReset);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                view.getContext().startActivity(intent);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                final String email = emailIdEditText.getText().toString().trim();

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Reset Link sent.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send link: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);
                finish();
            }
        });


    }
}