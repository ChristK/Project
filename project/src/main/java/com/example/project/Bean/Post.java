package com.example.project.Bean;

public class Post {
    private int _id;
    private String Username;
    private byte[] photo;
    private String comment;
    private String type;
    private float score;
    private double latitude;
    private double longitude;
    private String cityname;
    private String date;



    public Post(int id,byte[] photo,String comment,String type,float score,String date,String cityname){
        super();
        this._id=id;
        this.photo=photo;
        this.comment=comment;
        this.type=type;
        this.score=score;
        this.cityname=cityname;
        this.date=date;
    }

    public Post(String username,byte[] photo,String comment,String type,float score,String date,String cityname){
        super();
        this.Username=username;
        this.photo=photo;
        this.comment=comment;
        this.type=type;
        this.score=score;
        this.cityname=cityname;
        this.date=date;
    }

    public Post(int id,String username,String comment,String type,String time,float score,byte[] photo){
        super();
        this._id=id;
        this.Username=username;
        this.comment=comment;
        this.type=type;
        this.date=time;
        this.score=score;
        this.photo=photo;


    }

    public Post(int id,String username,byte[] photo,String comment,String type,float score,double latitude,double longitude,String cityname,String date){
        super();
        this._id=id;
        this.Username=username;
        this.photo=photo;
        this.comment=comment;
        this.type=type;
        this.score=score;
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

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
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
