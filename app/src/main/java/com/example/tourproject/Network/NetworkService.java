package com.example.tourproject.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkService {
    @GET("/user/insert_userId.php") //macAddress 넣기
    Call<String> insertUserId(@Query("user_id") String user_id);


}
