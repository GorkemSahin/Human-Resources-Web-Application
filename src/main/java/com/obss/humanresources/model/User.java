package com.obss.humanresources.model;

import java.io.Serializable;

public class User implements Serializable {

    private String sessionId;
    private String userId;
    private USER_TYPE userType;

    public enum USER_TYPE {
        VISITOR,
        APPLICANT,
        HR_EXPERT
    }

    public User (){}

    public User(String sessionId, String userId, USER_TYPE userType, Applicant applicant) {
        this.sessionId = sessionId;
        this.userType = userType;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public USER_TYPE getUserType() {
        return userType;
    }

    public void setUserType(USER_TYPE userType) {
        this.userType = userType;
    }

}
