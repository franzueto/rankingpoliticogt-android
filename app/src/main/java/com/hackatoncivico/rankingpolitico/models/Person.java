package com.hackatoncivico.rankingpolitico.models;

/**
 * Created by franz on 7/11/2015.
 */
public class Person {

    private String name;
    private String puesto;
    private int photoId;

    public Person(String name, String puesto, int photoId) {
        this.name = name;
        this.puesto = puesto;
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }

    public String getPuesto() {
        return puesto;
    }

    public int getPhotoId() {
        return photoId;
    }
}
