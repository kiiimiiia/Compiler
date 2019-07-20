package com.company;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;



public class Parser {

    Files GrammerFile;
    List<String> GrammerRawList =  new ArrayList<String>();
    List<Grammer> RHST = new ArrayList<Grammer>();
    List<Grammer> TempGrammer = new ArrayList<Grammer>();
    ArrayList<First> Firsts = new ArrayList<First>();
    ArrayList<Follow> Follows = new ArrayList<Follow>();
    private String [] splitedRightAndLeft ;
    private Grammer grammer ;
    private String [] reversed ;
    int start , end;
    String temp1;
    private First firstEl;
    private String [] terminalsColumn = {"(",")","{","}",";","+","++","-","--","*",",","=","[","]","$","id","num","float","int","char",
            "double","bool","if","else","for","while","do","void","case","character","%","break","main","default",":","switch","~","/"};
    private ArrayList<String> variable = new ArrayList<String>();
    private Follow followEl;
    private String [][] ParseTable ;
    private Predict predictEl;
    private ArrayList<Predict> Predicts = new ArrayList<Predict>();
    boolean isLL1 = true;
    public Stack<String> ParsStack = new Stack<String>();
    private Stack<String> SLRParsStack = new Stack<String>();
    private LexicalAnalyzer LA = new LexicalAnalyzer();

    private Token token;
    /** ino bara in gozashtam ke befahmim to kodom soton jadval SLR pareser bayad berim bayad ba terminal haye grammer boolean por beshe
     * be hamon tartibi ke to SLRTable hastan **/
    private String [] SLRterminal = {"&",")","(","id","/","*","-","+","~=","==",">=","<=",">","<","~","&","|","BE","BT","BF","T","F","E"};

    String [][] LRTTable = new String[21][2];
    String [][] BUParseTable = new String[42][24];

    private Stack<String> semanticStack = new Stack<String>();




    CodeGenerator CodeGen = new CodeGenerator();


    boolean terminat = false ;


    public void parseTableGenerator() throws IOException {
        GrammerFile = new Files();
       String m = JOptionPane.showInputDialog("whats is the name of your grammer file?" + "hint: Grammer.txt"); //"Grammer.txt"
        GrammerFile.Reader(m);
        Grammer temp = new Grammer();
        for (int i = 0; i < GrammerFile.getRecords().size(); i++)
        {
            String [] tempString = GrammerFile.getRecords().get(i).split("::");
            String [] gramer  = tempString[1].split(" ");
            temp.setLeft(tempString[0]);
            temp.setRight(gramer);
            TempGrammer.add(temp);
            temp = new Grammer();
        }
        System.out.println("RHST :");
        FillRHST();

        System.out.println();
        FirstTableGenerator();

        System.out.println();
        FollowTableGenerator();

        System.out.println();
        PredictTableGenerator();

        System.out.println();
        GenerateParseTable();

        System.out.println("Pars Table :");
        FillParseTableWithPredicts();

        System.out.println();

        FillLRTTable();
        CreateBUParseTable();

        GrammerFile = new Files();
         m = JOptionPane.showInputDialog("whats is the name of your grammer file with semntic rules?" + "hint: GrammerWithSemanticRole.txt");
        GrammerFile.Reader(m);
        temp = new Grammer();
        TempGrammer = new ArrayList<Grammer>();
        for (int i = 0; i < GrammerFile.getRecords().size(); i++)
        {
            String [] tempString = GrammerFile.getRecords().get(i).split("::");
            String [] gramer  = tempString[1].split(" ");
            temp.setLeft(tempString[0]);
            temp.setRight(gramer);
            TempGrammer.add(temp);
//            System.out.println(Arrays.toString(gramer));
            temp = new Grammer();
        }
        CreateBUParseTable();
        LL1Parser();

    }

