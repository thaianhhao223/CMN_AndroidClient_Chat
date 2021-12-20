package com.example.chat.handler;

public class IPCONFIG {
    private static String ip_config = "ec2-13-213-52-25.ap-southeast-1.compute.amazonaws.com"; //ec2-13-213-52-25.ap-southeast-1.compute.amazonaws.com

    public static String getIp_config() {
        return ip_config;
    }

    public static void setIp_config(String ip_config) {
        IPCONFIG.ip_config = ip_config;
    }

    public IPCONFIG() {
    }

}
