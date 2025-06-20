package com.example.ttlogexample.modelNestjs;

import java.io.IOException;

public class ServerErrorIO  {


   public int errcode;
   public String errmsg;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int statusCode) {
        this.errcode = statusCode;
    }

    String timestamp;
    String path;
}
