package com.company;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {

    //Token = id , num , special token , key word , white space , comment


    Files files = new Files();
    public ArrayList<Token> Tokens = new ArrayList<Token>();
    public Main main = new Main();
    String str;
    int Line = 0;
    char character;
    public String [] splited ;
    //    int WCounter = 0;
    int ChCounter = 0;
    public Token token = new Token();
    public StringBuilder Word = new StringBuilder();


    public void ReadChar()
    {
        if (ChCounter < str.length())
        {
//           System.out.println(ChCounter);
            character =  str.charAt(ChCounter++);
//           System.out.println(character + " in last readline");
        }
        else
        {
//           System.out.println("in else");
            ChCounter = 0;
            ReadLine(1);
        }
    }

    public void ReadLine (int time)
    {
        if (time == 0)
        {

            files.Reader("SourceCode.txt");
        }
//        System.out.println(files.getRecords().size());
        if (!files.getRecords().isEmpty())
        {
//            System.out.println("read line");
            Line++;
            str = files.records.get(0);
//            System.out.println(str.length());
            files.records.remove(0);
//            System.out.println(files.getRecords().size());
            ChCounter = 0;
            ReadChar();
        }
        else
        {
//            System.out.println(files.getRecords().isEmpty());
            character = '$';
        }
    }

    public boolean FindKeyWord(String word)
    {
        for (int i = 0; i < token.KeyWord.size() ; i++)
        {
            if (token.KeyWord.get(i).equals(word))
                return true;
        }
        return false;
    }

    public Token Scanner() {
//        System.out.println(character);
        //while (character != '$')
        //{
//        System.out.println(character);
        token = new Token();
        switch (character){
            case '+':                                                       // + or += or ++
                ReadChar();
                if (character == '+') {
                    token.setType(Token.TokenType.ST);
                    token.setValue("++");
                    ReadChar();
                    break;
                } else if (character == '=') {
                    token.setType(Token.TokenType.ST);
                    token.setValue("+=");
                    ReadChar();
                    break;
                } else {
                    token.setType(Token.TokenType.ST);
                    token.setValue("+");
                    break;
                }
            case '-':                                                       // - or -= or --
                ReadChar();
                if (character == '-') {
                    token.setType(Token.TokenType.ST);
                    token.setValue("--");
                    ReadChar();
                    break;
                } else if (character == '=') {
                    token.setType(Token.TokenType.ST);
                    token.setValue("-=");
                    ReadChar();
                    break;
                } else {
                    token.setType(Token.TokenType.ST);
                    token.setValue("-");
                    break;
                }
            case '(':                                                       // (
                ReadChar();
                token.setType(Token.TokenType.ST);
                token.setValue("(");
                break;
            case ')':                                                       // )
                ReadChar();
                token.setType(Token.TokenType.ST);
                token.setValue(")");
                break;
            case '{':                                                       // {
                ReadChar();
                token.setType(Token.TokenType.ST);
                token.setValue("{");
                break;
            case '}':                                                       // }
                ReadChar();
                token.setType(Token.TokenType.ST);
                token.setValue("}");
                break;
            case ';':                                                       // ;
                ReadChar();
                token.setType(Token.TokenType.ST);
                token.setValue(";");
                break;
            case '<':                                                       // < or <=
                ReadChar();
                if (character == '=') {
                    token.setType(Token.TokenType.ST);
                    token.setValue("<=");
                    ReadChar();
                    break;
                } else {
                    token.setType(Token.TokenType.ST);
                    token.setValue("<");
                    break;
                }
            case '>':                                                       // > or >=
                ReadChar();
                if (character == '=') {
                    token.setType(Token.TokenType.ST);
                    token.setValue(">=");
                    ReadChar();
                    break;
                } else {
                    token.setType(Token.TokenType.ST);
                    token.setValue(">");
                    break;
                }
            case '=':                                                       // == or =
                ReadChar();
                if (character == '=') {
                    token.setType(Token.TokenType.ST);
                    token.setValue("==");
                    ReadChar();
                    break;
                } else {
                    token.setType(Token.TokenType.ST);
                    token.setValue("=");
                    break;
                }
            case '!':
                ReadChar();
                if (character == '=') {
                    token.setType(Token.TokenType.ST);
                    token.setValue("!=");
                    ReadChar();
                    break;
                } else {
                    token.setType(Token.TokenType.ST);
                    token.setValue("!");
                    break;
                }
            case '/':                                                           // // or /* or /
//                    System.out.println("in / hastim");
                ReadChar();
//                    System.out.println(character);
                if (character == '/')
                {
//                        System.out.println(character);
                    ReadLine(1);
                    break;
                }
                else if (character == '*')
                {
//                        System.out.print(character);
                    ReadChar();
//                        System.out.print(character);
                    do {
                        while (character != '*') {
                            ReadChar();
//                                System.out.print(character);
                        }
                        ReadChar();
//                            System.out.print(character);
                    } while (character != '/');
                    ReadChar();
//                        System.out.println(character);
                }
                else
                {
                    token.setType(Token.TokenType.ST);
                    token.setValue("/");
                    break;
                }
                break;
            case '*':                                                          // * or *=
                ReadChar();
                if (character == '=') {
                    token.setType(Token.TokenType.ST);
                    token.setValue("*=");
                    ReadChar();
                    break;
                } else {
                    token.setType(Token.TokenType.ST);
                    token.setValue("*");
                    break;
                }
            case '$':
                token.setType(Token.TokenType.ST);
                token.setValue("$");
                break;
            case '@':
                Word = new StringBuilder();
                while (character != ' ')
                {
                    Word.append(character);
                    ReadChar();
                }
                token.setValue(Word.toString());
                token.setType(Token.TokenType.SemanticRole);
            default:
                if (character != ' ' && character != '$' && character != '\t')
                {
                    do {
                        Word.append(character);
                        if (ChCounter < str.length())
                            ReadChar();
                        else {
                            break;
                        }
                    }while (character != ' ' && character != '$');
                    if (FindKeyWord(Word.toString())) {
                        token.setType(Token.TokenType.KW);
                        token.setValue(Word.toString());
                        Word = new StringBuilder();
                        if (character != '$')
                            ReadChar();
                        break;
                    } else if (Word.charAt(0) == '1' | Word.charAt(0) == '2' | Word.charAt(0) == '3' | Word.charAt(0) == '4'
                            | Word.charAt(0) == '5' | Word.charAt(0) == '6' | Word.charAt(0) == '7' | Word.charAt(0) == '8'
                            | Word.charAt(0) == '9' | Word.charAt(0) == '0' | Word.charAt(0) == '.') {
                        token.setType(Token.TokenType.num);
                        token.setValue("num");
                        Word = new StringBuilder();
                        if (character != '$')
                            ReadChar();
                        break;
                    } else
                    {
                        token.setType(Token.TokenType.Id);
                        token.setValue(Word.toString());
                        Word = new StringBuilder();
                        if (character != '$')
                            ReadChar();
                        break;
                    }
                } else
                {
                    if (character != '$')
                    {
                        ReadChar();
                        Scanner();
                    }
                }
        }
//            if (token.getType() != null)
//            {
////                Tokens.add(token);
//                return token;
//            }
//            if (character == '$')
//                System.out.println("end of files");
        //}
//        token = new Token();
//        System.out.println(token.getValue() + " " + token.getType());
        return token;
    }

    public List<Token> getTokens (){
        return Tokens;
    }
}
