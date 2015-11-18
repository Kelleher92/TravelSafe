package com.example.ian.travelsafe;

/**
 * Created by ian on 05/11/2015.
 */
public class Users {

    int _id;
    String _emailAddress, _username, _password, _flag;

    public Users(int id, String emailAddress, String username, String password) {
        this._id = id;
        this._emailAddress = emailAddress;
        this._username = username;
        this._password = password;
        this._flag = "P";
    }

    public Users(int id, String emailAddress, String username, String password, String flag) {
        this._id = id;
        this._emailAddress = emailAddress;
        this._username = username;
        this._password = password;
        this._flag = flag;
    }

    public Users(String emailAddress, String username, String password) {
        this._emailAddress = emailAddress;
        this._username = username;
        this._password = password;
        this._flag = "P";
    }

    public Users(String username, String password) {
        this._username = username;
        this._password = password;
        this._flag = "P";
    }

    public void set_emailAddress(String _emailAddress) {
        this._emailAddress = _emailAddress;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public int get_id() {
        return _id;
    }

    public String get_flag() {
        return _flag;
    }


    public String get_emailAddress() {
        return _emailAddress;
    }

    public String get_username() {
        return _username;
    }

    public String get_password() {
        return _password;
    }

}
