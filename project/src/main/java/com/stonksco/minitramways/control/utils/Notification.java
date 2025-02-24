package com.stonksco.minitramways.control.utils;

// Represents a past message from a "listened" to a "listener"

public class Notification {

    private Object Data;
    private String Message;

    public Notification(String msg) {
        Message = msg;
    }

    public void SetData(Object data) {
        this.Data = data;
    }

    public String GetMessage() {
        return Message;
    }

    public Object GetData() {
        return Data;
    }

}
