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

import java.util.List;

public class BasketRecyclerAdapter extends RecyclerView.Adapter<BasketRecyclerAdapter.BasketItemHolder>{

    public static final String DEBUG_TAG = "BasketRecyclerAdapter";
    protected List<Shopping> shoppingBasket;
    protected Context context;

    public BasketRecyclerAdapter(List<Shopping> shoppingBasket, Context context){
        this.shoppingBasket = shoppingBasket;
        this.context = context;
    }

    @NonNull
    @Override
    public BasketItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.shopping, parent, false );
        return new BasketItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketItemHolder holder, int position) {
        Shopping basketItem = shoppingBasket.get(position);

        Log.d( DEBUG_TAG, "onBindViewHolder: " + basketItem );

        String key = basketItem.getKey();
        String itemName = basketItem.getItemName();
        String category = basketItem.getCategory();
        int quantity = basketItem.getQuantity();
        double price = basketItem.getPrice();

        holder.itemName.setText(basketItem.getItemName());
        holder.category.setText(basketItem.getCategory());
        holder.quantity.setText("" + basketItem.getQuantity());
        holder.price.setText("" + basketItem.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditShoppingBasketDialogFragment editBasketFragment =
                        EditShoppingBasketDialogFragment.newInstance( holder.getAdapterPosition(), key, itemName, category, quantity, price );
                editBasketFragment.show( ((AppCompatActivity)context).getSupportFragmentManager(), null);
            }
        });

    }

    @Override
    public int getItemCount() { return shoppingBasket.size(); }

    class BasketItemHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView category;
        TextView quantity;
        TextView price;

        public BasketItemHolder(View itemView){
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            category = itemView.findViewById(R.id.category);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
        }
    }
}