    private void FillRHST(){

        int i = 0 ;
        boolean flag = false;
        GrammerRawList = GrammerFile.getRecords();
        grammer = new Grammer();
        while(!GrammerRawList.isEmpty()) {
            reversed = null;
            splitedRightAndLeft = GrammerRawList.get(0).split("::");
            reversed  = splitedRightAndLeft[1].split(" ");
//            if(splitedRightAndLeft[0].equals("BE"))
//                flag = true;
//            if(flag) {
//                LRTTable[i][0] = splitedRightAndLeft[0];
//                LRTTable[i][1] = Integer.toString(reversed.length);
//                i++;
//            }
            grammer.setLeft(splitedRightAndLeft[0]);
            grammer.setRight(reversed);
            start = 0 ;
            end   = (reversed.length) - 1;
            while (start < end)
            {
                temp1 = reversed[start];
                reversed[start] = reversed[end];
                reversed[end] = temp1;
                start++;
                end--;
            }
            grammer.setRight( reversed );
            System.out.println(Arrays.toString(reversed));
            RHST.add(grammer);
            GrammerRawList.remove(0);
            grammer = new Grammer();
        }
    }

    private void FirstTableGenerator() {
        firstEl = new First();
        System.out.println("First of each grammer :");
        for (int i = 0; i < TempGrammer.size(); i++)
        {
            firstEl.setVaribale(TempGrammer.get(i).getLeft());
            System.out.print(TempGrammer.get(i).getLeft() + ":{");
            firstEl.setTerminal(getFirst(TempGrammer.get(i).getRight()));
            Firsts.add(firstEl);
            for (int j = 0; j < firstEl.getTerminal().size(); j++) {
                System.out.print(firstEl.getTerminal().get(j));
                if (j < firstEl.getTerminal().size() - 1)
                {
                    System.out.print(",");
                }
            }
            System.out.println("}");
            firstEl = new First();
        }
    }




    private ArrayList<String> getFirst (String [] grammer)
    {
        ArrayList<String> first = new ArrayList<String>();
        if (CheckTerminal(grammer[1]))
        {
            first.add(grammer[1]);
        }
        else
        {
            for (int i = 1; i < grammer.length; i++)
            {
                if (isNullable(grammer[i]))
                {
                    for (int j = 0; j <TempGrammer.size(); j++)
                    {
                        if (TempGrammer.get(j).getLeft().equals(grammer[i])) {
                            first.addAll(getFirst(TempGrammer.get(j).getRight()));
                            break;
                        }
                    }
                }
                else {
                    for (int j = 0; j < TempGrammer.size(); j++) {
                        if (TempGrammer.get(j).getLeft().equals(grammer[i])) {
                            first.addAll(getFirst(TempGrammer.get(j).getRight()));
                        }
                    }
                    break;
                }
            }
        }
        return first;
    }

    private boolean CheckTerminal (String input)
    {
        String [] terminals = {"(",")","{","}",";","+","++","-","--","*",",","=","[","]","id","num","float","int","char","/",
                "double","bool","if","else","for","while","do","void","case","character","%","break","main","default",":","switch","~"};
        for (int i = 0; i < terminals.length ; i++)
        {
            if (terminals[i].equals(input))
                return true;
        }
        return false;
    }

    private boolean BUCheckTerminal (String input)
    {
        String [] SLRterminal = {"&",")","(","id","/","*","-","+","~=","==",">=","<=",">","<","~","&","|","BE","BT","BF","T","F","E"};
        for (int i = 0; i < SLRterminal.length ; i++)
        {
            if (SLRterminal[i].equals(input))
                return true;
        }
        return false;
    }


    private boolean isNullable (String var)
    {
        for (int i = 0; i < TempGrammer.size(); i++)
        {
            if (TempGrammer.get(i).getLeft().equals(var))
            {
                if (TempGrammer.get(i).getRight()[1].equals("!"))
                    return true;
            }
        }
        return false;
    }

