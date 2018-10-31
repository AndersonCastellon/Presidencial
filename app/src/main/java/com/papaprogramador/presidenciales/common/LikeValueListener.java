package com.papaprogramador.presidenciales.common;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class LikeValueListener implements ValueEventListener {
    private final DatabaseReference reference;


    public LikeValueListener(DatabaseReference reference, String opinionId) {
        this.reference = reference;
        reference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        try {
            List<String> userLikes = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String userLike = snapshot.getValue(String.class);
                userLikes.add(userLike);
            }
            onDataChange(userLikes);
        } catch (DatabaseException ignored) {
        }
        reference.removeEventListener(this);

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    protected abstract void onDataChange(List<String> userLikes);
}
