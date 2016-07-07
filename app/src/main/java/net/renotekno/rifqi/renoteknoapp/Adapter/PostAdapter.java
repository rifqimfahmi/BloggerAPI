package net.renotekno.rifqi.renoteknoapp.Adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.renotekno.rifqi.renoteknoapp.BitmapWorker.ImageDownloader;
import net.renotekno.rifqi.renoteknoapp.Data.HomePostData;
import net.renotekno.rifqi.renoteknoapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.postViewHolder> {
    public FragmentActivity mActivity;
    public PostAdapter(FragmentActivity activity) {
        mActivity = activity;
    }

    @Override
    public postViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler, parent, false);
        return new postViewHolder(view);
    }

    @Override
    public void onBindViewHolder(postViewHolder holder, int position) {
        holder.bindView(position, mActivity);
    }

    @Override
    public int getItemCount() {
        return HomePostData.mPosts.size();
    }

    public static class postViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.postImage) ImageView mPostImage;
        @BindView(R.id.postTitle) TextView mPostTitle;
        @BindView(R.id.dateFormat) TextView mDateFormat;
        ImageDownloader imageDownloader;
        public postViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageDownloader = new ImageDownloader();
        }

        public void bindView(int position, FragmentActivity activity){
            mPostTitle.setText(HomePostData.mPosts.get(position).getPostTitle());
            mDateFormat.setText(HomePostData.mPosts.get(position).getPublishedTimePost());
            imageDownloader.loadImage(position, mPostImage);
//            Log.v("MY_POSITION", "I AM CURRENTLY IN " + position + " IS THE " + HomePostData.mPosts.get(position).getPostTitle());

        }
//        private void loadImage(final int position, final FragmentActivity activity, ImageView postImage) throws MalformedURLException {
//
//            String url = HomePostData.mPosts.get(position).getImageURL();
//            if(cancelPotentialDownload(url, postImage)){
//
//                BitmapWorker bitmapWorker = new BitmapWorker(postImage);
//                bitmapWorker.execute(new URL(url));
//
//            }
////            Thread thread = new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    try {
////
//////                        URLConnection connection = url.openConnection();
//////                        InputStream is = connection.getInputStream();
//////                        final Bitmap imageBitmap = BitmapFactory.decodeStream(is);
//////                        is.close();
//////                        activity.runOnUiThread(new Runnable() {
//////                            @Override
//////                            public void run() {
//////                                mPostImage.setImageBitmap(imageBitmap);
//////                            }
//////                        });
////                    } catch (MalformedURLException e) {
////                        e.printStackTrace();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////            });
////            thread.setName("loadingBitmap");
////            thread.start();
//        }

//        private boolean cancelPotentialDownload(String url, ImageView postImage) {
//            BitmapWorker bitmapWorker = getBitmapWorker(postImage);
//
//            if(bitmapWorker != null){
//                String bitmapUrl = bitmapWorker.url;
//                if((bitmapUrl == null) || (!bitmapUrl.equals(url))){
//                    bitmapWorker.cancel(true);
//                } else {
//                    return false;
//                }
//            }
//            return false;
//        }
//
//        public static BitmapWorker getBitmapWorker(ImageView postImage) {
//            if(postImage != null){
//                Drawable drawable = postImage.getDrawable();
//                if(drawable instanceof DownloadedDrawable){
//                    DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
//                    return downloadedDrawable.getBitmapWorker();
//                }
//            }
//            return null;
//        }
    }
}
