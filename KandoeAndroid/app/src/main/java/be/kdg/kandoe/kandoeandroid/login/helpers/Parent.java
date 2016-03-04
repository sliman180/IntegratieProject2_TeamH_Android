package be.kdg.kandoe.kandoeandroid.login.helpers;


import java.util.ArrayList;

import be.kdg.kandoe.kandoeandroid.login.pojo.Spelkaart;

public class Parent {

    private String position;

    private ArrayList<Spelkaart> children;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ArrayList<Spelkaart> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Spelkaart> children) {
        this.children = children;
    }
}
