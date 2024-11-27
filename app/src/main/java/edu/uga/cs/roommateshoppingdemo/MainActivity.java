package edu.uga.cs.roommateshoppingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity
        implements UserSignInDialogFragment.SignInDialogListener{

    public static final String DEBUG_TAG = "MainActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button signInButton = findViewById(R.id.signIn);
        Button registerButton = findViewById(R.id.register);
        Button resetPasswordButton = findViewById(R.id.resetPassword);

        signInButton.setOnClickListener(new SignInButtonClickListener());
        registerButton.setOnClickListener(new RegisterButtonClickListener());
        resetPasswordButton.setOnClickListener(new ResetPasswordButtonClickListener());
    }

    @Override
    public void signIn(String email, String password){
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(DEBUG_TAG, "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this, ShoppingManagementActivity.class);
                            startActivity(intent);
                        }
                        else{
                            // If sign in fails, display a message to the user.
                            Log.d(DEBUG_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Invalid Credentials.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private class RegisterButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view){
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class SignInButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick( View v ) {
            DialogFragment newFragment = new UserSignInDialogFragment();
            newFragment.show( getSupportFragmentManager(), null );
        }
    }

    private class ResetPasswordButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            Intent intent = new Intent(view.getContext(), ResetPasswordActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}