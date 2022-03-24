package com.example.socialmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Activity.CommentActivity;
import com.example.socialmedia.Model.Notification;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.NotificationSimpleLayoutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder> {

    Context context;
    ArrayList<Notification> list;

    public NotificationAdapter(Context context, ArrayList<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.notification_simple_layout,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Notification model=list.get(position);

        String type=model.getType();

        holder.binding.notificationTime.setText(model.getNotificationAt()+"");


        FirebaseDatabase.getInstance().getReference()
                .child("user")
                .child(model.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists())
                        {
                            Users users=snapshot.getValue(Users.class);


                            Picasso.get().load(users.getProfile_pic())
                                    .placeholder(R.drawable.image_gallery)
                                    .into(holder.binding.notificationProfile);

                            if (type.equals("like"))
                            {

                                holder.binding.notificationName.setText(Html.fromHtml("<b>"+users.getName()+"</b>"+" liked your post"));

                            }else if (type.equals("comment"))
                            {

                                holder.binding.notificationName.setText(Html.fromHtml( "<b>"+users.getName()+"</b"+" commented on your post"));

                            }else
                            {

                                holder.binding.notificationName.setText(Html.fromHtml( "<b>"+users.getName()+"</b"+" start following you"));

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.binding.notificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!type.equals("follow") && !type.equals("like"))
                {

                    FirebaseDatabase.getInstance().getReference()
                            .child("notification")
                            .child(model.getPostedBy())
                            .child(model.getNotificationId())
                            .child("checkOpen")
                            .setValue(true);

                    holder.binding.notificationLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Intent intent =new Intent(context, CommentActivity.class);
                    intent.putExtra("postedBy",model.getPostedBy());
                    intent.putExtra("postId",model.getPostId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }



            }
        });
        Boolean checkOpen= model.isCheckOpen();

        if (checkOpen==true)
        {
            holder.binding.notificationLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

        }else {

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        NotificationSimpleLayoutBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding=NotificationSimpleLayoutBinding.bind(itemView);



        }
    }
}
