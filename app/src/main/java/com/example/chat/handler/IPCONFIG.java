package com.example.chat.handler;

public class IPCONFIG {
    private static String ip_config = "192.168.1.111";

    public static String getIp_config() {
        return ip_config;
    }

    public static void setIp_config(String ip_config) {
        IPCONFIG.ip_config = ip_config;
    }

    public IPCONFIG() {
    }

}
