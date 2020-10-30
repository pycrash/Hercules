package com.example.hercules.Remote;

import com.example.hercules.Models.DataMessage;
import com.example.hercules.Models.MyResponse;
import com.example.hercules.Models.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAumE5X8Y:APA91bF6slaIOcnSISX2V3vx4uFsekrhGwL2FF8TGZrMw0YaS8-eZg4WIhUvoaWkPi64Jem47jO7Y69XBB_8qhWn3YLlhNhbI-H4FRH1Jd9BSTDfSyIIQV4ZdRxaCVHEKCHLrygROBki"
            }
    )


    @POST ("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);

}
