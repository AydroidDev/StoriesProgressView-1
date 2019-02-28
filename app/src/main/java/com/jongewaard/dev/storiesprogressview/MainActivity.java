package com.jongewaard.dev.storiesprogressview;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jongewaard.dev.storiesprogressview.Utils.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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


        mImageView = (ImageView)findViewById(R.id.image);

        //Event
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStoriesProgressView.skip();

            }
        });

        btn_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStoriesProgressView.reverse();
            }
        });

        btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStoriesProgressView.resume();
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStoriesProgressView.pause();
            }
        });

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Movie> movies = new ArrayList<>();
                        for(DataSnapshot itemSnapShot: dataSnapshot.getChildren()){

                            Movie movie = itemSnapShot.getValue(Movie.class);
                            movies.add(movie);
                        }

                        mIFirebaseLoadDone.onFirebaseLoadSuccess(movies);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mIFirebaseLoadDone.onFirebaseLoadFalied(databaseError.getMessage());

                    }
                });
            }
        });


    }

    @Override
    public void onFirebaseLoadSuccess(final List<Movie> movieList) {
        mStoriesProgressView.setStoriesCount(movieList.size());
        mStoriesProgressView.setStoryDuration(1000L); //1 sec

        //Load first image
        Picasso.get().load(movieList.get(0).getImage()).into(mImageView, new Callback() {
            @Override
            public void onSuccess() {
                mStoriesProgressView.startStories();
            }

            @Override
            public void onError(Exception e) {

            }
        });

        //Set event Stories
        mStoriesProgressView.setStoriesListener(new StoriesProgressView.StoriesListener() {
            @Override
            public void onNext() {
                //Display next image
                if(counter < movieList.size()){
                    counter++;
                    Picasso.get().load(movieList.get(counter).getImage()).into(mImageView);
                }
            }

            @Override
            public void onPrev() {

                if(counter > 0){
                    counter--;
                    Picasso.get().load(movieList.get(counter).getImage()).into(mImageView);
                }

            }

            @Override
            public void onComplete() {
                //Reset counter
                counter = 0;
                Toast.makeText(MainActivity.this, "Load done !!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onFirebaseLoadFalied(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}
