package com.example.chat.socket;

import com.example.chat.handler.IPCONFIG;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketClient {
    private String IP_config = IPCONFIG.getIp_config();
    private static Socket mSocket;


    public static void setmSocket(Socket mSocket) {
        SocketClient.mSocket = mSocket;
    }
    public SocketClient() {
        {
            try {
                mSocket = IO.socket("http://"+IP_config+":3001");
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
