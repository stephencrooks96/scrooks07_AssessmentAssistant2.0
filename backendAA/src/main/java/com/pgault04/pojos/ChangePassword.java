package com.pgault04.pojos;

public class ChangePassword {

    private String password;
    private String newPassword;
    private String repeatPassword;

    public ChangePassword() {}

    public ChangePassword(String password, String newPassword, String repeatPassword) {
        this.setPassword(password);
        this.setNewPassword(newPassword);
        this.setRepeatPassword(repeatPassword);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
