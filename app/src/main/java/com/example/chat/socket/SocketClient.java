package com.example.chat.socket;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketClient {
    private static Socket mSocket;


    public static void setmSocket(Socket mSocket) {
        SocketClient.mSocket = mSocket;
    }
    public SocketClient() {
        {
            try {
                mSocket = IO.socket("http://192.168.1.107:3001");
            }catch (URISyntaxException e){
                e.printStackTrace();
            }
        }
    }
    public SocketClient(Socket mSocket) {
        this.mSocket = mSocket;
    }

    public static Socket getmSocket() {
        return mSocket;
    }

    public void connectSocket(){
        mSocket.connect();
    }
}
