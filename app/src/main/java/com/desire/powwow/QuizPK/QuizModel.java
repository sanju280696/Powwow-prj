package com.desire.powwow.QuizPK;

public class QuizModel {

    String A,B,C,D,Que,Ans;

    public QuizModel(String a, String b, String c, String d, String que, String ans) {
        A = a;
        B = b;
        C = c;
        D = d;
        Que = que;
        Ans = ans;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getQue() {
        return Que;
    }

    public void setQue(String que) {
        Que = que;
    }

    public String getAns() {
        return Ans;
    }

    public void setAns(String ans) {
        Ans = ans;
    }
}
