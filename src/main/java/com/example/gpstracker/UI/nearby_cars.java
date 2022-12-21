package com.example.gpstracker.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.gpstracker.UI.data.PostClient;
import com.example.gpstracker.UI.model.Post;
import com.example.gpstracker.UI.model.dist;
import com.example.gpstracker.UI.model.pointSchema;
import com.example.gpstracker.databinding.ActivityNearbyCarsBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.gpstracker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class nearby_cars extends AppCompatActivity implements LocationListener{
    private AppBarConfiguration appBarConfiguration;
    private ActivityNearbyCarsBinding binding;
    LocationManager locationManger;
    PostViewModel postViewModel;
    public Post post;
    public dist d;
    public pointSchema pointschema;
    PostsAdapter adapter=  new PostsAdapter();
    public Post post1;
    public pointSchema pointschema1;
    public double[] arr1;

    // TextView view1,view2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNearbyCarsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
       String  str = intent.getStringExtra("message_key");
        //----------------------------------------------------------CreatePost1-----------------------------------------------------------------------//
        arr1=new double[2];
        arr1[0]=-1;
        arr1[1]=-1;
        pointschema1=new pointSchema();
        pointschema1.setCoordinates(arr1);
        pointschema1.setType("Point");
        post1=new Post(str,pointschema1);
        //-------------------------------------------------------------------------------------------------------------------------------------//
        double []arr=new double[2];
        d=new dist();
        d.setCalculated(1.0);
        arr[0]=-122.0;
        arr[1]=-99.0;
        pointschema=new pointSchema();
        pointschema.setType("Point");
        pointschema.setCoordinates(arr);
        post=new Post("shroukGamal",pointschema);
        post.setCalculated(d);
        ArrayList<Post> list=new ArrayList<>();
      //  list.add(post);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        //postViewModel.getPosts();
//----------------------------------------------------------LocationManager--------------------------------------------------------------------//
        if (ActivityCompat.checkSelfPermission(nearby_cars.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(nearby_cars.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
       locationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);



//------------------------------------------------------------UpdateViewModel--------------------------------------------------------------//
   patchPost();
    postViewModel.getNearby(str);
//----------------------------------------------------------Adapter----------------------------------------------------------------------------//
        RecyclerView recyclerView = findViewById(R.id.recycler);
        TextView Nodata=findViewById(R.id.Nodata);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PostsAdapter adapter=  new PostsAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setList(list);
//---------------------------------------------------------------------------------------------------------------------------------------------//
        postViewModel.postsMutableLiveData.observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> postModels) {
                Log.d("Tag","adapter");
                Log.d("lol", String.valueOf(postModels.size()));
            ///    int index = 0;
               // postModels.remove(index);
                for(int i=0;i<postModels.size();i++){
                    if(postModels.get(i).getEmail().equals(str)){
                        Log.d("str",str);
                        postModels.remove(i);
                    }
                }
                if(postModels.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    Nodata.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setVisibility(View.VISIBLE);
                    Nodata.setVisibility(View.GONE);
                }
                if(postModels.size()>0){
                    //Notifaction.build();
                }
                adapter.setList(postModels);
            }
        });
        //recyclerView.setAdapter(adapter);
        //   view1=(TextView) findViewById(R.id.txtView);
        // view2=(TextView) findViewById(R.id.txtView1);
     /*   if (ActivityCompat.checkSelfPermission(nearby_cars.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(nearby_cars.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }*/
      /*  locationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);*/

    }

    private void patchPost() {
        Observable observable=Observable.interval(10, TimeUnit.SECONDS)
                .flatMap(n-> PostClient.getINSTANCE().patchPost(post1.getEmail(), post1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        io.reactivex.Observer<Post> observer = new io.reactivex.Observer<Post>() {
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
        observable.subscribe(observer);
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        arr1[0]=location.getLongitude();
        arr1[1]=location.getLatitude();
        pointschema1.setCoordinates(arr1);
        post1.setLocation(pointschema1);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
     //   DeleteMessage(post1.getEmail());

    }

}
