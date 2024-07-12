package com.example.moviesapp.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesapp.Adapters.CategoryListAdapter;
import com.example.moviesapp.Adapters.FilmListAdapter;
import com.example.moviesapp.Adapters.SliderAdapters;
import com.example.moviesapp.Domain.GenresItem;
import com.example.moviesapp.Domain.ListFilm;
import com.example.moviesapp.Domain.SliderItems;
import com.example.moviesapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class activity_main2 extends AppCompatActivity {
    private RecyclerView.Adapter adapterBestMovies,adapterUpComing,adapterCategory;
    private RecyclerView recyclerViewBestMovies,recyclerViewUpComing,recyclerViewCategory;
    private RequestQueue nRequestQueue;
    private StringRequest nStringRequest1,nStringRequest2,nStringRequest3;
    private ProgressBar loading1,loading2,loading3;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        banners();
        sendRequestBestMovies();
        sendRequestUpComing();
        sendRequestCategory();
    }

    private void sendRequestBestMovies() {
        nRequestQueue = Volley.newRequestQueue(this);
        loading1.setVisibility(View.VISIBLE);

        String url = "https://moviesapi.ir/api/v1/movies?page=1";
        nStringRequest1 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        loading1.setVisibility(View.GONE);

                        // Assuming ListFilm is your model class
                        ListFilm items = gson.fromJson(response, ListFilm.class);

                        // Assuming recyclerViewBestMovies and adapterBestMovies are initialized properly
                        adapterBestMovies = new FilmListAdapter(items);
                        recyclerViewBestMovies.setAdapter(adapterBestMovies);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading1.setVisibility(View.GONE);
                        Log.e("MoviesApp", "onErrorResponse: " + error.toString());
                    }
                });

        nRequestQueue.add(nStringRequest1);
    }
    private void sendRequestUpComing() {
        nRequestQueue = Volley.newRequestQueue(this);
        loading3.setVisibility(View.VISIBLE);

        String url = "https://moviesapi.ir/api/v1/movies?page=2";
        nStringRequest3 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        loading3.setVisibility(View.GONE);

                        // Assuming ListFilm is your model class
                        ListFilm items = gson.fromJson(response, ListFilm.class);

                        // Assuming recyclerViewBestMovies and adapterBestMovies are initialized properly
                         adapterUpComing= new FilmListAdapter(items);
                        recyclerViewUpComing.setAdapter(adapterUpComing);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading3.setVisibility(View.GONE);
                        Log.e("MoviesApp", "onErrorResponse: " + error.toString());
                    }
                });

        nRequestQueue.add(nStringRequest3);
    }
    private void sendRequestCategory() {
        nRequestQueue = Volley.newRequestQueue(this);
        loading2.setVisibility(View.VISIBLE);

        String url = "https://moviesapi.ir/api/v1/genres";
        nStringRequest2 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        loading2.setVisibility(View.GONE);

                        ArrayList<GenresItem> catList= gson.fromJson(response,new TypeToken<ArrayList<GenresItem>>(){

                        }.getType());
                        adapterCategory= new CategoryListAdapter(catList);
                        recyclerViewCategory.setAdapter(adapterCategory);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading2.setVisibility(View.GONE);
                        Log.e("MoviesApp", "onErrorResponse: " + error.toString());
                    }
                });

        nRequestQueue.add(nStringRequest2);
    }
private void banners() {
        List<SliderItems>sliderItems =new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide));
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.wide3));

        viewPager2.setAdapter(new SliderAdapters(sliderItems,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
            }
        });
    }
private Runnable sliderRunnable=new Runnable() {
    @Override
    public void run() {
        viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
    }
};

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable,2000);
    }

    private void initView() {
        viewPager2=findViewById(R.id.viewpagerSlider);
        recyclerViewBestMovies=findViewById(R.id.view1);
        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewUpComing=findViewById(R.id.view3);
        recyclerViewUpComing.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewCategory=findViewById(R.id.view2);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        loading1=findViewById(R.id.progressBar1);
        loading2=findViewById(R.id.progressBar2);
        loading3=findViewById(R.id.progressBar3);
    }
}