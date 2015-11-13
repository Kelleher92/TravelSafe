package com.example.ian.travelsafe;

public class ChildDetails {

    int _id, _parentid;
    String _name, _username, _password, _route;

    public ChildDetails(String name) {
        this._name = name;
        this._route = null;
    }

    public ChildDetails(int id, String name, String username, String password) {
        this._id = id;
        this._name = name;
        this._username = username;
        this._password = password;
        this._route = null;
    }

    public ChildDetails(int id, int _parentid, String name, String username, String password) {
        this._id = id;
        this._parentid = _parentid;
        this._name = name;
        this._username = username;
        this._password = password;
        this._route = null;
    }

    public ChildDetails(String name, String username, String password) {
        this._name = name;
        this._username = username;
        this._password = password;
        this._route = null;
    }

    public ChildDetails(String username, String password) {
        this._username = username;
        this._password = password;
        this._route = null;
    }

    public void set_name(String _name) {
        this._name = _name;
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

    public int get_parentid() {
        return _parentid;
    }

    public String get_name() {
        return _name;
    }

    public String get_username() {
        return _username;
    }

    public String get_password() {
        return _password;
    }

}
