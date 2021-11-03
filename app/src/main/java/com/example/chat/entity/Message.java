package com.example.chat.entity;

public class Message {
    private String id_send;
    private String type;
    private String message;

    public String getId_send() {
        return id_send;
    }

    public void setId_send(String id_send) {
        this.id_send = id_send;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(String id_send, String type, String message) {
        this.id_send = id_send;
        this.type = type;
        this.message = message;
    }

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "id_send='" + id_send + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
