package com.jongewaard.dev.storiesprogressview;

import com.jongewaard.dev.storiesprogressview.Utils.Movie;

import java.util.List;

public interface IFirebaseLoadDone {

    void onFirebaseLoadSuccess(List<Movie> movieList);

    void onFirebaseLoadFalied(String message);

}
