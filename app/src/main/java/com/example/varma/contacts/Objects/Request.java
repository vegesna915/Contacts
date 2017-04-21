package com.example.varma.contacts.Objects;

/**
 * Created by Varma on 4/18/2017.
 */

public class Request {

    private String REQUEST_ID, SENDER_ID, RECEIVER_ID, IS_PENDING, IS_ACCEPTED;

    public String getREQUEST_ID() {
        return REQUEST_ID;
    }

    public void setREQUEST_ID(String REQUEST_ID) {
        this.REQUEST_ID = REQUEST_ID;
    }

    public String getSENDER_ID() {
        return SENDER_ID;
    }

    public void setSENDER_ID(String SENDER_ID) {
        this.SENDER_ID = SENDER_ID;
    }

    public String getRECEIVER_ID() {
        return RECEIVER_ID;
    }

    public void setRECEIVER_ID(String RECEIVER_ID) {
        this.RECEIVER_ID = RECEIVER_ID;
    }

    public String getIS_PENDING() {
        return IS_PENDING;
    }

    public void setIS_PENDING(String IS_PENDING) {
        this.IS_PENDING = IS_PENDING;
    }

    public String getIS_ACCEPTED() {
        return IS_ACCEPTED;
    }

    public void setIS_ACCEPTED(String IS_ACCEPTED) {
        this.IS_ACCEPTED = IS_ACCEPTED;
    }
}
