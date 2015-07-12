package com.hackatoncivico.rankingpolitico.models;

/**
 * Created by franz on 7/12/2015.
 */
public class FAQ {

    public String question;
    public String answer;

    public FAQ(){

    }

    public FAQ(String question, String answer){
        this.question = question;
        this.answer = answer;
    }
}
