package com.hackatoncivico.rankingpolitico.models;

/**
 * Created by franz on 7/12/2015.
 */
public class FAQ {

    public String titulo;
    public String descripcion;

    public FAQ(){

    }

    public FAQ(String question, String answer){
        this.titulo = question;
        this.descripcion = answer;
    }
}
