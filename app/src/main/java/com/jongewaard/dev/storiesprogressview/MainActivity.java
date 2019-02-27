package com.jongewaard.dev.storiesprogressview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jongewaard.dev.storiesprogressview.Utils.Movie;

import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class MainActivity extends AppCompatActivity implements IFirebaseLoadDone {

    StoriesProgressView mStoriesProgressView;
    ImageView mImageView;

    Button btn_load,btn_pause, btn_resume, btn_reverse;

    int counter = 0;

    DatabaseReference dbRef;
    IFirebaseLoadDone mIFirebaseLoadDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Movies");
        //Init interface
        mIFirebaseLoadDone = this;

        //View
        btn_load = (Button)findViewById(R.id.btn_load);
        btn_pause = (Button)findViewById(R.id.btn_pause);
        btn_resume = (Button)findViewById(R.id.btn_resume);
        btn_reverse = (Button)findViewById(R.id.btn_reverse);

        mStoriesProgressView = (StoriesProgressView)findViewById(R.id.stories);
        mStoriesProgressView.setStoryDuration(1000L);
        mImageView = (ImageView)findViewById(R.id.image);
    }

    @Override
    public void onFirebaseLoadSuccess(List<Movie> movieList) {

    }

    @Override
    public void onFirebaseLoadFalied(String message) {

    }
}
