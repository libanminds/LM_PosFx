package com.libanminds.models;

public class ItemCategory{
    private int ID;
    private String name;

    public ItemCategory(int id, String name) {
        this.ID = id;
        this.name = name;
    }

    public int getID() { return ID; }
    public String getName() { return name; }
    public void setName(String val) {name = val;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true ;

        if (! (other instanceof ItemCategory)) return false ;

        ItemCategory otherCategory = (ItemCategory) other ;
        return this.ID == otherCategory.ID;
    }

    @Override
    public String toString() {
        return name;
    }
}
