package net.renotekno.rifqi.renoteknoapp.BitmapWorker;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import net.renotekno.rifqi.renoteknoapp.Data.HomePostData;

import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloader {
    public void loadImage(int position, ImageView image){
        String url = HomePostData.mPosts.get(position).getImageURL();
        if(cancelPotentialDownload(url, image, position)){
            BitmapWorker bitmapWorker = new BitmapWorker(image);
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(bitmapWorker);
            image.setImageDrawable(downloadedDrawable);
            try {
                bitmapWorker.execute(new URL(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }
    private boolean cancelPotentialDownload(String url, ImageView postImage, int position) {
        BitmapWorker bitmapWorker = getBitmapWorker(postImage);

        if(bitmapWorker != null){
            String bitmapUrl = bitmapWorker.urlToCheck;
            if((bitmapUrl == null) || (!bitmapUrl.equals(url))){
                Log.v("CANCELED", position + " CANCELED");
                bitmapWorker.cancel(true);
            } else {
                Log.v("CANCELED_BEING_DOWNLOAD", "URL BEING DOWNLOADED");
                return false;
            }
        }
        return true;
    }

    public static BitmapWorker getBitmapWorker(ImageView postImage) {
//        if(postImage != null){
//            Log.v("THEPOST_IMAGE", "POST_IMAGE NOT NULL");
//        }
//        if(postImage == null){
//            Log.v("THEPOST_IMAGE NULL", "POST_IMAGE IS NULL");
//        }
        if(postImage != null){
            Drawable drawable = postImage.getDrawable();
            if(drawable instanceof DownloadedDrawable){
//                Log.v("INSIDE", "YEAH I GOT HERE");
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
//                Log.v("UPS_NULL_not", "ITS not RETURN NULL");
                return downloadedDrawable.getBitmapWorker();
            }
        }
//        Log.v("UPS_NULL", "ITS RETURN NULL");
        return null;
    }
}
