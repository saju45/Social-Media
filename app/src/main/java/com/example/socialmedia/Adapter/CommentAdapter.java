package com.example.socialmedia.Adapter;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Model.Comments;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.SimpleCommentBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.viewholder> {

  Context context;
    ArrayList<Comments> commentsArrayList;

    public CommentAdapter(Context context, ArrayList<Comments> commentsArrayList) {
        this.context = context;
        this.commentsArrayList = commentsArrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.simple_comment,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Comments comments= commentsArrayList.get(position);

       // String time = TimeAgo.using(comments.getCommentedAt());
       holder.binding.commentBody.setText(comments.getCommentedAt()+"");

        FirebaseDatabase.getInstance().getReference("user")
                .child(comments.getCommentedBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists())
                        {
                            Users users=snapshot.getValue(Users.class);
                            Picasso.get().load(users.getProfile_pic()).placeholder(R.drawable.image_gallery).into(holder.binding.profileImage);

                            holder.binding.username.setText(Html.fromHtml("<b>"+users.getName()+"</b>"+"  "+ comments.getCommentBody()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return commentsArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        SimpleCommentBinding binding;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            binding=SimpleCommentBinding.bind(itemView);
        }
    }

    //try using timeAgo
    public void calculateTimeAgo(String datePost){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long time = sdf.parse("2016-01-24T16:00:00.000Z").getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
