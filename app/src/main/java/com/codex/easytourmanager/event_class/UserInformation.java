package com.codex.easytourmanager.event_class;

public class UserInformation {
    private String userName;
    private String userContact;
    private String userEmail;
    private String userAddress;
    private String userKey;
    private String profileImageData;

    public String getProfileImageData() {
        return profileImageData;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserKey() {
        return userKey;
    }

    public UserInformation() {
    }

    public UserInformation(String userName, String userContact, String userEmail, String userAddress, String userKey, String profileImageData) {
        this.userName = userName;
        this.userContact = userContact;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.userKey = userKey;
        this.profileImageData = profileImageData;
    }
}
