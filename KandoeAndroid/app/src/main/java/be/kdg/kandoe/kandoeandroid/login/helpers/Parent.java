package be.kdg.kandoe.kandoeandroid.login.helpers;


import java.util.ArrayList;

public class Parent {

    private String position;

    private ArrayList<Child> children;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }
}
