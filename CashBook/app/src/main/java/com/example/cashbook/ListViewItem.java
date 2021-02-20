package com.example.cashbook;

public class ListViewItem {
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getInputNum() {
        return inputNum;
    }

    public void setInputNum(String inputNum) {
        this.inputNum = inputNum;
    }


    String date;
    String kind;
    String Content;
    String cash;
    String inputNum;
}
