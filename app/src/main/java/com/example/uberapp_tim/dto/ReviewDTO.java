package com.example.uberapp_tim.dto;

public class ReviewDTO {

    private int rating;
    private String comment;
    private UserShortDTO user;

    public ReviewDTO(int rating, String comment, UserShortDTO user) {
        this.rating = rating;
        this.comment = comment;
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserShortDTO getUser() {
        return user;
    }

    public void setUser(UserShortDTO user) {
        this.user = user;
    }
}
