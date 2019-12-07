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


    @Override
    public boolean equals(Object other) {
        if (this == other) return true ;

        if (! (other instanceof Role)) return false ;

        Role otherRole = (Role) other ;
        return this.ID == otherRole.ID;
    }

    @Override
    public String toString() {
        return name;
    }
}
