package net.renotekno.rifqi.renoteknoapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import net.renotekno.rifqi.renoteknoapp.Data.HomePostData;
import net.renotekno.rifqi.renoteknoapp.FragmentLayout.Home;
import net.renotekno.rifqi.renoteknoapp.Posts.Post;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomePage extends AppCompatActivity {

    public static final String API_KEY = "AIzaSyAHeVlvt9A8uMlgR75drY0IcWwp39PO04c";
    public static final String BLOG_ID = "7038144870910668918";
    private static final String PREF_DATA = "net.renotekno.rifqi.renoteknoapp.sharedpreferences";
    private static final String HOME_POST_SAVED = "home_post_saved";
    private static final String SAVED_POST_VALUE = "saved_post_value";

    @BindView(R.id.actionBar) Toolbar mToolbar;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    Gson mGson = new Gson();
    private static LruCache<String, Bitmap> mLruCache;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory /8;
            mLruCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount() / 1024;
                }
            };

        mSharedPreferences = getSharedPreferences(PREF_DATA, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();



        if(savedInstanceState == null) {
            Home home = new Home();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.frameLayout, home);
            transaction.commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        if(HomePostData.mPosts.size() == 0 && mSharedPreferences != null){
            int savedPostValue = mSharedPreferences.getInt(SAVED_POST_VALUE, 0);
            String[] savedStringPost = new String[savedPostValue];
            for(int i = 0; i < savedPostValue; i++){
                savedStringPost[i] = mSharedPreferences.getString(HOME_POST_SAVED + i, "");
                Post post = mGson.fromJson(savedStringPost[i], Post.class);
                HomePostData.mPosts.add(i, post);
            }
        }
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.v("ARRAY_DATA", String.valueOf(HomePostData.mPosts.size()));
        int dataSize = HomePostData.mPosts.size();
        String savedJson[] = new String[HomePostData.mPosts.size()];
        for(int i = 0 ; i < dataSize; i++){
            savedJson[i] = mGson.toJson(HomePostData.mPosts.get(i));
            mEditor.putString(HOME_POST_SAVED + i, savedJson[i]);
        }
        mEditor.putInt(SAVED_POST_VALUE, savedJson.length);
        mEditor.commit();
        super.onPause();
    }
//  cache bitmap function
    public static void addBitmaptoCache(String key, Bitmap bitmap){
        if(getBitmapFromCache(key) == null){
            mLruCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromCache(String key) {
        return mLruCache.get(key);
    }

}
