package net.renotekno.rifqi.renoteknoapp.Posts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post {
    private String postId;
    private String postTitle;
    private String imageURL;
    private String postLink;
    private String postLabel;
    private String publishedTimePost;
    private int totalComment;

    public Post(String postId,
                String postTitle,
                String imageURL,
                String publishedPostTime) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.imageURL = imageURL;
        this.publishedTimePost = setPostTime(publishedPostTime);
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

//    public String getPostLink() {
//        return postLink;
//    }
//
//    public void setPostLink(String postLink) {
//        this.postLink = postLink;
//    }
//
//    public String getPostLabel() {
//        return postLabel;
//    }
//
//    public void setPostLabel(String postLabel) {
//        this.postLabel = postLabel;
//    }

    public String getPublishedTimePost() {
        return publishedTimePost;
    }

    public String setPostTime(String updatedPostTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat desiredFormat  = new SimpleDateFormat("E, d MMMM yyyy");
        String dateFormat = null;
        try {
            Date date = df.parse(updatedPostTime);
            dateFormat = desiredFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat;
    }

//    public int getTotalComment() {
//        return totalComment;
//    }
//
//    public void setTotalComment(int totalComment) {
//        this.totalComment = totalComment;
//    }
}
