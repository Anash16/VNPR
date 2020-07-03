package com.project.vnpr;

public class Vehicle {

    public String UserName,Email,Password,ConfirmPassword;

    private Vehicle(){

    }

    public Vehicle(String UserName, String Email, String Password, String ConfirmPassword){
        this.UserName = UserName;
        this.Email = Email;
        this.Password = Password;
        this.ConfirmPassword = ConfirmPassword;

    }
}
