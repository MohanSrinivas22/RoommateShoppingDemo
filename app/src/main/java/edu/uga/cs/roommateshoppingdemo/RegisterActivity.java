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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "RegisterActivity";

    protected EditText emailEditText;
    protected EditText passworEditText;
    protected EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.editTextEmailAddress);
        nameEditText = findViewById(R.id.editTextName);
        passworEditText = findViewById(R.id.editTextPassword);
        Button registerSubmit = findViewById(R.id.submit);
        registerSubmit.setOnClickListener(new RegisterSubmitClickListener());
    }

    private class RegisterSubmitClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            final String name = nameEditText.getText().toString();
            final String email = emailEditText.getText().toString();
            final String password = passworEditText.getText().toString();

            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            // This is how we can create a new user using an email/password combination.
            // Note that we also add an onComplete listener, which will be invoked once
            // a new user has been created by Firebase.  This is how we will know the
            // new user creation succeeded or failed.
            // If a new user has been created, Firebase already signs in the new user;
            // no separate sign in is needed.

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(),
                                        "Registered user: " + email,
                                        Toast.LENGTH_SHORT).show();

                                Log.d(DEBUG_TAG, "createUserWithEmail: success");

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Intent intent = new Intent(RegisterActivity.this, ShoppingManagementActivity.class);
                                startActivity(intent);

                            } else {
                                Log.w(DEBUG_TAG, "createUserWithEmail: failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}