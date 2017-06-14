package com.example.varma.contacts.Objects;


import com.example.varma.contacts.Extra.Utils;

public class Friend {

    private String _NAME, _NUMBER, _EMAIL, _ID, _NUMBER_OLD, IMAGE_URL, firstLetter, USER_ID;

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }



    public String getFirstLetter() {
        return firstLetter;
    }


    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String get_NAME() {
        return _NAME;
    }

    public void set_NAME(String _NAME) {
        this._NAME = _NAME;
        this.firstLetter = Utils.getFirstLetter(_NAME);
    }

    public String get_NUMBER() {
        return _NUMBER;
    }

    public void set_NUMBER(String _NUMBER) {
        this._NUMBER = _NUMBER;
    }

    public String get_EMAIL() {
        return _EMAIL;
    }

    public void set_EMAIL(String _EMAIL) {
        this._EMAIL = _EMAIL;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String get_NUMBER_OLD() {
        return _NUMBER_OLD;
    }

    public void set_NUMBER_OLD(String _NUMBER_OLD) {
        this._NUMBER_OLD = _NUMBER_OLD;
    }
}
