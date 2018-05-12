package com.thericebag.application.application.beans;

/**
 * Created by abhil on 22-09-2016.
 */
public class UserProfileAPIBean {
    UserProfileBean profile;
    int success;

    public UserProfileBean getProfile() {
        return profile;
    }

    public void setProfile(UserProfileBean profile) {
        this.profile = profile;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
