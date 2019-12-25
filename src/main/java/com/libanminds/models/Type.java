package com.libanminds.models;

public class Type {
    private int ID;
    private String name;

    public Type(int id, String name) {
        this.ID = id;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String val) {
        name = val;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (!(other instanceof Type)) return false;

        Type otherCategory = (Type) other;
        return this.ID == otherCategory.ID;
    }

    @Override
    public String toString() {
        return name;
    }
}
