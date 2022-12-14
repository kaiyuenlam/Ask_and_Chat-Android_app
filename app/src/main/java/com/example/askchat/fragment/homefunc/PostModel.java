package com.example.askchat.fragment.homefunc;

public class PostModel {
    private String postID, image, publisher, question, voteCounter;


    public PostModel() {

    }

    public PostModel(String postID, String image, String publisher, String question, String voteCounter) {
        this.postID = postID;
        this.image = image;
        this.publisher = publisher;
        this.question = question;
        this.voteCounter = voteCounter;
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

    public String getVoteCounter() {
        return voteCounter;
    }

    public void setVoteCounter(String voteCounter) {
        this.voteCounter = voteCounter;
    }
}
