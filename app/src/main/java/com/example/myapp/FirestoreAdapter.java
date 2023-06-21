package com.example.myapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;


public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder, T> extends FirestoreRecyclerAdapter<T, VH> {

    private static final String TAG = "FirestoreAdapter";

    public FirestoreAdapter(@NonNull FirestoreRecyclerOptions<T> options) {
        super(options);
    }

    @NonNull
    @Override
    public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    protected abstract void onBindViewHolder(@NonNull VH holder, int position, @NonNull T model);

    @Override
    public void onDataChanged() {
        // Optional override
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
        Log.e(TAG, "onError: ", e);
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot snapshot, int position);
    }
}
