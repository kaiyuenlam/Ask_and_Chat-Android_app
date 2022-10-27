package com.example.askchat.fragment.homefunc;

public class CommentModel {
    private String postID, image, publisher, question;
    public CommentModel(String postID, String image, String publisher, String question) {
        this.postID = postID;
        this.image = image;
        this.publisher = publisher;
        this.question = question;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
