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

public class EditShoppingBasketDialogFragment extends DialogFragment {

    public static final int SAVE = 1;   // update an existing shopping bucket item
    public static final int DELETE = 2; // delete an item from bucket and place it in shopping list

    protected EditText itemNameView;
    protected EditText categoryView;
    protected EditText quantityView;
    protected EditText priceView;

    int position;
    String key;
    String itemName;
    String category;
    int quantity;
    double price;

    public interface EditShoppingBasketDialogListener{
        void updateShoppingBasket(int position, Shopping shoppingItem, int action);
    }

    public static EditShoppingBasketDialogFragment newInstance(int position, String key, String itemName, String category, int quantity, double price) {
        EditShoppingBasketDialogFragment dialog = new EditShoppingBasketDialogFragment();

        // Supply job lead values as an argument.
        Bundle args = new Bundle();
        args.putString( "key", key );
        args.putInt( "position", position );
        args.putString("itemName", itemName);
        args.putString("category", category);
        args.putInt("quantity", quantity);
        args.putDouble("price", price);
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        key = getArguments().getString("key");
        position = getArguments().getInt("position");
        itemName = getArguments().getString("itemName");
        category = getArguments().getString("category");
        quantity= getArguments().getInt("quantity");
        price = getArguments().getDouble("price");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate( R.layout.add_shopping_item_dialog, getActivity().findViewById( R.id.root ) );

        itemNameView = layout.findViewById(R.id.addItemName);
        categoryView = layout.findViewById(R.id.addCategory);
        quantityView = layout.findViewById(R.id.addQuantity);
        priceView = layout.findViewById(R.id.addPrice);

        itemNameView.setText(itemName);
        categoryView.setText(category);
        quantityView.setText("" + quantity);
        priceView.setText("" + price);

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.AlertDialogStyle );
        builder.setView(layout);

        builder.setTitle("Edit Shopping Item");

        builder.setPositiveButton("SAVE", new EditShoppingBasketDialogFragment.SaveButtonClickListener());

        builder.setNegativeButton("DELETE", new EditShoppingBasketDialogFragment.DeleteButtonClickListener());

        builder.setNeutralButton( android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // close the dialog
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i){
            String itemName = itemNameView.getText().toString();
            String category = categoryView.getText().toString();
            int quantity = Integer.parseInt(quantityView.getText().toString());
            double price = Double.parseDouble(priceView.getText().toString());

            Shopping basketItem = new Shopping(itemName, category, quantity, price);
            basketItem.setKey(key);

            EditShoppingBasketDialogFragment.EditShoppingBasketDialogListener listener = (EditShoppingBasketDialogFragment.EditShoppingBasketDialogListener) getActivity();

            listener.updateShoppingBasket(position, basketItem, SAVE);

            dismiss();
        }
    }


    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i){
            Shopping shoppingItem = new Shopping(itemName, category, quantity, price);

//             Add the item back to the shopping list.
            AddShoppingItemDialogFragment.AddShoppingItemDialogListener list = (AddShoppingItemDialogFragment.AddShoppingItemDialogListener) getActivity();
            list.addShoppingItem(shoppingItem);

            Shopping basketItem = new Shopping(itemName, category, quantity, price);
            basketItem.setKey(key);

            // Now delete the existing basket item.
            EditShoppingBasketDialogFragment.EditShoppingBasketDialogListener listener = (EditShoppingBasketDialogFragment.EditShoppingBasketDialogListener) getActivity();
            listener.updateShoppingBasket(position, basketItem, DELETE);

            dismiss();
        }
    }
}
