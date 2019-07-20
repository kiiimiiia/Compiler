package com.company;

import java.util.ArrayList;

public class Predict {


    int number;
    String variable;
    ArrayList<String> Terminal;

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }



    public ArrayList<String> getTerminal() {
        return Terminal;
    }

    public void setTerminal(ArrayList<String> terminal) {
        Terminal = terminal;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTerminalByIndex(int index){
        return  Terminal.get(index);

    }
}
