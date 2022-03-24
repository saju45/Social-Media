package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.socialmedia.Adapter.CommentAdapter;
import com.example.socialmedia.Model.Comments;
import com.example.socialmedia.Model.Notification;
import com.example.socialmedia.Model.PostModel;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.ActivityCommentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    Intent intent;
    String postId;
    String postedBy;

    FirebaseDatabase database;
    FirebaseAuth auth;

    ArrayList<Comments> commentsArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.tollBar);
        CommentActivity.this.setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        commentsArrayList=new ArrayList<>();

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        intent=getIntent();
       postedBy=intent.getStringExtra("postedBy");
       postId=intent.getStringExtra("postId");


       database.getReference().child("posts").child(postId).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

                   PostModel postModel=snapshot.getValue(PostModel.class);

                  binding.liketext.setText(postModel.getPostLike()+"");
                  binding.commentText.setText(postModel.getCommentCount()+"");
                   if (postModel.getPostImg().equals("none"))
                   {
                      binding.postImage.setVisibility(View.GONE);
                      binding.postText.setText(""+postModel.getPostDescription());
                   }else {
                       if (postModel.getPostImg().equals("") || postModel.getPostImg().equals("none"))
                       {
                          binding.postImage.setVisibility(View.GONE);
                       }else {
                           Picasso.get().load(postModel.getPostImg()).placeholder(R.drawable.saju).into(binding.postImage);
                       }
                       binding.postText.setText(postModel.getPostDescription());
                   }

                   database.getReference().child("user").child(postedBy)
                           .addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {

                                   Users users=snapshot.getValue(Users.class);

                                   binding.name.setText(users.getName());

                                   Picasso.get().load(users.getProfile_pic())
                                           .placeholder(R.drawable.image_gallery)
                                           .into(binding.profileImage);

                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });



           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Comments comments=new Comments();
                String comment=binding.commentEditText.getText().toString();

                comments.setCommentBody(comment);
                comments.setCommentedAt(new Date().getTime());
                comments.setCommentedBy(auth.getUid());


                database.getReference().child("posts")
                        .child(postId)
                        .child("comments")
                        .push()
                        .setValue(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        database.getReference().child("posts")
                                .child(postId)
                                .child("commentCount").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int commentCount=0;
                                if (snapshot.exists())
                                {
                                    commentCount=snapshot.getValue(Integer.class);



                                }
                                database.getReference().child("posts")
                                        .child(postId)
                                        .child("commentCount")
                                        .setValue(commentCount+1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(getApplicationContext(), "Commented", Toast.LENGTH_SHORT).show();
                                        binding.commentEditText.setText("");

                                        Notification notification=new Notification();

                                        notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                        notification.setNotificationAt(new Date().getTime());
                                        notification.setPostId(postId);
                                        notification.setPostedBy(postedBy);
                                        notification.setType("comment");

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("notification")
                                                .child(postedBy)
                                                .push()
                                                .setValue(notification);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
            }
        });

        CommentAdapter adapter=new CommentAdapter(this,commentsArrayList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.commentRecyclerview.setLayoutManager(layoutManager);
        binding.commentRecyclerview.setAdapter(adapter);

        database.getReference()
                .child("posts")
                .child(postId)
                .child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentsArrayList.clear();
                        for (DataSnapshot snapshot1: snapshot.getChildren())
                        {
                            Comments comments=snapshot1.getValue(Comments.class);

                            commentsArrayList.add(comments);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}