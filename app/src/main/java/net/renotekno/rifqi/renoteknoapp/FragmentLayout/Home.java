package net.renotekno.rifqi.renoteknoapp.FragmentLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.renotekno.rifqi.renoteknoapp.Adapter.DividerList;
import net.renotekno.rifqi.renoteknoapp.Adapter.PostAdapter;
import net.renotekno.rifqi.renoteknoapp.Data.HomePostData;
import net.renotekno.rifqi.renoteknoapp.HomePage;
import net.renotekno.rifqi.renoteknoapp.Posts.Post;
import net.renotekno.rifqi.renoteknoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Home extends Fragment {


    private String connection;
    public Context context;


    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_fragment, container, false);
        context = getActivity().getApplicationContext();
        ButterKnife.bind(this, view);

        getJsonData();





        return view;
    }

    private void getJsonData() {
        connection = "https://www.googleapis.com/blogger/v3/blogs/"
                + HomePage.BLOG_ID
                +"/posts?fetchBodies=false&fetchImages=true&maxResults=10&key="
                + HomePage.API_KEY ;
        if(isNetworkConnected()){
            getPostData();
        }else {
            Toast.makeText(context,
                    "No network connection, please check your internet connection",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getPostData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(connection).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Failed to get data from the server ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String jsonData = response.body().string();
                    try {
                        setPostData(jsonData);
                        Log.d("DATAJSON", HomePostData.mPosts.size() +"");
                        attacthDataToRecycler();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        toastMessageOnUI("Failed to get data ");
                    }
                } else {
                    Toast.makeText(context, "Ups something wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void attacthDataToRecycler() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager manager = new LinearLayoutManager(context);
                PostAdapter postAdapter = new PostAdapter(getActivity());
                DividerList dividerList = new DividerList(context);
                mRecyclerView.setLayoutManager(manager);
                mRecyclerView.addItemDecoration(dividerList);
                mRecyclerView.setAdapter(postAdapter);
            }
        });
    }

    private void toastMessageOnUI(final String message){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setPostData(String jsonData) throws JSONException {
        JSONObject rootData = new JSONObject(jsonData);
        JSONArray items = rootData.getJSONArray("items");
        int totalBlogPost = items.length();

        for(int i = 0; i < totalBlogPost; i++){
            JSONObject eachPost = items.getJSONObject(i);
            String postId = eachPost.getString("id");
            String publishedTimePost = eachPost.getString("published");
            String posttitle = eachPost.getString("title");

            JSONArray images = eachPost.getJSONArray("images");
            JSONObject imageObject = images.getJSONObject(0);
            String imageURL = imageObject.getString("url");


            HomePostData.mPosts.add(new Post(postId,
                    posttitle,
                    imageURL,
                    publishedTimePost));
        }
    }

    private boolean isNetworkConnected(){
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean networkConnected = false;
        if(networkInfo != null && networkInfo.isConnected()){
            networkConnected = true;
        }
        return networkConnected;
    }

    @Override
    public void onPause() {
        HomePostData.mPosts.clear();
        super.onPause();
    }


}
