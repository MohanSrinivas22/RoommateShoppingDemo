package edu.uga.cs.roommateshoppingdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class UserSignInDialogFragment extends DialogFragment {
    private EditText emailView;
    private EditText passwordView;

    public interface SignInDialogListener{
        void signIn(String email, String password);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.signin_dialog, getActivity().findViewById(R.id.signInPage));

        emailView = layout.findViewById( R.id.editTextTextEmailAddress );
        passwordView = layout.findViewById( R.id.editTextTextPassword );

        // create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        // Set its view (inflated above).
        builder.setView(layout);

        builder.setTitle("Sign In");

        builder.setNegativeButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });
        // Provide the positive button listener
        builder.setPositiveButton( android.R.string.ok, new SignInListener() );

        // Create the AlertDialog and show it
        return builder.create();
    }

    private class SignInListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // get the new job lead data from the user
            String email = emailView.getText().toString();
            String password = passwordView.getText().toString();

            // get the Activity's listener to add the new job lead
            UserSignInDialogFragment.SignInDialogListener listener = (UserSignInDialogFragment.SignInDialogListener) getActivity();

            // add the new job lead
            listener.signIn( email, password );

            // close the dialog
            dismiss();
        }
    }
}
