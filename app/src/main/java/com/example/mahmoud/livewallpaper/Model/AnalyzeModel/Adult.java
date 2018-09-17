package com.example.mahmoud.livewallpaper.Model.AnalyzeModel;

/**
 * Created by mahmoud on 21/04/18.
 */

public class Adult {

    private boolean isAdultContent;
    private double adultScore;
    private boolean isRecyContent;
    private double racyScore;


    public Adult() {
    }

    public Adult(boolean isAdultContent, double adultScore, boolean isRecyContent, double racyScore) {
        this.isAdultContent = isAdultContent;
        this.adultScore = adultScore;
        this.isRecyContent = isRecyContent;
        this.racyScore = racyScore;
    }

    public boolean isAdultContent() {
        return isAdultContent;
    }

    public void setAdultContent(boolean adultContent) {
        isAdultContent = adultContent;
    }

    public double getAdultScore() {
        return adultScore;
    }

    public void setAdultScore(double adultScore) {
        this.adultScore = adultScore;
    }

    public boolean isRecyContent() {
        return isRecyContent;
    }

    public void setRecyContent(boolean recyContent) {
        isRecyContent = recyContent;
    }

    public double getRacyScore() {
        return racyScore;
    }

    public void setRacyScore(double racyScore) {
        this.racyScore = racyScore;
    }
}
