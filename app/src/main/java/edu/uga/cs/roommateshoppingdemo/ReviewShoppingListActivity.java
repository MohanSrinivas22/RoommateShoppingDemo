package edu.uga.cs.roommateshoppingdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewShoppingListActivity extends AppCompatActivity
            implements AddShoppingItemDialogFragment.AddShoppingItemDialogListener,
                       EditShoppingItemDialogFragment.EditShoppingItemDialogListener {

    public static final String DEBUG_TAG = "ReviewShoppingListActivity";

    protected RecyclerView recyclerView;
    protected RecyclerView basketRecyclerView;
    protected List<Shopping> shoppingList;
    protected List<Shopping> basketList;
    protected ShoppingRecyclerAdapter recyclerAdapter;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d( DEBUG_TAG, "ReviewShoppingList.onCreate()" );
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_review_shopping_list);

        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new AddShoppingItemDialogFragment();
                newFragment.show(getSupportFragmentManager(), null);
            }
        });
        // initialize the Shopping Items List.
        shoppingList = new ArrayList<Shopping>();

        // use a linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // the recycler adapter with shopping items is empty at first; it will be updated later
        recyclerAdapter = new ShoppingRecyclerAdapter( shoppingList, ReviewShoppingListActivity.this );
        recyclerView.setAdapter( recyclerAdapter );

        // get a Firebase DB instance reference
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppingItems");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shoppingList.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Shopping shoppingItem = postSnapshot.getValue(Shopping.class);
                    shoppingItem.setKey(postSnapshot.getKey());
                    shoppingList.add(shoppingItem);

                    Log.d( DEBUG_TAG, "ValueEventListener: added: " + shoppingItem );
                    Log.d( DEBUG_TAG, "ValueEventListener: key: " + postSnapshot.getKey() );
                }

                Log.d( DEBUG_TAG, "ValueEventListener: notifying recyclerAdapter" );
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println( "ValueEventListener: reading failed: " + error.getMessage() );
            }
        });
    }

    public void addShoppingItem(Shopping shoppingItem){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppingItems");
        myRef.push().setValue(shoppingItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(shoppingList.size()-1);
                            }
                        });
                        Log.d( DEBUG_TAG, "Shopping Item saved: " + shoppingItem );
                        Toast.makeText(getApplicationContext(), "Shopping Item created for " + shoppingItem.getItemName(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create a shopping item for " + shoppingItem.getItemName(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateShoppingItem(int position, Shopping shoppingItem, int action){
        if( action == EditShoppingItemDialogFragment.SAVE ) {
            Log.d(DEBUG_TAG, "Updating shopping item at: " + position + "(" + shoppingItem.getItemName() + ")");
            recyclerAdapter.notifyItemChanged(position);
            DatabaseReference ref = database
                    .getReference()
                    .child("shoppingItems")
                    .child(shoppingItem.getKey());

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().setValue(shoppingItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(DEBUG_TAG, "updated shopping item at: " + position + "(" + shoppingItem.getItemName() + ")");
                            Toast.makeText(getApplicationContext(), "shopping item updated for " + shoppingItem.getItemName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(DEBUG_TAG, "failed to update shopping item at: " + position + "(" + shoppingItem.getItemName() + ")");
                    Toast.makeText(getApplicationContext(), "Failed to update " + shoppingItem.getItemName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if( action == EditShoppingItemDialogFragment.DELETE ) {
            Log.d( DEBUG_TAG, "Deleting shopping item at: " + position + "(" + shoppingItem.getItemName() + ")" );
            shoppingList.remove( position );
            recyclerAdapter.notifyItemRemoved( position );
            DatabaseReference ref = database
                    .getReference()
                    .child( "shoppingItems" )
                    .child( shoppingItem.getKey() );
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d( DEBUG_TAG, "deleted shopping item at: " + position + "(" + shoppingItem.getItemName() + ")" );
                            Toast.makeText(getApplicationContext(), "Shopping item deleted for " + shoppingItem.getItemName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d( DEBUG_TAG, "failed to delete shopping item at: " + position + "(" + shoppingItem.getItemName() + ")" );
                    Toast.makeText(getApplicationContext(), "Failed to delete " + shoppingItem.getItemName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        else if(action == EditShoppingItemDialogFragment.ADD) {
            Log.d( DEBUG_TAG, "Adding Shopping Item at: " + position + "(" + shoppingItem.getItemName() + ") to purchase list." );
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("shoppingBasket");
            myRef.push().setValue(shoppingItem)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            basketRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    basketRecyclerView.smoothScrollToPosition(basketList.size()-1);
                                }
                            });
                            Log.d( DEBUG_TAG, "Shopping Item added to basket: " + shoppingItem );
                            Toast.makeText(getApplicationContext(), "Added to basket for " + shoppingItem.getItemName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to create a shopping item for " + shoppingItem.getItemName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

}