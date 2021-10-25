package com.model;

public class My_user {

    private long userId;
    private String name;
    private String passwd;
    private int status;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "My_user{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", passwd='" + passwd + '\'' +
                ", status=" + status +
                '}';
    }
}
