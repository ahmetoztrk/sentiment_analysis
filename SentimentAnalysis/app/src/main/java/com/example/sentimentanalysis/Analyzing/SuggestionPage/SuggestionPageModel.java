package com.example.sentimentanalysis.Analyzing.SuggestionPage;

public class SuggestionPageModel {

    String title, description;
    int image;

    public SuggestionPageModel(String title, String description, int image){
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }
}