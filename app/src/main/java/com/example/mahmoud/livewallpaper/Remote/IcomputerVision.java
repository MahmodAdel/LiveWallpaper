package com.example.mahmoud.livewallpaper.Remote;

import com.example.mahmoud.livewallpaper.Model.AnalyzeModel.ComputerVision;
import com.example.mahmoud.livewallpaper.Model.AnalyzeModel.URLUpload;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by mahmoud on 21/04/18.
 */

public interface IcomputerVision {
    @Headers({
            "Content-Type:application/json",
            "Ocp-Apim-Subscription-key:be0f91abef4d4df9bda930b7995972c1"
    })
    @POST
    Call<ComputerVision> analyzeImage(@Url String apiEndpoint, @Body URLUpload url);

}
