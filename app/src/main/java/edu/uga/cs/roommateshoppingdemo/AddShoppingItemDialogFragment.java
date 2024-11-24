package edu.uga.cs.roommateshoppingdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddShoppingItemDialogFragment extends DialogFragment {
    protected EditText itemNameView;
    protected EditText categoryView;
    protected EditText quantityView;
    protected EditText priceView;

    public interface AddShoppingItemDialogListener{
        void addShoppingItem(Shopping shoppingItem);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_shopping_item_dialog,
                                                getActivity().findViewById(R.id.root));
        itemNameView = layout.findViewById( R.id.addItemName );
        categoryView = layout.findViewById( R.id.addCategory );
        quantityView = layout.findViewById( R.id.addQuantity );
        priceView = layout.findViewById( R.id.addPrice );

        // create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        // Set its view (inflated above).
        builder.setView(layout);

        builder.setTitle("New Shopping Item");

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(android.R.string.ok, new AddShoppingItemListener());

        return builder.create();
    }


    private class AddShoppingItemListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i){
            String itemName = itemNameView.getText().toString();
            String category = categoryView.getText().toString();
            int quantity = Integer.parseInt(quantityView.getText().toString());
            double price = Double.parseDouble(priceView.getText().toString());

            Shopping shoppingItem = new Shopping(itemName, category, quantity, price);
            AddShoppingItemDialogListener listener = (AddShoppingItemDialogListener) getActivity();
            listener.addShoppingItem(shoppingItem);
            dismiss();
        }
    }
}
