package com.example.hercules.Models;

import com.example.hercules.Remote.APIService;
import com.example.hercules.Remote.RetroFitClient;

import retrofit2.Retrofit;

public class Common {

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService() {
        return RetroFitClient.getClient(BASE_URL).create(APIService.class);
    }
}
