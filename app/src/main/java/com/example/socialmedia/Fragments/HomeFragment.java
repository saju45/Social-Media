package com.example.socialmedia.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmedia.Adapter.PostAdapter;
import com.example.socialmedia.Adapter.StoryAdapter;
import com.example.socialmedia.Model.PostModel;
import com.example.socialmedia.Model.Story;
import com.example.socialmedia.Model.UserStories;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {

    RecyclerView recyclerViewrv,dashboardRecyclerview;
    ArrayList<Story> list;
    ArrayList<PostModel> postArrayList;
    PostAdapter postAdapter;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FragmentHomeBinding binding;
    ActivityResultLauncher<String> galleryLauncher;

    ProgressDialog dialog;

    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentHomeBinding.inflate(inflater, container, false);

       database=FirebaseDatabase.getInstance();
       storage=FirebaseStorage.getInstance();
       auth=FirebaseAuth.getInstance();
       dialog=new ProgressDialog(getContext());
       dialog.setTitle("Story Uploading");
       dialog.setMessage("Please wait...");
       dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       dialog.setCancelable(false);

       list=new ArrayList<>();
       postArrayList =new ArrayList<>();

       dayStory();

        postAdapter =new PostAdapter(getContext(), postArrayList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.dashboardRecylerview.setLayoutManager(layoutManager);
        binding.dashboardRecylerview.setHasFixedSize(true);
        binding.dashboardRecylerview.setNestedScrollingEnabled(false);
        binding.dashboardRecylerview.setAdapter(postAdapter);

       database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               postArrayList.clear();
               for (DataSnapshot snapshot1: snapshot.getChildren())
               {
                   PostModel postModel=snapshot1.getValue(PostModel.class);

                   postArrayList.add(postModel);
               }
               postAdapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                galleryLauncher.launch("image/*");

            }
        });
        galleryLauncher=registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        dialog.show();
                        binding.roundedImageView.setImageURI(result);

                        final StorageReference storageReference=storage.getReference()
                                .child("stories")
                                .child(auth.getUid())
                                .child(new Date().getTime()+"");

                        storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        Story story=new Story();

                                        story.setStoryAt(new Date().getTime());
                                        story.setStoryBy(auth.getUid());
                                        database.getReference()
                                                .child("stories")
                                                .child(auth.getUid())
                                                .child("postedBy")
                                                .setValue(story.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                UserStories userStories=new UserStories();

                                                userStories.setStoryAt(story.getStoryAt());
                                                userStories.setImage(uri.toString());

                                                database.getReference()
                                                        .child("stories")
                                                        .child(auth.getUid())
                                                        .child("userStories")
                                                        .push()
                                                        .setValue(userStories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        dialog.dismiss();

                                                    }
                                                });

                                            }
                                        });
                                    }
                                });

                            }
                        });
                    }
                });

       return binding.getRoot();
    }

    public void dayStory()
    {
      /*  list.add(new Story(R.drawable.programmer,R.drawable.live,R.drawable.programmer,"Android"));
        list.add(new Story(R.drawable.programmer2,R.drawable.live,R.drawable.programmer2,"Web"));
        list.add(new Story(R.drawable.programmer3,R.drawable.live,R.drawable.programmer3,"iOs"));
        list.add(new Story(R.drawable.programmer4,R.drawable.live,R.drawable.programmer4,"programmer"));
        list.add(new Story(R.drawable.saju,R.drawable.live,R.drawable.saju,"saju"));
        list.add(new Story(R.drawable.programmer,R.drawable.live,R.drawable.programmer,"Android"));
        list.add(new Story(R.drawable.programmer2,R.drawable.live,R.drawable.programmer2,"Web"));
        list.add(new Story(R.drawable.programmer3,R.drawable.live,R.drawable.programmer3,"iOs"));
        list.add(new Story(R.drawable.programmer4,R.drawable.live,R.drawable.programmer4,"programmer"));
        list.add(new Story(R.drawable.saju,R.drawable.live,R.drawable.saju,"saju"));*/

        StoryAdapter storyAdapter=new StoryAdapter(list,getContext());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.storyRecycler.setLayoutManager(layoutManager);
        binding.storyRecycler.setNestedScrollingEnabled(false);
        binding.storyRecycler.setAdapter(storyAdapter);


        database.getReference()
                .child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Story story=new Story();
                    story.setStoryBy(snapshot1.getKey());
                    story.setStoryAt(snapshot1.child("postedBy").getValue(Long.class));

                    ArrayList<UserStories> stories=new ArrayList<>();

                    for (DataSnapshot snapshot2: snapshot1.child("userStories").getChildren())
                    {
                        UserStories userStories=snapshot2.getValue(UserStories.class);

                        stories.add(userStories);
                    }
                    story.setArrayList(stories);
                    list.add(story);
                }
                storyAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}