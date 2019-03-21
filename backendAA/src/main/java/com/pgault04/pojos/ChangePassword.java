package com.pgault04.pojos;

/**
 * @author Paul Gault - 40126005
 * @since Jan 2019
 * Pojo to allow collection of change password info to be sent to/from front end
 */
public class ChangePassword {

    private String password;
    private String newPassword;
    private String repeatPassword;

    /**
     * Default constructor
     */
    public ChangePassword() {}

    /**
     * Constructor with arguments
     *
     * @param password       - old password
     * @param newPassword    - new password
     * @param repeatPassword - new password repeated
     */
    public ChangePassword(String password, String newPassword, String repeatPassword) {
        this.setPassword(password);
        this.setNewPassword(newPassword);
        this.setRepeatPassword(repeatPassword);
    }

    /**
     * @return gets the old password
     */
    public String getPassword() { return password; }

    /**
     * @param password sets the old password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * @return gets the new password
     */
    public String getNewPassword() { return newPassword; }

    /**
     * @param newPassword sets the new password
     */
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    /**
     * @return gets the repeated new password
     */
    public String getRepeatPassword() { return repeatPassword; }

    /**
     * @param repeatPassword sets the repeated new password
     */
    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    /*
     * the object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChangePassword{");
        sb.append("password='").append(password).append('\'');
        sb.append(", newPassword='").append(newPassword).append('\'');
        sb.append(", repeatPassword='").append(repeatPassword).append('\'');
        sb.append('}');
        return sb.toString();
    }
}