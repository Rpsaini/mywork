package com.markrap.fragments

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface AppViewModel {




    @POST("inattendance")
    @Multipart
    fun  uploadImageIn(
        @Part("user_id") Method: RequestBody?,
        @Part("on_date") MemberId: RequestBody?,
        @Part("in_latitude") UserId: RequestBody?,
        @Part("in_longitude") Password: RequestBody?,
        @Part("image_type") imageType: RequestBody?,
        @Part image: MultipartBody.Part?,
        ): Observable<JSONObject>?


    @POST("outattendance")
    @Multipart
    fun  uploadImageout(
        @Part("user_id") Method: RequestBody?,
        @Part("on_date") MemberId: RequestBody?,
        @Part("out_latitude") UserId: RequestBody?,
        @Part("out_longitude") Password: RequestBody?,
        @Part("image_type") imageType: RequestBody?,
        @Part image: MultipartBody.Part?,
    ): Observable<JSONObject>?


//    @POST("outletvisit")
//    @Multipart
//    fun  outletvisit(
//        @Part("outlet_id") outlet_id: RequestBody?,
//        @Part("user_id") user_id: RequestBody?,
//        @Part("latitude") latitude: RequestBody?,
//        @Part("longitude") longitude: RequestBody?,
//
//
//    ): Observable<JSONObject>?

    @POST("addoutlets")
    @Multipart
    fun  addoutlets(
        @Part("user_id") user_id: RequestBody?,
        @Part("supplier_id") supplier_id: RequestBody?,
        @Part("outlet_name") outlet_name: RequestBody?,
        @Part("owner_name") owner_name: RequestBody?,
        @Part("mobile") mobile: RequestBody?,
        @Part("location_name") location_name: RequestBody?,
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part image: MultipartBody.Part?,
    ): Observable<JSONObject>?



    @POST("stockistvisit")
    @Multipart
    fun  updateStockistLatLng(
        @Part("user_id") user_id: RequestBody?,
        @Part("stockist_id") stockist_id: RequestBody?,
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?,
        @Part image: MultipartBody.Part?,
    ): Observable<JSONObject>?




}