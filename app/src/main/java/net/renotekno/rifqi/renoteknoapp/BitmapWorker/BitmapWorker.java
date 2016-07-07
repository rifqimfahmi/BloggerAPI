package net.renotekno.rifqi.renoteknoapp.BitmapWorker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import net.renotekno.rifqi.renoteknoapp.Adapter.PostAdapter;
import net.renotekno.rifqi.renoteknoapp.HomePage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class BitmapWorker extends AsyncTask<URL, Void, Bitmap> {
    ImageView mPostImage;
    private WeakReference<ImageView> mImageViewWeakReference;
    public String urlToCheck;

    public BitmapWorker(ImageView postImage) {
        mPostImage = postImage;
        mImageViewWeakReference = new WeakReference<ImageView>(postImage);
    }
//    @Override
//    protected Bitmap doInBackground(String... strings) {
//        String url = strings[0];
//        Bitmap imageBitmap = null;
//            imageBitmap = decodeAndSamplesized(url);
//        return imageBitmap;
//    }

    @Override
    protected Bitmap doInBackground(URL... urls) {
        URL url = urls[0];
        urlToCheck = url.toString();
        Bitmap imageBitmap = HomePage.getBitmapFromCache(urlToCheck);
        if(imageBitmap == null) {
            imageBitmap = decodeAndSamplesized(url);
            HomePage.addBitmaptoCache(urlToCheck, imageBitmap);
        }
        
        return imageBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if(isCancelled()){
            bitmap = null;
        }
        if(mImageViewWeakReference != null){
            ImageView theImagePost = mImageViewWeakReference.get();
            BitmapWorker bitmapWorker = ImageDownloader.getBitmapWorker(theImagePost);
            if(HomePage.getBitmapFromCache(urlToCheck) != null){
                theImagePost.setImageBitmap(bitmap);
            }else {
                if (this == bitmapWorker) {
                    theImagePost.setImageBitmap(bitmap);
                }
            }
        }
//        if(mImageViewWeakReference != null && bitmap != null){
//            ImageView mPostImage = mImageViewWeakReference.get();
//            if(mPostImage == null){
//                mPostImage.setImageBitmap(bitmap);
//            }
//        }
    }

    private Bitmap decodeAndSamplesized(URL url) {
;
        Bitmap imageBitmap = null;
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(url.openConnection().getInputStream());
            imageBitmap = sampleSizedBitmap(bis);
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageBitmap;

    }

    private Bitmap sampleSizedBitmap(InputStream is) {

        //set decodeBound=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap bitmapa = BitmapFactory.decodeStream(is);
//
//        calculate the sample sized
//        options.inSampleSize = calculateInSampleSized(options, 130, 130);
        options.inSampleSize = 2;

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        return bitmap;
    }

    private int calculateInSampleSized(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSized = 1;

        if(width > reqWidth || height > reqHeight){
            int halfHeight = height /2;
            int halfWidth = width /2;

            while((halfHeight / inSampleSized) > reqHeight && (halfWidth / inSampleSized) > reqWidth){
                inSampleSized *= 2;
            }
        }
        return inSampleSized;
    }
}