    private void FollowTableGenerator(){
        System.out.println();
        boolean flag = false;

        for (int i = 0 ; i<TempGrammer.size() ; i++)
        {
            for (int j = 0; j<variable.size() ; j++)
            {
                if ( TempGrammer.get(i).getLeft().equals(variable.get(j))){
                    flag = true;
                    break;
                }
            }

            if( !flag ) {
                variable.add(TempGrammer.get(i).getLeft());
            }
            flag = false ;
        }

        System.out.println("Follow of each Variable :");
        for (int i = 0;i<variable.size() ; i++)
        {
            boolean add = true;
            boolean Check = false;
            ArrayList<String> FinalFollow = new ArrayList<String>();
            ArrayList<String> TempFollow ;
            followEl = new Follow();
//            for (int k = 0; k < Follows.size(); k++)
//            {
//                if (Follows.get(k).getVaribale().equals(variable.get(i)))
//                {
//                    Check = true;
//                    break;
//                }
//            }
            followEl.setVaribale(variable.get(i));
            TempFollow = getFollow(variable.get(i));
//                for (int j = 0; j < TempFollow.size(); j++) {
//                    for (int k = 0; k < FinalFollow.size(); k++) {
//                        if (FinalFollow.get(k).equals(TempFollow.get(j)))
//                            add = false;
//                    }
//                    if (add) {
//                        FinalFollow.add(TempFollow.get(j));
//                    }
//                }
            followEl.setTerminal(TempFollow);
            Follows.add(followEl);

//            System.out.println(variable.get(i));

        }
        boolean change = true;
        while (change) {
            change = false;
            for (int i = 0; i < Follows.size(); i++) {
                for (int j = 0; j < Follows.size(); j++) {
                    if (i == j)
                        continue;
                    if (Follows.get(i).getVaribale().equals(Follows.get(j).getVaribale())) {
                        Follows.get(i).getTerminal().addAll(Follows.get(j).getTerminal());
                        Follows.remove(j);
                        change = true;
                    }
                }
            }
        }
        change = true;
        while (change) {
            change = false;
            for (int i = 0; i < Follows.size(); i++) {
                for (int j = 0; j < Follows.get(i).getTerminal().size(); j++) {
                    String terminal = Follows.get(i).getTerminal().get(j);
                    for (int k = 0; k < Follows.get(i).getTerminal().size(); k++) {
                        if (j == k)
                            continue;
                        if (terminal.equals(Follows.get(i).getTerminal().get(k))) {
                            Follows.get(i).getTerminal().remove(k);
                            change = true;
                        }
                    }
                }
            }
        }



        for (int i = 0; i< Follows.size(); i++)
        {
            System.out.print(Follows.get(i).getVaribale() + ":");
            System.out.println(Arrays.toString(Follows.get(i).getTerminal().toArray()));
        }

    }

