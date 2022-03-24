package com.example.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Model.FollowModel;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.FirendRvSimpleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollwerAdapter extends RecyclerView.Adapter<FollwerAdapter.viewHolder>{

    Context context;
    ArrayList<FollowModel> list;

    public FollwerAdapter(Context context, ArrayList<FollowModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.firend_rv_simple,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        FollowModel model=list.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child(model.getFollowedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users=snapshot.getValue(Users.class);

                Picasso.get().load(users.getProfile_pic()).placeholder(R.drawable.profile).into(holder.binding.fProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        FirendRvSimpleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding=FirendRvSimpleBinding.bind(itemView);
        }
    }
}
