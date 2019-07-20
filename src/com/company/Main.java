package com.company;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static Files files = new Files();
    public static LexicalAnalyzer LA = new LexicalAnalyzer();
    public static  Parser parser = new Parser();

    public static void main(String[] args) throws IOException {
//        files.Reader("SourceCode.txt");
//        LA.ReadLine();
//        LA.Scanner();
//        files.writer();
//        parser.runner();

     //   Parser parser = new Parser();
       parser.parseTableGenerator();

//       text simpleDialogFrame = new text();
//       while( simpleDialogFrame.getFilename().equals("!")){
//
//       }
//        System.out.println(simpleDialogFrame.getFilename());

//        String m = JOptionPane.showInputDialog("Anyone there?");
//        System.out.println(m);
//
//
//        parser.PrintLRT();





      //  parser.CreateBUParseTable();
    //    parser.printBUParseTable();
     //   parser.FillLRTTable();
      //  parser.printLRTTable();
//        CodeGenerator codegen = new CodeGenerator();
//        codegen.push("int");
//        codegen.CDesc();
//        codegen.run("@push" , "a" , 1 );
//        codegen.run("@push" , "b" , 1 );
//        codegen.run("@add" , "p" , 1 );

    }
}
