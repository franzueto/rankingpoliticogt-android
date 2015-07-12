package com.hackatoncivico.rankingpolitico.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by franz on 7/11/2015.
 */
public class Candidato {

    public long id;
    public String nombres;
    public String apellidos;
    public String foto;
    public Puesto puesto;
    public Organizacion organizacion;

    public Candidato(){

    }

}
