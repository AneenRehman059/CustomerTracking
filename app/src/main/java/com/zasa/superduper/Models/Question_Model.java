package com.zasa.superduper.Models;

import java.util.ArrayList;

public class Question_Model {
    private String question_name;
    private  String qr_code;
    private ArrayList<String> arrayList ;
    private String type;

    public Question_Model(String question_name, String type) {
        this.question_name = question_name;
        this.type = type;
        this.arrayList = new ArrayList<>();
        this.qr_code = qr_code;
    }

    public String getQuestion_name() {
        return question_name;
    }

    public void setQuestion_name(String question_name) {
        this.question_name = question_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }
}