    private ArrayList<String>  getFollow(String var){
        ArrayList<String> follow = new ArrayList<String>();
//        System.out.println(var);
        for(int i = 0 ; i<TempGrammer.size() ; i++)
        {
            String [] RighSide = TempGrammer.get(i).getRight();
            for(int j = 0 ; j<RighSide.length ; j++ )
            {
                if(RighSide[j].equals(var) )
                {
                    String next = RighSide[j+1];
//                    System.out.println(next + " next is");
                    if(next.equals("_"))
                    {
                        if (!TempGrammer.get(i).getLeft().equals(var))
                        {
                            Follow follow1 = new Follow();
                            follow1.setVaribale(TempGrammer.get(i).getLeft());
                            follow1.setTerminal(getFollow(TempGrammer.get(i).getLeft()));
                            Follows.add(follow1);
                            follow.addAll(follow1.getTerminal());
                        }
                    }
                    else
                    {
                        if (CheckTerminal(next))
                        {
                            follow.add(next);
                        }
                        else
                        {
                            for (int h = 0; h < Firsts.size(); h++) {
                                if (next.equals(Firsts.get(h).getVaribale())) {
                                    follow.addAll(Firsts.get(h).getTerminal());
                                    if (isNullable(next)) {
                                        if (!TempGrammer.get(i).getLeft().equals(var)) {
                                            Follow follow1 = new Follow();
                                            follow1.setVaribale(TempGrammer.get(i).getLeft());
                                            follow1.setTerminal(getFollow(TempGrammer.get(i).getLeft()));
                                            Follows.add(follow1);
                                            follow.addAll(follow1.getTerminal());
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        return follow;
    }

    private void PredictTableGenerator()
    {
        for(int i =0 ; i<TempGrammer.size(); i++)
        {
            predictEl = new Predict();
            predictEl.setTerminal(getPredict(TempGrammer.get(i), i));
            predictEl.setVariable(TempGrammer.get(i).getLeft());
            predictEl.setNumber(i+1);
            Predicts.add(predictEl);
        }
        System.out.println("Predicts of each Grammer :");
        for (int i = 0; i < Predicts.size(); i++)
        {
            System.out.print(Predicts.get(i).getNumber() + ": ");
            System.out.println(Arrays.toString(Predicts.get(i).getTerminal().toArray()));
        }

    }

    private ArrayList<String> getPredict(Grammer grammer, int number){
        ArrayList<String> Predicts = new ArrayList<String>();
        if (Firsts.get(number).getTerminal().size() == 0)
        {
            for (int i = 0; i < Follows.size(); i++)
            {
                if (Follows.get(i).getVaribale().equals(grammer.getLeft()))
                {
                    Predicts.addAll(Follows.get(i).getTerminal());
                    break;
                }
            }
        }
        else
        {
            Predicts.addAll(Firsts.get(number).getTerminal());
        }
        return Predicts;

    }

    private void FillParseTableWithPredicts(){
        boolean isll1 ;
        for(int i=0 ;i<Predicts.size() ; i++){
            for ( int j = 0 ;j<Predicts.get(i).getTerminal().size() ; j++){
//                System.out.println(Predicts.get(i).getVariable()  + " " + Predicts.get(i).getTerminalByIndex(j) + " " + Integer.toString(Predicts.get(i).getNumber()));
                isll1 = AddToParseTable(Predicts.get(i).getVariable() ,Predicts.get(i).getTerminalByIndex(j),Integer.toString(Predicts.get(i).getNumber()));
                if( ! isll1){
                    System.out.println("This grammer is not LL1 because of " + " " + i + " " + "row" + " " + j + " " + "column" + " of pareTable" );
                }
            }
        }

        PrintParseTable();


    }

    private boolean AddToParseTable(String var , String terminal , String num){
        for(int i =0 ; i<variable.size()  ; i++ )
        {
            if(var.equals(variable.get(i))){
                for(int j = 0 ; j<terminalsColumn.length; j++){
                    if(terminal.equals(terminalsColumn[j])){
                        String str = "";
                        if(ParseTable[i+1][j+1].equals("0")){
                            str = num ;
                            ParseTable[i+1][j+1] = num ;
                        }
                        else
                        {
                            str = ParseTable[i+1][j+1];
                            str = str + "," + num ;
                            ParseTable[i+1][j+1] = str ;
                            isLL1 = false;
                        }

                        return  isLL1;
                    }
                }
            }
        }


        System.out.println("error:no such var or terminal does exist var is : " + var + "terminal is : " + terminal );
        return isLL1;
    }

    private void GenerateParseTable(){
        ParseTable = new String [variable.size() + 1][terminalsColumn.length + 1];
        for(int i=0 ; i<=variable.size() ; i++){
            for(int j = 0 ;j<=terminalsColumn.length ; j++){
                ParseTable[i][j] = "0";
            }
        }

        for (int i = 1 ; i <= variable.size() ; i++)
            ParseTable[i][0] = variable.get(i - 1);
        for(int i = 1 ; i<= terminalsColumn.length ; i++)
            ParseTable[0][i] = terminalsColumn[i - 1];
    }
    private void PrintParseTable(){
        String str  = "";
        ParseTable[0][0] = "Pars";
        System.out.println();
        for(int i=0 ; i<=variable.size() ; i++){
            for(int j = 0 ;j<=terminalsColumn.length ; j++){
                str += ParseTable[i][j];
                int s = 12 - ParseTable[i][j].length();
                while (s > 0)
                {
                    str += " ";
                    s--;
                }
            }
            System.out.println(str);
            str = "";
        }
    }

    private int getProduction(String Top,Token token)
    {
        for (int i = 0; i < variable.size(); i++)
        {
            if (variable.get(i).equals(Top))
            {
                for (int j = 0; j < terminalsColumn.length; j++)
                {
                    if (!CheckTerminal(token.getValue()) && (!token.getValue().equals("$")))
                    {
                        int k;
                        for (k = 0 ; k < terminalsColumn.length ; k++)
                        {
                            if (terminalsColumn[k].equals("id"))
                                break;
                        }
                        return Integer.parseInt(ParseTable[i + 1][k + 1]);
                    }
                    else if (terminalsColumn[j].equals(token.getValue()))
                    {
                        return Integer.parseInt( ParseTable[i + 1][j + 1]);
                    }
                }
            }
        }
        return 0;
    }


    private void LL1Parser() throws IOException {
//        System.out.println("in LL1 parser");

        //GetBuParseTableFromExel();

        LA.ReadLine(0);
        ParsStack.push("$");
        ParsStack.push("P");
        int prod;
        token = LA.Scanner();
        while (token.getValue() == null)
        {
            token = LA.Scanner();
        }
//        System.out.println("in ll1 parser");
//        System.out.println(token.getValue() + " " + token.getType());
        while (true)
        {
//            System.out.println(ParsStack.peek());
            if (!CheckTerminal(ParsStack.peek()) && (!ParsStack.peek().equals("$")) && (ParsStack.peek().charAt(0) != '@'))
            {
                /**inja be nazarm bayad BUparser run she agar BE bashe sare parse stack**/
                if (ParsStack.peek().equals("BE"))
                {
//                    System.out.println("go to SLR parser");
                    SLRParser();
//                    System.out.println("Come back from SLR parser");
//                    System.out.println(ParsStack.peek());
                    ParsStack.pop();
                    token = LA.Scanner();
//                    System.out.println(token.getValue());
                }
                else
                {
                    prod = getProduction(ParsStack.peek(), token);
//                    System.out.println(ParsStack.peek() + " " + token.getValue() + " " + prod);
                    if (prod == 0) {
                        error(4);
                        break;
                    } else {
                        ParsStack.pop();
                        for (int i = TempGrammer.get(prod - 1).getRight().length - 2 ; i > 0 ; i--) {
                            if (TempGrammer.get(prod - 1).getRight()[i].equals("!"))
                                break;
                            ParsStack.push(TempGrammer.get(prod - 1).getRight()[i]);
//                            System.out.println("fill stack " + ParsStack.peek());
                        }
                    }
                }
            }
            else if (CheckTerminal(ParsStack.peek()))
            {
                if (!CheckTerminal(token.getValue()))
                {
                    if (ParsStack.peek().equals("id"))
                    {
//                        System.out.println(token.getValue() + " pop");
                        ParsStack.pop();
                        token = LA.Scanner();
                        while (token.getValue() == null)
                        {
                            token = LA.Scanner();
                        }
//                        System.out.println("new token is " + token.getValue());
                    }
                    else
                    {
                        if (token.getValue().equals("$"))
                        {
                            error(1);
                            break;
                        }
                    }
                }
                else if (ParsStack.peek().equals(token.getValue()))
                {
//                    System.out.println(token.getValue() + " pop");
                    ParsStack.pop();
                    token = LA.Scanner();
                    while (token.getValue() == null)
                    {
                        token = LA.Scanner();
                    }
//                    System.out.println("new token is " + token.getValue());

                }
                else
                {
                    error(1);
                    break;
                }
            }
            /**inja ham ye else if dige bashe baraye @semanticrule ha ke codeGenerator seda zade beshe**/
            /** nazar man ro injast **/
            else if (ParsStack.peek().charAt(0) == '@')
            {
//                System.out.println("see semantic role");
                if (ParsStack.peek().equals("@push"))
                    terminat = CodeGen.run( ParsStack.pop() , token.getValue() , LA.Line - 1);
                else
                    terminat = CodeGen.run( ParsStack.pop() , ParsStack.peek() , LA.Line - 1);


                if(terminat)
                    Terminate();
                /** code generation inja seda mishe **/
            }
            else if (ParsStack.peek().equals("$"))
            {
                if (token.getValue().equals("$"))
                {
                    Accept();
                    break;
                }
                else {
                    error(2);
                    break;
                }
            }
            else {
                error(3);
                break;
            }
        }

    }

    public void Accept () throws IOException {
        System.out.println("Successful parsing ");
        CodeGen.WriteToFile();
    }

    public void error (int Type)
    {
        switch (Type)
        {
            case 1:
                System.out.println("Error in Line : " + LA.Line + " expected " + ParsStack.peek());
                break;
            case 2:
                System.out.println("expected end of file");
                break;
            case 3:
                System.out.println("error in ParsStack");
                break;
            default: {
                System.out.println("error in line " + (LA.Line - 1));
            }
        }
    }

//    public void PrintLRT(){
//        System.out.println("this is LRT table:");
//        for( int i = 0 ; i< 11 ; i++){
//            System.out.println(LRTTable[i][0] + "  " + LRTTable[i][1]);
//
//        }
//    }

    public void Terminate() throws IOException {
        System.out.println("Compilation Error");
        CodeGen.WriteToFile();
        System.exit(0);
    }

    public void SLRParser () throws IOException {
//        System.out.println("in SLR");
//        System.out.println("line is " + LA.Line);
        char Action ;
        String tableOutput;
        int num;
        int i;
        SLRParsStack.push("0");
        while (!token.getValue().equals("&")) {
//            System.out.println(token.getType() + " " + token.getValue());
            for (i = 0; i < SLRterminal.length; i++)
            {
                if (!BUCheckTerminal(token.getValue()))
                {
                    if (SLRterminal[i].equals("id")) {
                        break;
                    }
                }
                else {
                    if (SLRterminal[i].equals(token.getValue()))
                        break;
                }
            }
//            System.out.println(SLRParsStack.peek() + " " + i);
            tableOutput = BUParseTable[Integer.parseInt(SLRParsStack.peek()) + 1][i + 1];
//            System.out.println(tableOutput);
            Action = tableOutput.charAt(0);
            if (tableOutput.length() == 3)
            {
                num = Character.getNumericValue(tableOutput.charAt(1));
                num *= 10;
                num += Character.getNumericValue(tableOutput.charAt(2));
            }
            else
            {
                num = Character.getNumericValue(tableOutput.charAt(1));
            }
//            System.out.println(token.getValue());
//            System.out.println(Action);
//            System.out.println(num);
            switch (Action) {
                case 's':
//                    System.out.println("in shift action");
                    SLRParsStack.push(String.valueOf(num));
                    token = LA.Scanner();
//                    System.out.println(token.getValue());
                    break;
                case 'r':
//                    System.out.println("in reduce action");
                    int numberOfVar = Integer.parseInt(LRTTable[num - 1][1]);
//                    System.out.println(SLRParsStack.size());
                    while (numberOfVar > 0) {
                        SLRParsStack.pop();
                        numberOfVar--;
                    }
//                    System.out.println("out of while");
//                    System.out.println(LRTTable[num - 1][0]);
                    for (i = 0; i < SLRterminal.length; i++)
                    {
                        if (SLRterminal[i].equals(LRTTable[num - 1][0]))
                            break;
                    }
//                    System.out.println(i);
//                    System.out.println(SLRParsStack.isEmpty() + " size stack");
                    tableOutput = BUParseTable[Integer.parseInt(SLRParsStack.peek()) + 1][i + 1];
//                    System.out.println(tableOutput);
                    SLRParsStack.push(String.valueOf(tableOutput.charAt(1)));
                    break;
                case 'a':
//                    System.out.println("in accept");
                    Accept();
                    return;
                case 'e':
//                    System.out.println("in error");
                    error(4);
            }
        }

    }




    public void CreateBUParseTable(){
        for(int i= 0; i<42 ; i++){
            for(int j = 0 ; j<24 ; j++)
                BUParseTable[i][j] = "0";
        }

        Files BUParseT = new Files();
        String m = JOptionPane.showInputDialog("whats is the name of your parseTableFile?" + "hint: BUPT.txt");
        BUParseT.Reader(m);
        List<String> records = BUParseT.getRecords();

        for(int i = 0 ; i<records.size() ; i++) {

            String[] res = records.get(i).split("\\t");
//            System.out.println();
//            System.out.println(res [0] + "   "  + res.length);
            int j;
            for (j = 0; j < res.length; j++)
                BUParseTable[i][j] = res[j];

        }

    }


    public void printBUParseTable(){
        String str  = "";
        BUParseTable[0][0] = "BUPT";
        System.out.println();
        for(int i=0 ; i<=41 ; i++){
            for(int j = 0 ;j<=23 ; j++){
                str += BUParseTable[i][j];
                int s = 12 - BUParseTable[i][j].length();
                while (s > 0)
                {
                    str += " ";
                    s--;
                }
            }
            System.out.println(str);
            str = "";
        }
    }




    public void FillLRTTable(){

        Files LRTTAB = new Files();
        String m = JOptionPane.showInputDialog("whats is the name of your SlrParser GrammerFile?" + "hint: BGrammer.txt");
        LRTTAB.Reader(m);
        List<String> records = LRTTAB.getRecords();


        for(int i = 0;i<records.size() ; i++){
            String []  temp = records.get(i).split("::");
            LRTTable[i][0] = temp [0];
            String []  temp2 = temp[1].split(" ");
            LRTTable[i][1] = Integer.toString(temp2.length - 1);
        }
        System.out.println("LRT Table : ");
        for (int i = 0; i < 21 ; i++)
        {
            for (int j = 0; j < 2 ; j++)
            {
                System.out.print(LRTTable[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

    }

    public void printLRTTable(){

        String str  = "";
        System.out.println();
        for(int i=0 ; i<=20 ; i++){
            for(int j = 0 ;j<=1 ; j++){
                str += LRTTable[i][j];
                int s = 12 - LRTTable[i][j].length();
                while (s > 0)
                {
                    str += " ";
                    s--;
                }
            }
            System.out.println(str);
            str = "";
        }

    }

//    public void GetBuParseTableFromExel() throws IOException {
//        Workbook workbook =  WorkbookFactory.create(new java.io.File("BUParseTable.xlsx"));
//        Sheet sheet =  workbook.getSheetAt(0);
//
//        int rows = 0;
//        int columns = 0;
//
//        Iterator rowIterator = sheet.iterator();
//        while (rowIterator.hasNext()) {
//            Row row = (Row) rowIterator.next();
//            Iterator cellIterator = row.cellIterator();
//
//            while (cellIterator.hasNext()) {
//                Cell cell = (Cell) cellIterator.next();
//
//                BUParseTable[rows][columns] = cell.getStringCellValue();
//
//                columns++;
//
//            }
//
//            rows++;
//        }
//
//    }
}