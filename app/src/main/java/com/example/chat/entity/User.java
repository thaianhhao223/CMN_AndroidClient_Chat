package com.example.chat.entity;

public class User {
    private String id_user;
    private String name;
    private String birthday;
    private String phonenumber;
    private String address;
    private String url_avatar;
    private String status;
    private String create;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl_avatar() {
        return url_avatar;
    }

    public void setUrl_avatar(String url_avatar) {
        this.url_avatar = url_avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public User() {

    }

    public User(String id_user, String name, String birthday, String phonenumber, String address, String url_avatar, String status, String create) {
        this.id_user = id_user;
        this.name = name;
        this.birthday = birthday;
        this.phonenumber = phonenumber;
        this.address = address;
        this.url_avatar = url_avatar;
        this.status = status;
        this.create = create;
    }

    @Override
    public String toString() {
        return "User{" +
                "id_user='" + id_user + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", address='" + address + '\'' +
                ", url_avatar='" + url_avatar + '\'' +
                ", status='" + status + '\'' +
                ", create='" + create + '\'' +
                '}';
    }
}
