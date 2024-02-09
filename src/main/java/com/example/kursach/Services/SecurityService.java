package com.example.kursach.Services;

public interface SecurityService
{
    String findLoggeInUsername();
    void autoLogin(String login,String password);

}
