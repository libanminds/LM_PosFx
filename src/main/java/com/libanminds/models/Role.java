package com.libanminds.models;

public class Role {
    private int ID;
    private String name;

    public Role(int id, String name) {
        this.ID = id;
        this.name = name;
    }

    public int getID() { return ID; }
    public String getName() { return name; }
}
