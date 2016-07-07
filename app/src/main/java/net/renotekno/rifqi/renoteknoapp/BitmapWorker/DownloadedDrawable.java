package net.renotekno.rifqi.renoteknoapp.BitmapWorker;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;

import java.lang.ref.WeakReference;

public class DownloadedDrawable extends ColorDrawable {
    WeakReference<BitmapWorker> bitmapWorkerTaskReference;

    public DownloadedDrawable(BitmapWorker worker){
        super(Color.WHITE);
        bitmapWorkerTaskReference = new WeakReference<BitmapWorker>(worker);
    }

    public BitmapWorker getBitmapWorker(){
        return bitmapWorkerTaskReference.get();
    }
}
