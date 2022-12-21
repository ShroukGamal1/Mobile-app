package com.example.gpstracker.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpstracker.R;
import com.example.gpstracker.UI.data.ApiInterface;
import com.example.gpstracker.UI.data.PostClient;
import com.example.gpstracker.UI.model.Post;
import com.example.gpstracker.UI.model.pointSchema;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LocationListener {

    protected double latitude=-1,longitude=-1;
    Button btn;
    EditText api;
    LocationManager locationManger;
    Post post;
    pointSchema pointschema;
    TextView txt;
    PostViewModel postViewModel;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);


        btn=findViewById(R.id.btn);
        api=findViewById(R.id.api);
     //   txt=findViewById(R.id.txt);
        locationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://gradproject.onrender.com/Cars/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                Log.d("msg1", String.valueOf(post.getLocation().getCoordinates()[0]));
                if (api.getText().toString().isEmpty() == false) {
                    s=api.getText().toString();
                    pointschema=new pointSchema();
                    pointschema.setType("Point");
                    double [] arr= new double[2];
                    if(longitude==-1&&latitude==-1){
                        Toast.makeText(MainActivity.this, "We Try to GET Your Location Try Again", Toast.LENGTH_LONG).show();
                    }
                    else {
                        arr[0] = longitude;
                        arr[1] = latitude;
                        pointschema.setCoordinates(arr);
                        post = new Post(api.getText().toString(), pointschema);

                        Call<Post> call = apiInterface.storePost(post);
                        call.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                //       Log.d("Class",post.getEmail());
                                if (response.code() == 400) {
                                    Toast.makeText(MainActivity.this, "Enter anther Email", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Post Successfully", Toast.LENGTH_LONG).show();
                                    postViewModel.setEmail(post.getEmail());
                                    String str = post.getEmail();
                                    Intent i = new Intent(getApplicationContext(), nearby_cars.class);
                                    i.putExtra("message_key", str);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                //  Log.d("responce","Failed");
                                Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
 latitude=location.getLatitude();
 longitude=location.getLongitude();
    }
}