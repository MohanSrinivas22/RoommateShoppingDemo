package edu.uga.cs.roommateshoppingdemo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewShoppingBasketActivity extends AppCompatActivity {

    public static final String DEBUG_TAG = "ReviewShoppingBasketActivity";

    protected RecyclerView basketRecyclerView;
    protected List<Shopping> shoppingBasket;
    protected BasketRecyclerAdapter basketRecyclerAdapter;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_shopping_basket);

        basketRecyclerView = findViewById(R.id.basketRecyclerView);
        Button backButton = findViewById(R.id.back);
        Button checkoutButton = findViewById(R.id.checkout);

        shoppingBasket = new ArrayList<Shopping>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        basketRecyclerView.setLayoutManager(layoutManager);

        basketRecyclerAdapter = new BasketRecyclerAdapter(shoppingBasket, ReviewShoppingBasketActivity.this);
        basketRecyclerView.setAdapter(basketRecyclerAdapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppingBasket");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shoppingBasket.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Shopping basketItem = postSnapshot.getValue(Shopping.class);
                    basketItem.setKey(postSnapshot.getKey());
                    shoppingBasket.add(basketItem);

                    Log.d( DEBUG_TAG, "ValueEventListener: added: " + basketItem );
                    Log.d( DEBUG_TAG, "ValueEventListener: key: " + postSnapshot.getKey() );
                }
                Log.d( DEBUG_TAG, "ValueEventListener: notifying basketRecyclerAdapter" );
                basketRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println( "ValueEventListener: reading failed: " + error.getMessage() );
            }
        });

    }

    public void updateShoppingBasket(int position, Shopping basketItem, int action){
        if( action == EditShoppingItemDialogFragment.SAVE ) {
            Log.d(DEBUG_TAG, "Updating shopping item at: " + position + "(" + basketItem.getItemName() + ")");
            basketRecyclerAdapter.notifyItemChanged(position);
            DatabaseReference ref = database
                    .getReference()
                    .child("shoppingBasket")
                    .child(basketItem.getKey());

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().setValue(basketItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(DEBUG_TAG, "updated shopping item at: " + position + "(" + basketItem.getItemName() + ")");
                            Toast.makeText(getApplicationContext(), "shopping item updated for " + basketItem.getItemName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(DEBUG_TAG, "failed to update shopping item at: " + position + "(" + basketItem.getItemName() + ")");
                    Toast.makeText(getApplicationContext(), "Failed to update " + basketItem.getItemName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if( action == EditShoppingItemDialogFragment.DELETE ) {
            Log.d( DEBUG_TAG, "Removed item from shopping basket at: " + position + "(" + basketItem.getItemName() + ")" );
            shoppingBasket.remove( position );
            basketRecyclerAdapter.notifyItemRemoved( position );
            DatabaseReference ref = database
                    .getReference()
                    .child( "shoppingItems" )
                    .child( basketItem.getKey() );
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d( DEBUG_TAG, "Removed item from shopping basket at: " + position + "(" + basketItem.getItemName() + ")" );
                            Toast.makeText(getApplicationContext(), "Basket item deleted for " + basketItem.getItemName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d( DEBUG_TAG, "failed to remove item from shopping basket at: " + position + "(" + basketItem.getItemName() + ")" );
                    Toast.makeText(getApplicationContext(), "Failed to delete " + basketItem.getItemName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}