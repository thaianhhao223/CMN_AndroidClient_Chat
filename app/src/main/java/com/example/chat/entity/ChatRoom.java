package com.example.chat.entity;


import java.sql.Date;

public class ChatRoom {
    private String id_chatroom;
    private String create_date;
    private int is_group_chat;
    private String id_admin;
    private String message_newest;
    private String datetime_newest;
    private String id_user_lastsend;
    private String name_chatroom;
    private String url_ava_chatroom;

    public String getId_chatroom() {
        return id_chatroom;
    }

    public void setId_chatroom(String id_chatroom) {
        this.id_chatroom = id_chatroom;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public int isIs_group_chat() {
        return is_group_chat;
    }

    public void setIs_group_chat(int is_group_chat) {
        this.is_group_chat = is_group_chat;
    }

    public String getId_admin() {
        return id_admin;
    }

    public void setId_admin(String id_admin) {
        this.id_admin = id_admin;
    }

    public String getMessage_newest() {
        return message_newest;
    }

    public void setMessage_newest(String message_newest) {
        this.message_newest = message_newest;
    }

    public String getDatetime_newest() {
        return datetime_newest;
    }

    public void setDatetime_newest(String datetime_newest) {
        this.datetime_newest = datetime_newest;
    }

    public String getId_user_lastsend() {
        return id_user_lastsend;
    }

    public void setId_user_lastsend(String id_user_lastsend) {
        this.id_user_lastsend = id_user_lastsend;
    }

    public String getName_chatroom() {
        return name_chatroom;
    }

    public void setName_chatroom(String name_chatroom) {
        this.name_chatroom = name_chatroom;
    }

    public String getUrl_ava_chatroom() {
        return url_ava_chatroom;
    }

    public void setUrl_ava_chatroom(String url_ava_chatroom) {
        this.url_ava_chatroom = url_ava_chatroom;
    }

    public ChatRoom(String id_chatroom, String create_date, int is_group_chat, String id_admin,
                    String message_newest, String datetime_newest, String id_user_lastsend,
                    String name_chatroom, String url_ava_chatroom) {
        this.id_chatroom = id_chatroom;
        this.create_date = create_date;
        this.is_group_chat = is_group_chat;
        this.id_admin = id_admin;
        this.message_newest = message_newest;
        this.datetime_newest = datetime_newest;
        this.id_user_lastsend = id_user_lastsend;
        this.name_chatroom = name_chatroom;
        this.url_ava_chatroom = url_ava_chatroom;
    }

    public ChatRoom() {

    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id_chatroom='" + id_chatroom + '\'' +
                ", create_date=" + create_date +
                ", is_group_chat=" + is_group_chat +
                ", id_admin='" + id_admin + '\'' +
                ", message_newest='" + message_newest + '\'' +
                ", datetime_newest=" + datetime_newest +
                ", id_user_lastsend='" + id_user_lastsend + '\'' +
                ", name_chatroom='" + name_chatroom + '\'' +
                ", url_ava_chatroom='" + url_ava_chatroom + '\'' +
                '}';
    }
}
