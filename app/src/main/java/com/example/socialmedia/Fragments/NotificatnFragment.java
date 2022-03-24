package com.example.socialmedia.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.socialmedia.Adapter.NotificationAdapter;
import com.example.socialmedia.Model.Notification;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class NotificatnFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Notification> modelList;
    FirebaseDatabase database;

    public NotificatnFragment() {
        // Required empty public constructor
    }

;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_notificatn, container, false);



       modelList =new ArrayList<>();
       database=FirebaseDatabase.getInstance();
       recyclerView=view.findViewById(R.id.notification_recyclerView);


     /*  modelList.add(new Notification(R.drawable.programmer,"<b>Shanawaj hossain</b>  Mention you in a comment in nice","just now"));
       modelList.add(new Notification(R.drawable.programmer2,"<b>programmer </b> like your photo","just now"));
       modelList.add(new Notification(R.drawable.programmer3,"<b>Programmer1</b> comment your profile pic","just now"));
       modelList.add(new Notification(R.drawable.programmer4,"<b>Programmer2</b> Mention you in a comment in nice","just now"));
       modelList.add(new Notification(R.drawable.profile,"<b>Programmer3</b> Like your profile picture","just now"));
       modelList.add(new Notification(R.drawable.rounded,"<b>Programmer4</b> Mention you in a comment group of psd","just now"));
       modelList.add(new Notification(R.drawable.programmer,"<b>Programmer5</b> like your photo","just now"));
       modelList.add(new Notification(R.drawable.programmer2,"<b>Programmer6</b> comment your cover pic ","just now"));
       modelList.add(new Notification(R.drawable.programmer3,"<b>Programmer7</b love react in your profile picture ","just now"));
       modelList.add(new Notification(R.drawable.programmer4,"<b>Programmer8</b> ","just now"));
       modelList.add(new Notification(R.drawable.programmer,"<b>Programmer9</b ","just now"));
*/
        NotificationAdapter adapter=new NotificationAdapter(getContext(),modelList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        database.getReference()
                .child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                 .addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {

                         modelList.clear();
                         for (DataSnapshot snapshot1:snapshot.getChildren())
                         {
                             Notification notification=snapshot1.getValue(Notification.class);

                             notification.setNotificationId(snapshot1.getKey());
                             modelList.add(notification);
                         }
                         adapter.notifyDataSetChanged();
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });
       return view;

    }
}