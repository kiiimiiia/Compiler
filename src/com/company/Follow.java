package com.company;

import java.util.ArrayList;

public class Follow {

    String varibale;
    ArrayList<String> Terminal;



    public ArrayList<String> getTerminal() {
        return Terminal;
    }

    public void setTerminal(ArrayList<String> terminal) {
        Terminal = terminal;
        if (getVaribale().equals("P"))
            Terminal.add("$");
    }

    public String getVaribale() {
        return varibale;
    }

    public void setVaribale(String varibale) {
        this.varibale = varibale;
    }

}
