package com.example.project.DAO;

public class UserBean {
    private int id;
    private String Username;
    private String Password;
    private String Email;
    private String Image;
    private String Location;
    private double Longitute;
    private double Latitute;
    private String Time;
    private String Text;

    public UserBean() {
    }

    public UserBean(int id, String username, String password, String email, String image, String location, double longitute, double latitute, String time, String text) {
        this.id = id;
        this.Username = username;
        this.Password = password;
        this.Email = email;
        this.Image = image;
        this.Location = location;
        this.Longitute = longitute;
        this.Latitute = latitute;
        this.Time = time;
        this.Text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public double getLongitute() {
        return Longitute;
    }

    public void setLongitute(double longitute) {
        Longitute = longitute;
    }

    public double getLatitute() {
        return Latitute;
    }

    public void setLatitute(double latitute) {
        Latitute = latitute;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

}
