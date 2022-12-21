package com.example.gpstracker.UI;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gpstracker.UI.data.PostClient;
import com.example.gpstracker.UI.model.Post;

import java.util.List;
import java.util.concurrent.TimeUnit;

//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.annotations.NonNull;
//import io.reactivex.rxjava3.core.Observable;
//import io.reactivex.rxjava3.core.Observer;
//import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.Observable;
import io.reactivex.Observer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewModel extends ViewModel implements LocationListener {
    MutableLiveData<List<Post>> postsMutableLiveData= new MutableLiveData<>();
    MutableLiveData<String> posts=new MutableLiveData<>();
 MutableLiveData<String> email=new MutableLiveData<>();
 LiveData<String> _email=email;
 double latitude,longtiude;
    public void getPosts(){
        PostClient.getINSTANCE().getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("msg", String.valueOf(response.body()));
                postsMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                posts.setValue("errr");
            }
        });
    }
    public void setEmail(String Email){
        email.setValue("car33@gmail.com");
    }

    public void DeleteMessage(String email){
        Observable observable=PostClient.getINSTANCE().deletePost(email)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
        Observer <Post> observer=new Observer<Post>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Post post) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    public void getNearby(String str){
      /*  Observable observable=PostClient.getINSTANCE().getNearby()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());*/
        Observable observable=Observable.interval(10,TimeUnit.SECONDS)
                .flatMap(n->PostClient.getINSTANCE().getNearby(str))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observer<List<Post>> observer = new Observer<List<Post>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<Post> posts) {
          postsMutableLiveData.setValue(posts);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                      Log.d("Msg_Error","onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude=location.getLatitude();
        longtiude=location.getLongitude();
        Log.d("TAG_Location", String.valueOf(latitude));
        Log.d("TAG_Location",String.valueOf(longtiude));
    }

}
