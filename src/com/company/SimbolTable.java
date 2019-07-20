package com.company;

public class SimbolTable {


    descriptor Desc;
    String name;

    public SimbolTable(descriptor desc, String name) {
        Desc = desc;
        this.name = name;
    }


    public descriptor getDesc() {
        return Desc;
    }

    public void setDesc(descriptor desc) {
        Desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
