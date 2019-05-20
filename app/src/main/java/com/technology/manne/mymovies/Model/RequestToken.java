package com.technology.manne.mymovies.Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by manne on 15.2.2018.
 */

public class RequestToken {
    public String expires_at;
    public String request_token;
    public String session_id;
    public boolean success;

    public RequestToken(){

    }


    public String getExpires_at() {
        return expires_at;
    }

    public String getRequest_token() {
        return request_token;
    }

    public String getSession_id() {
        return session_id;
    }

    public Boolean isExpired () {
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date= null;// converting String to date
        try {
            date = df.parse(expires_at);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date today=new Date();
        return today.after(date);
    }
}
