package com.example.plantport.Model;

public class Query {

    String Subject,Reason,Query_Id,User_Id,User_Phone,User_name;

    public Query() {
    }

    public Query(String subject, String reason, String query_Id, String user_Id, String user_Phone, String user_name) {
        Subject = subject;
        Reason = reason;
        Query_Id = query_Id;
        User_Id = user_Id;
        User_Phone = user_Phone;
        User_name = user_name;
    }

    public String getUser_Phone() {
        return User_Phone;
    }

    public void setUser_Phone(String user_Phone) {
        User_Phone = user_Phone;
    }

    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getQuery_Id() {
        return Query_Id;
    }

    public void setQuery_Id(String query_Id) {
        Query_Id = query_Id;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }
}
