package com.company;

public class Code {

    String Opcode;
    String Opr1;
    String Opr2 ;


    String Result ;

    public Code(String opcode) {
        Opcode = opcode;
    }

    public String getOpcode() {
        return Opcode;
    }

    public void setOpcode(String opcode) {
        Opcode = opcode;
    }

    public String getOpr1() {
        return Opr1;
    }

    public void setOpr1(String opr1) {
        Opr1 = opr1;
    }

    public String getOpr2() {
        return Opr2;
    }

    public void setOpr2(String op2) {
        Opr2 = op2;
    }
    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

}
