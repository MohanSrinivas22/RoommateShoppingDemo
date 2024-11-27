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

public class EditShoppingItemDialogFragment extends DialogFragment {

    public static final int SAVE = 1;   // update an existing shopping item
    public static final int DELETE = 2; // delete an existing shopping item
    public static final int TO_BASKET = 3; // add the item to the purchase list

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

    public interface EditShoppingItemDialogListener{
        void updateShoppingItem(int position, Shopping shoppingItem, int action);
    }

    public static EditShoppingItemDialogFragment newInstance(int position, String key, String itemName, String category, int quantity, double price) {
        EditShoppingItemDialogFragment dialog = new EditShoppingItemDialogFragment();

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

        builder.setPositiveButton("SAVE", new SaveButtonClickListener());

        builder.setNegativeButton("DELETE", new DeleteButtonClickListener());

        builder.setNeutralButton("TO_BASKET", new ToBasketButtonClickListener());

        return builder.create();
    }

    private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i){
            String itemName = itemNameView.getText().toString();
            String category = categoryView.getText().toString();
            int quantity = Integer.parseInt(quantityView.getText().toString());
            double price = Double.parseDouble(priceView.getText().toString());

            Shopping shoppingItem = new Shopping(itemName, category, quantity, price);
            shoppingItem.setKey(key);

            EditShoppingItemDialogListener listener = (EditShoppingItemDialogListener) getActivity();

            listener.updateShoppingItem(position, shoppingItem, SAVE);

            dismiss();
        }
    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i){
            Shopping shoppingItem = new Shopping(itemName, category, quantity, price);
            shoppingItem.setKey(key);

            EditShoppingItemDialogListener listener = (EditShoppingItemDialogListener) getActivity();
            listener.updateShoppingItem(position, shoppingItem, DELETE);
            dismiss();
        }
    }

    private class ToBasketButtonClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i){
            String itemName = itemNameView.getText().toString();
            String category = categoryView.getText().toString();
            int quantity = Integer.parseInt(quantityView.getText().toString());
            double price = Double.parseDouble(priceView.getText().toString());

            Shopping basketItem = new Shopping(itemName, category, quantity, price);
            Shopping shoppingItem = new Shopping(itemName, category, quantity, price);

            shoppingItem.setKey(key);

            EditShoppingItemDialogListener listener = (EditShoppingItemDialogListener) getActivity();
            listener.updateShoppingItem(position, basketItem, TO_BASKET);

            listener.updateShoppingItem(position, shoppingItem, DELETE);
            dismiss();
        }
    }
}