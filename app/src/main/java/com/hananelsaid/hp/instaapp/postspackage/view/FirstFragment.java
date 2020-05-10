package com.hananelsaid.hp.instaapp.postspackage.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hananelsaid.hp.instaapp.CheckConnection;
import com.hananelsaid.hp.instaapp.R;
import com.hananelsaid.hp.instaapp.postspackage.model.Post;
import com.hananelsaid.hp.instaapp.postspackage.viewmodel.PostViewModel;
import com.hananelsaid.hp.instaapp.profilepackage.model.User;

import java.util.List;

public class FirstFragment extends Fragment {

    PostViewModel postViewModel;

    //rec
    private PostAdapter adapterClass;
    private RecyclerView postsRecycler;
    //firebase


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postsRecycler = view.findViewById(R.id.postsRecyclerView);
        //setAdapter();
       /* view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
        //adapterClass.notifyDataSetChanged();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAdapter();
        loadPosts();

    }

    private void loadPosts() {
        //postViewModel.getContext(getActivity());
        if (CheckConnection.isNetworkConnected(getActivity())) {
            Log.i("TAG", "Hi I am here: ");
            postViewModel.getDataFromFirebase().observe(getActivity(), new Observer<List<Post>>() {
                @Override
                public void onChanged(List<Post> posts) {
                    adapterClass.setData(posts);


                }
            });
            postViewModel.getUserDataFromFirebase().observe(getActivity(), new Observer<List<User>>() {
                @Override
                public void onChanged(List<User> users) {
                    adapterClass.setUserData(users);

                }
            });
        } else Toast.makeText(getActivity(), "Please check the internet connection", Toast.LENGTH_SHORT).show();
       /* else
            postViewModel.getDataFromRoom();*/
    }

    private void setAdapter() {

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        postsRecycler.setLayoutManager(manager);
        adapterClass = new PostAdapter();
        postsRecycler.setAdapter(adapterClass);

    }
}
