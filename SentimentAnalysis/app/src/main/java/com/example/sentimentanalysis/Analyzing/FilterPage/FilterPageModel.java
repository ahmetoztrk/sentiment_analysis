package com.example.sentimentanalysis.Analyzing.FilterPage;

public class FilterPageModel {

    String title, description;
    int image;

    public FilterPageModel(String title, String description, int image){
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