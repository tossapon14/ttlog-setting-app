package com.example.ttlogexample.modelNestjs;

public class TtlockData {
    private String ttlockuser;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private int uid;
    private int openid;
    private String scope;
    private String token_type;

    // Getter and Setter for ttlockuser
    public String getTtlockuser() {
        return ttlockuser;
    }

    public void setTtlockuser(String ttlockuser) {
        this.ttlockuser = ttlockuser;
    }

    // Getter and Setter for access_token
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    // Getter and Setter for expires_in
    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    // Getter and Setter for refresh_token
    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    // Getter and Setter for uid
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    // Getter and Setter for openid
    public int getOpenid() {
        return openid;
    }

    public void setOpenid(int openid) {
        this.openid = openid;
    }

    // Getter and Setter for scope
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    // Getter and Setter for token_type
    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}
