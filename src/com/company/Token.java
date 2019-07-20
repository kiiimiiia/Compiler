package com.company;

import java.util.ArrayList;

public class Token {

    enum TokenType{
        KW,Id,STR,num,ST,SemanticRole
    }

    TokenType Type;
    String value;
    ArrayList<String> KeyWord = new ArrayList<String>();

    public Token ()
    {
        KeyWord.add("if");
        KeyWord.add("while");
        KeyWord.add("do");
        KeyWord.add("for");
        KeyWord.add("main");
        KeyWord.add("return");
        KeyWord.add("int");
        KeyWord.add("float");
        KeyWord.add("double");
        KeyWord.add("char");
        KeyWord.add("else");
        KeyWord.add("boolean");
        KeyWord.add("String");
        KeyWord.add("void");
        KeyWord.add("switch");
        KeyWord.add("case");
        KeyWord.add("main");
        KeyWord.add("default");
        KeyWord.add("break");
        setType(null);
        setValue(null);
    }

    public Token(TokenType type, String value) {
        Type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getType() {
        return Type;
    }

    public void setType(TokenType type) {
        Type = type;
    }
}
