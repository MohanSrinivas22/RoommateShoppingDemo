package edu.uga.cs.roommateshoppingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShoppingManagementActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "ManagementActivity";
    protected TextView signedInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_management);

        Log.d( DEBUG_TAG, "ShoppingManagementActivity.onCreate()" );

        Button reviewShoppingBasketButton = findViewById(R.id.button2);
        Button reviewShoppingItemsButton = findViewById(R.id.button3);
        Button changePasswordButton = findViewById(R.id.button5);
        Button signoutButton = findViewById(R.id.button6);
        Button unRegisterButton = findViewById(R.id.button7);

        signedInTextView = findViewById(R.id.textView4);

        reviewShoppingItemsButton.setOnClickListener(new ReviewShoppingItemsButtonClickListener());
        reviewShoppingBasketButton.setOnClickListener(new ReviewShoppingBasketButtonClickListener());
        changePasswordButton.setOnClickListener(new ChangePasswordButtonClickListener());
        signoutButton.setOnClickListener(new SignOutButtonClickListener());
        unRegisterButton.setOnClickListener(new UnRegisterButtonClickListener());

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null){
                    // User is signed in
                    Log.d(DEBUG_TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
                    String userEmail = currentUser.getEmail();
                    signedInTextView.setText( "Signed in as: " + userEmail );
                } else{
                    // User is signed out
                    Log.d( DEBUG_TAG, "onAuthStateChanged:signed_out" );
                    signedInTextView.setText( "Signed in as: not signed in" );
                }
            }
        });


    }

    private class SignOutButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view){
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();

            Toast.makeText(ShoppingManagementActivity.this, "Signed out successfully.", Toast.LENGTH_SHORT).show();

            // Redirect to MainActivity
            Intent intent = new Intent(ShoppingManagementActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish();
        }
    }

    private class UnRegisterButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view){
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            String email = currentUser.getEmail();
            if(currentUser != null) currentUser.delete();
            Toast.makeText(ShoppingManagementActivity.this, "User Deleted Successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ShoppingManagementActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);
            finish();
        }
    }

    private class ChangePasswordButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view){
            Intent intent = new Intent(view.getContext(), ChangePasswordActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class ReviewShoppingItemsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            Intent intent = new Intent(view.getContext(), ReviewShoppingListActivity.class);
            view.getContext().startActivity(intent);
        }
    }

    private class ReviewShoppingBasketButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            Intent intent = new Intent(view.getContext(), ReviewShoppingBasketActivity.class);
            view.getContext().startActivity(intent);
        }
    }
}