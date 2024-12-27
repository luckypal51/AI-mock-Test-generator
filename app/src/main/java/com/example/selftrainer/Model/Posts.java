package com.example.selftrainer.Model;

public class Posts {
    private String Question;
    private String UserId;
    private String VideoUri;

    public Posts() {
    }

    public Posts(String question, String videoUri, String userId) {
        Question = question;
        VideoUri = videoUri;
        UserId = userId;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getVideoUri() {
        return VideoUri;
    }

    public void setVideoUri(String videoUri) {
        VideoUri = videoUri;
    }
}
