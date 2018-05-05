package com.cabrera.parcial_v2.Modelo;

import java.io.Serializable;
//This is the main class
public class Contacto implements Serializable {
    private String name;
    private String number;
    private String imageUri;
    private String email;
    private String type;
    private boolean fav;
    private boolean searched;

    public Contacto(String name, String number, String imageUri,String email,boolean fav, String type){
        this.name = name;
        this.number = number;
        this.imageUri = imageUri;
        this.email = email;
        this.fav=fav;
        searched=false;
        this.type=type;
    }
    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getImageUri() {
        return imageUri;
    }

    public boolean isFav() {
        return fav;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public boolean isSearched() {
        return searched;
    }

    public void setSearched(boolean searched) {
        this.searched = searched;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }
}

