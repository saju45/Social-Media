package com.example.socialmedia.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmedia.Adapter.FollwerAdapter;
import com.example.socialmedia.Model.FollowModel;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {



    public ProfileFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    ArrayList<FollowModel> list;
    ImageView galleryIMg;
    int IMAGE_REQUEST=101;
    Uri imageUri;
    ImageView coverImg,profile;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    TextView name,about,followersCount,friend_count,photos_count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.fragment_profile, container, false);

       list=new ArrayList<>();
       recyclerView=view.findViewById(R.id.profile_Recylerview);
       galleryIMg=view.findViewById(R.id.galleryImg);
       coverImg=view.findViewById(R.id.coverImg);
       profile=view.findViewById(R.id.pProfile);
       name=view.findViewById(R.id.nameTextView);
       about=view.findViewById(R.id.pabout);
       followersCount=view.findViewById(R.id.follower_count);
       friend_count=view.findViewById(R.id.friend_cound);
       photos_count=view.findViewById(R.id.photos_count);


       auth=FirebaseAuth.getInstance();
       storage=FirebaseStorage.getInstance();
       database=FirebaseDatabase.getInstance();



       database.getReference().child("user").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               list.clear();
               if (snapshot.exists())
               {
                   Users users=snapshot.getValue(Users.class);


                   Picasso.get().load(users.getCoverPhoto()).placeholder(R.drawable.image_gallery).into(coverImg);
                   Picasso.get().load(users.getProfile_pic()).placeholder(R.drawable.image_gallery).into(profile);
                   name.setText(users.getName());
                   about.setText(users.getProfession());
                   followersCount.setText(users.getFollowerCount()+"");

               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

       profile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent=new Intent();
               intent.setAction(Intent.ACTION_GET_CONTENT);
               intent.setType("image/*");
               startActivityForResult(intent,11);
           }
       });

        galleryIMg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_REQUEST);


            }
        });
        FollwerAdapter adapter=new FollwerAdapter(getContext(),list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(layoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        database.getReference().child("user")
                .child(auth.getUid())
                .child("followers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                       for (DataSnapshot snapshot1: snapshot.getChildren())
                       {
                             FollowModel followModel=snapshot1.getValue(FollowModel.class);


                             list.add(followModel);


                       }
                       adapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==IMAGE_REQUEST &&  resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri=data.getData();
            coverImg.setImageURI(imageUri);


            final StorageReference  storageReference=storage.getReference().child("cover_image")
                    .child(FirebaseAuth.getInstance().getUid()+System.currentTimeMillis());

            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getContext(), "cover photo saved", Toast.LENGTH_SHORT).show();

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            database.getReference().child("user").child(auth.getUid()).child("coverPhoto").setValue(uri.toString());
                        }
                    });

                }
            });

        }
        if (requestCode==11 &&  resultCode==RESULT_OK && data!=null && data.getData()!=null)

        {

            imageUri=data.getData();
            profile.setImageURI(imageUri);


            final StorageReference reference=storage.
                    getReference().child("profile_pic").child(FirebaseAuth.getInstance().getUid()+System.currentTimeMillis());

            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getActivity(), "profile pic saved", Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            database.getReference().child("user").child(auth.getUid()).child("profile_pic").setValue(uri.toString());
                        }
                    });

                }
            });



        }



    }


}