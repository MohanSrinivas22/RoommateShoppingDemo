package edu.uga.cs.roommateshoppingdemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingRecyclerAdapter.ShoppingItemHolder>{
    public static final String DEBUG_TAG = "ShoppingRecyclerAdapter";
    protected List<Shopping> shoppingList;
    protected Context context;

    public ShoppingRecyclerAdapter(List<Shopping> shoppingList, Context context){
        this.shoppingList = shoppingList;
        this.context = context;
    }

    class ShoppingItemHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView category;
        TextView quantity;
        TextView price;

        public ShoppingItemHolder(View itemView){
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            category = itemView.findViewById(R.id.category);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
        }
    }

    @NonNull
    @Override
    public ShoppingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.shopping, parent, false );
        return new ShoppingItemHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemHolder holder, int position) {
        Shopping shoppingItem = shoppingList.get(position);

        Log.d( DEBUG_TAG, "onBindViewHolder: " + shoppingItem );

        String key = shoppingItem.getKey();
        String itemName = shoppingItem.getItemName();
        String category = shoppingItem.getCategory();
        int quantity = shoppingItem.getQuantity();
        double price = shoppingItem.getPrice();

        holder.itemName.setText(shoppingItem.getItemName());
        holder.category.setText(shoppingItem.getCategory());
        holder.quantity.setText("" + shoppingItem.getQuantity());
        holder.price.setText("" + shoppingItem.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditShoppingItemDialogFragment editShoppingFragment =
//                        EditShoppingItemDialogFragment.newInstance( holder.getAdapterPosition(), key, itemName, category, quantity, price );
//                editShoppingFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }


}
