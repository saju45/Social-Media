package com.example.socialmedia.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.socialmedia.Model.Post;
import com.example.socialmedia.Model.PostModel;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.FragmentAdBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;


public class AddFragment extends Fragment {

    FragmentAdBinding binding;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    StorageReference storageReference;
    FirebaseAuth auth;
    ProgressDialog dialog;
    String postedBy;
    int IMAGE_REQUEST_CODE = 101;
    private Uri imageUri;

    String time;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;


    public AddFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdBinding.inflate(inflater, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        calendar= Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");
        time=simpleDateFormat.format(calendar.getTime());

        postedBy=auth.getUid();
        dialog=new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        storageReference=FirebaseStorage.getInstance().getReference("posts");

        databaseReference.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);

                    binding.name.setText(users.getName());
                    binding.simpleRvmessage.setText(users.getProfession());
                    Picasso.get().load(users.getProfile_pic()).placeholder(R.drawable.image_gallery).into(binding.profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.addPostDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String description = binding.addPostDescription.getText().toString();

                if (!description.isEmpty()) {
                    binding.postBtn.setEnabled(true);
                    binding.postBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.follow_btn));
                    binding.postBtn.setTextColor(getContext().getResources().getColor(R.color.white));
                } else {
                    binding.postBtn.setEnabled(false);
                    binding.postBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.follow_bg_btn));
                    binding.postBtn.setTextColor(getContext().getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.galleryimgId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });

        binding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postType="";
                String postText=binding.addPostDescription.getText().toString();
                if(postText.isEmpty() && imageUri==null){
                    Toast.makeText(getContext(), "At Least Image Or Text Need To Add", Toast.LENGTH_SHORT).show();
                }else{
                    if(postText.isEmpty() && imageUri!=null){
                        postType=PostModel.POST_TYPE_IMAGE;
                    }else if(!postText.isEmpty() && imageUri==null){
                        postType=PostModel.POST_TYPE_TEXT;
                    }else if(!postText.isEmpty() && imageUri!=null){
                        postType=PostModel.POST_TYPE_IMAGEANDTEXT;
                    }
                    saveData(postType);
                }

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            binding.postImg.setVisibility(View.VISIBLE);
            binding.postImg.setImageURI(imageUri);

            binding.postBtn.setEnabled(true);
            binding.postBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.follow_btn));
            binding.postBtn.setTextColor(getContext().getResources().getColor(R.color.white));

        }


    }
    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver=getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    public void saveData(String postType){

        String messsage=binding.addPostDescription.getText().toString();
        String postId=databaseReference.push().getKey();
        if(postType.equals(PostModel.POST_TYPE_TEXT)){
            PostModel postModel = new PostModel(postId,"none",postedBy,messsage,time);
            databaseReference.child(postId).setValue(postModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "Post Upload Successful.", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getContext(), "Post Upload Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            StorageReference ref=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(getActivity(), "successfully post", Toast.LENGTH_LONG).show();

                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri loadUri=uriTask.getResult();

                    //String postMessage=messsage.isEmpty()?messsage:"";
                   String postMessage=binding.addPostDescription.getText().toString();


                    PostModel postModel = new PostModel(postId,loadUri.toString(),postedBy,postMessage,time);

                    databaseReference.child(postId).setValue(postModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Post Upload Successful.", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getContext(), "Post Upload Failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }


    }
}