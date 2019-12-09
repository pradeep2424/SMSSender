package com.smser.smssender.apicaller;

import com.smser.smssender.model.RegisterReq;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiSignature {

    @POST("Insertsmsdetails")
    Call<ResponseBody> registerUser(@Body RegisterReq req);

    @GET("getUserDetails/{mobile}/{token}")
    Call<RegisterReq> purchaseAccount(@Path("mobile") String mobile, @Path("token") String token);

    @GET("getUserDetailsFromMobile/{mobile}")
    Call<RegisterReq> getUserDetails(@Path("mobile") String mobile);

    @POST("SendSMSAPI/{user}/{pass}/Trans/{number}/{message}/{SenderID}/XYZ")
    Call<ResponseBody> sendWebSms(/*@Path("channel") String channel,*/
            @Path("user") String user, @Path("pass") String pass, @Path("number") String number,
            @Path("message") String message, @Path("SenderID") String SenderID
            /*@Path("Campaign") String Campaign*/);

    

}
