package com.example.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Model.Story;
import com.example.socialmedia.Model.UserStories;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.StoryRvDesignBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.viewHolder> {

    ArrayList<Story> storyList;
    Context context;

    public StoryAdapter(ArrayList<Story> storyList, Context context) {
        this.storyList = storyList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.story_rv_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Story story = storyList.get(position);

        if (story.getArrayList().size()>0) {

            UserStories lastStories = story.getArrayList().get(story.getArrayList().size() - 1);

            Picasso.get().load(lastStories.getImage())
                    .placeholder(R.drawable.image_gallery)
                    .into(holder.binding.roundedImageView);

            FirebaseDatabase.getInstance().getReference()
                    .child("user")
                    .child(story.getStoryBy())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                Users users = snapshot.getValue(Users.class);

                                holder.binding.username.setText(users.getName());
                                Picasso.get().load(users.getProfile_pic())
                                        .placeholder(R.drawable.image_gallery)
                                        .into(holder.binding.pProfile);

                                holder.binding.roundedImageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    /*ArrayList<MyStory> myStories = new ArrayList<>();

                                    for(Story story: data){
                                        myStories.add(new MyStory(
                                                story.getImageUrl(),
                                                story.getDate()
                                        ));
                                    }*/

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
        }
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        StoryRvDesignBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding=StoryRvDesignBinding.bind(itemView);
        }
    }
}
