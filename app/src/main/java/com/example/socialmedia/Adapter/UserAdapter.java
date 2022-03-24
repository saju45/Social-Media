package com.example.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Model.FollowModel;
import com.example.socialmedia.Model.Notification;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {


    Context context;
    List<Users> usersList;

    public UserAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.simple_recyclerview,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder,int position) {

        Users users=usersList.get(position);

        holder.name.setText(users.getName());
        holder.message.setText(users.getProfession());
        Picasso.get().load(users.getProfile_pic()).placeholder(R.drawable.profile).into(holder.proifle);


        FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child(users.getUserId())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    holder.follow.setBackground(ContextCompat.getDrawable(context,R.drawable.follow_bg_btn));
                    holder.follow.setText("following");
                    holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
                    holder.follow.setEnabled(false);
                }
                else {
                    holder.follow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FollowModel followModel=new FollowModel();
                            followModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            followModel.setFollowedArt(new Date().getTime());

                            FirebaseDatabase.getInstance().getReference()
                                    .child("user")
                                    .child(users.getUserId())
                                    .child("followers")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(followModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("user")
                                            .child(users.getUserId())
                                            .child("followerCount")
                                            .setValue(users.getFollowerCount()+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            holder.follow.setBackground(ContextCompat.getDrawable(context,R.drawable.follow_bg_btn));
                                            holder.follow.setText("following");
                                            holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
                                            holder.follow.setEnabled(false);
                                            Toast.makeText(context, "You followed "+users.getName(), Toast.LENGTH_SHORT).show();

                                            Notification notification=new Notification();
                                            notification.setNotificationAt(new Date().getTime());
                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            notification.setType("follow");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(users.getUserId())
                                                    .push()
                                                    .setValue(notification);
                                        }
                                    });


                                }
                            });

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView proifle;
        TextView name,message;
        Button follow;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            proifle=itemView.findViewById(R.id.simple_rvprofile);
            name=itemView.findViewById(R.id.simple_rvname);
            message=itemView.findViewById(R.id.simple_rvmessage);
            follow=itemView.findViewById(R.id.followBtn);

        }
    }
}
