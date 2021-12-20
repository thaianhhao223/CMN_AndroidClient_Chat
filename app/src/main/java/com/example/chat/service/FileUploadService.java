package com.example.chat.service;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {
    @Multipart
    @POST("Image")
    Call<ResponseBody> uploadFile(@Part("file") RequestBody body);
}
