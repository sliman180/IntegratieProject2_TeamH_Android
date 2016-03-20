package be.kdg.kandoe.kandoeandroid.helpers.adaptermodels;


import java.util.ArrayList;

import be.kdg.kandoe.kandoeandroid.pojo.response.Spelkaart;

public class Parent {

    private String position;

    private int circleLength;

    public int getCircleLength() {
        return circleLength;
    }

    public void setCircleLength(int circleLength) {
        this.circleLength = circleLength;
    }

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
