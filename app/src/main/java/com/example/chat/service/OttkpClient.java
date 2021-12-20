package com.example.chat.service;

import com.example.chat.handler.IPCONFIG;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class OttkpClient {
    private final String IP_HOST = IPCONFIG.getIp_config();
    public String storgeAFile(File file) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file",file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file))
                .build();
        Request request = new Request.Builder()
                .url("http://"+IP_HOST+":3000/Image")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // request the entire body.
        Buffer buffer = source.buffer();
        // clone buffer before reading from it
        String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));

        return responseBodyString;
    }
}
