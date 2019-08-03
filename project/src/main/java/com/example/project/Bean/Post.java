package com.example.project.Bean;

public class Post {
    private int _id;
    private String Username;
    private byte[] photo;
    private String comment;
    private String type;
    private double latitude;
    private double longitude;
    private String cityname;
    private String date;
    private String digest;

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public Post(String type,byte[] photo){
        super();
        this.type=type;
        this.photo=photo;
    }
    private Post(byte[] photo){
        super();
        this.photo=photo;
    }


    public Post(int id,byte[] photo,String comment,String type,String date,String cityname){
        super();
        this._id=id;
        this.photo=photo;
        this.comment=comment;
        this.type=type;
        this.cityname=cityname;
        this.date=date;
    }

    public Post(String username,byte[] photo,String comment,String type,String date,String cityname){
        super();
        this.Username=username;
        this.photo=photo;
        this.comment=comment;
        this.type=type;
        this.cityname=cityname;
        this.date=date;
    }

    public Post(int id,String username,String comment,String type,String time,byte[] photo,double latitude,double longitude){
        super();
        this._id=id;
        this.Username=username;
        this.comment=comment;
        this.type=type;
        this.date=time;
        this.photo=photo;
        this.latitude=latitude;
        this.longitude=longitude;

    }

    public Post(int id,String username,byte[] photo,String comment,String type,double latitude,double longitude,String cityname,String date){
        super();
        this._id=id;
        this.Username=username;
        this.photo=photo;
        this.comment=comment;
        this.type=type;
        this.latitude=latitude;
        this.longitude=longitude;
        this.cityname=cityname;
        this.date=date;

    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
