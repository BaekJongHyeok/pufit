package com.example.pethealth;

public class UserAccount {

    private String idToken;		// Firebase Uid(고유 토큰 정보)
    private String UserName;		// 사용자 이름
    private String emailId;		// 이메일 아이디
    private String password;	// 비밀번호

    public UserAccount() { }


    public String getIdToken() { return idToken; }

    public void setIdToken(String idToken) { this.idToken = idToken; }

    public String getEmailId() { return emailId; }

    public void setEmailId(String emailId) { this.emailId = emailId; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}