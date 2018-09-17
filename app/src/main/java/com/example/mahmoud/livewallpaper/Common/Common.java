package com.example.mahmoud.livewallpaper.Common;

import android.content.IntentSender;

import com.example.mahmoud.livewallpaper.Model.WallpaperItem;
import com.example.mahmoud.livewallpaper.Remote.IcomputerVision;
import com.example.mahmoud.livewallpaper.Remote.RetrofitClient;

/**
 * Created by mahmoud on 7/04/18.
 */

public class Common {
    public static final int SIGN_IN_REQUEST_CODE =1001 ;
    public static final int PICK_IMAGE_REQUEST = 1002;
    public static String STR_CATEGORY_BACKGROUND="CategoryBackground";
    public static String CATEGORY_SELECTED;
    public static String STR_WALLPAPER="Backgrounds";
    public static String CATEGORY_ID_SELECTED;
    public static final int PERMISSION_REQUEST_CODE=1000;
    public static WallpaperItem selected_background=new WallpaperItem();
    public static String selected_background_key;

    public static String BASE_URL="https://westcentralus.api.cognitive.microsoft.com/vision/v1.0/";
    public static IcomputerVision getComputerVisionAPI(){
        return RetrofitClient.getClient(BASE_URL).create(IcomputerVision.class);
    }

    public static String getAPIAdultEndpoint(){
        return new StringBuilder(BASE_URL).append("analyze?visualFeatures=Adult&language=en").toString();
    }

}
