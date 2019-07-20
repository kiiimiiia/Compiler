package com.company;

import javax.management.Descriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CodeGenerator {

    public  List <Code> codes  ;
    //
    private Parser parser ;
    private Code code ;
    String Opcode;
    String Opr1;
    String Opr2 ;
    String result;
    int PC = 0;
    String ReturnType;
    int BaseTempAddress = 1000 ;
    private List <SimbolTable> ST;
    private Stack<String> semanticStack = new Stack<String>();
    int CodeLine;
    boolean terminate = false;
    int ADRS = 0;
    boolean isPointer = false ;

    CodeGenerator(){

        codes = new ArrayList<Code>();
      //  parser = new Parser();
        ST =   new ArrayList<SimbolTable>();


    }

    public boolean run(String SemanticRule , String PopPS  , int  line ){

        CodeLine = line ;
        if(SemanticRule.equals("@push"))
           push(PopPS);
        if(SemanticRule.equals("@add"))
            Add();
        if (SemanticRule.equals("@sub"))
           Sub();
        if(SemanticRule.equals("@mult"))
           Mult();
        if (SemanticRule.equals("@div"))
           Div();
        if (SemanticRule.equals("@mode"))
            Mod();
        if(SemanticRule.equals("@push_pc"))
            PushPc();
        if(SemanticRule.equals("@CDesc"))
            CDesc( PopPS );
        if(SemanticRule.equals("@pop"))
            Pop();
        if(SemanticRule.equals("@AssignDesc"))
            AssignDesc(PopPS);
        if(SemanticRule.equals("@isPointer "))
            isPointer();

        return terminate;
    }

    public void push(String PopPS){
        semanticStack.push(PopPS);
       // System.out.println(PopPS + " pushed in semantic stack");

    }
    public void PushPc(){
        semanticStack.push(Integer.toString(PC));
    }
    public void Pop(){
        semanticStack.pop();
    }


    public void isPointer(){
        isPointer = true ;
    }

    public void AssignDesc(String  PopPs){

        if(CheckTypeAssign(PopPs))
        {
            Opr1 =   semanticStack.pop();
            Opr2  =   PopPs;
            Code code = new Code("MOV");
            code.setOpr1(SearchInSymbolTableReturnAddress(1));
            code.setOpr2(Opr2);
            PC++;

            System.out.println("MOV   " + Opr1 + "  " + Opr2) ;

        }


    }

    public void CDesc(String PopPS ){

        if(! SearchInST(semanticStack.peek())){
            String name = semanticStack.pop();
            descriptor desc;
            if(isPointer) {
                desc = new descriptor("pointer", semanticStack.peek(), Integer.toString(ADRS), "-");
                isPointer = false;
            }
            else
                desc = new descriptor("simple",semanticStack.peek() , Integer.toString(ADRS) ,"-");
            ADRS += getSize(semanticStack.peek());
            SimbolTable st = new SimbolTable(desc , name);
            ST.add(st);
            System.out.println(desc.getDescriptorType() + " " + desc.getType() + " " + desc.getAddress() + " " + desc.getSizeOrAdrMode()) ;
        }
        else{
            System.out.println("error: Duplicate");
        }

    }

    public  void Mod(){
        Opcode = "MOD";
        Opr1 =   semanticStack.pop();
        Opr2  =   semanticStack.pop();
        ReturnType = CheckType(SearchInSymbolTableReturnType(1) , SearchInSymbolTableReturnType(2));
        if(!ReturnType.equals("!")) {
            //result address
            result = getTemp();
            code = new Code(Opcode);
            code.setOpr1(SearchInSymbolTableReturnAddress(1));
            code.setOpr2(SearchInSymbolTableReturnAddress(2));
            code.setResult(result);
            PC++;
            semanticStack.push(result);
            System.out.println("MOD   " + Opr1 + "  " + Opr2 + " " + result) ;
        }
        else {
            System.out.println("Synthax error occured in line " + CodeLine);
            terminate = true ;
        }

    }

    public void Add(){
        ////semantic stack koja update mishe

        Opcode = "ADD";
        Opr1 =   semanticStack.pop();
        Opr2  =   semanticStack.pop();
        ReturnType = CheckType(SearchInSymbolTableReturnType(1) , SearchInSymbolTableReturnType(2));
        if(!ReturnType.equals("!")) {
            //result address
            result = getTemp();
            code = new Code(Opcode);
            code.setOpr1(SearchInSymbolTableReturnAddress(1));
            code.setOpr2(SearchInSymbolTableReturnAddress(2));
            code.setResult(result);
            PC++;
            semanticStack.push(result);
            System.out.println("ADD   " + Opr1 + "  " + Opr2 + " " + result) ;
        }
        else {
            System.out.println("Synthax error occured in line " + CodeLine);
            terminate = true ;
        }

    }

    public void Sub()
    {
        Opcode = "SUB";
        Opr1 =   semanticStack.pop();
        Opr2  =   semanticStack.pop();
        ReturnType = CheckType(SearchInSymbolTableReturnType(1) , SearchInSymbolTableReturnType(2));
        if(!ReturnType.equals("!")) {
            //result address
            result = getTemp();
            code = new Code(Opcode);
            code.setOpr1(SearchInSymbolTableReturnAddress(1));
            code.setOpr2(SearchInSymbolTableReturnAddress(2));
            code.setResult(result);
            PC++;
            semanticStack.push(result);
            System.out.println("SUB   " + Opr1 + "  " + Opr2 + " " + result) ;
        }
        else
        {
            System.out.println("Synthax error occured in line " + CodeLine);
            terminate = true ;
        }

    }

    public void Div(){
        ////semantic stack koja update mishe

        Opcode = "DIV";
        Opr1 =   semanticStack.pop();
        Opr2  =   semanticStack.pop();
        ReturnType = CheckType(SearchInSymbolTableReturnType(1) , SearchInSymbolTableReturnType(2));
        if(!ReturnType.equals("!")) {
            //result address
            result = getTemp();
            code = new Code(Opcode);
            code.setOpr1(SearchInSymbolTableReturnAddress(1));
            code.setOpr2(SearchInSymbolTableReturnAddress(2));
            code.setResult(result);
            PC++;
            semanticStack.push(result);
            System.out.println("DIV   " + Opr1 + "  " + Opr2 + " " + result) ;
        }
        else {
            System.out.println("Synthax error occured in line " + CodeLine);
            terminate = true ;
        }


    }

    public void Mult(){

        Opcode = "MUL";
        Opr1 =   semanticStack.pop();
        Opr2  =   semanticStack.pop();
        ReturnType = CheckType(SearchInSymbolTableReturnType(1) , SearchInSymbolTableReturnType(2) );
        if(!ReturnType.equals("!")) {
            //result address
            result = getTemp();
            code = new Code(Opcode);
            code.setOpr1(SearchInSymbolTableReturnAddress(1));
            code.setOpr2(SearchInSymbolTableReturnAddress(2));
            code.setResult(result);
            PC++;
            semanticStack.push(result);
            System.out.println("MULT   " + Opr1 + "  " + Opr2 + " " + result) ;
        }
        else {
            System.out.println("Synthax error occured in line " + CodeLine);
            terminate = true ;
        }


    }


    public void WriteToFile() throws IOException {


        String temp = "";
        String str ;
        int s;
        FileWriter writer = new FileWriter("Code.txt");
        for(int i =0; i < codes.size(); i++) {

            temp = codes.get(i).getOpcode() ;
            s = 12  - codes.get(i).getOpcode().length() ;
            str  = "";
            while (s > 0)
            {
                str += " ";
                s--;
            }
            temp += str ;

            temp += codes.get(i).getOpr1();
            s = 12  - codes.get(i).getOpr1().length() ;
            str  = "";
            while (s > 0)
            {
                str += " ";
                s--;
            }
            temp += str ;

            temp += codes.get(i).getOpr2();
            s = 12  - codes.get(i).getOpr2().length() ;
            str  = "";
            while (s > 0)
            {
                str += " ";
                s--;
            }
            temp += str ;

            temp += codes.get(i).getResult();


            writer.write(temp);
            temp = "";
        }

        writer.close();

    }


    public String  CheckType( String Type1 , String Type2){
        String Res = "!" ;

        if(Type1.equals(Type2)) {
            if (Type1.equals("int"))
                Res = "int";

            else if(Type1.equals("float"))
                Res = "float";

        }
        else{
            if (Type1.equals("int")){
                if(Type2.equals("float"))
                    Res = "float";
            }

            if(Type1.equals("float"))
            {
                if(Type2.equals("int"))
                    Res = "float";
            }
        }


        return Res ;
    }


    public String getTemp(){
        int additional = 0;
        if(ReturnType.equals("int"))
            additional = 4;
        if(ReturnType.equals("float"))
            additional = 8;
        if(ReturnType.equals("char"))
            additional = 1;
        if(ReturnType.equals("bool"))
            additional = 1;

        BaseTempAddress += additional;
        String result = Integer.toString(BaseTempAddress);
        if (ReturnType.equals("error"))
            result = "!";

        return  result;
    }


    public String SearchInSymbolTableReturnType(int which){

        String Type = "";
        String oprand = "";
        if(which == 1)
            oprand = Opr1;
        if(which == 2)
            oprand = Opr2 ;

        for(int i = 0 ; i<ST.size() ; i++){
            if(ST.get(i).getName().equals(oprand)) {
                Type = ST.get(i).getDesc().getType();
                break ;
            }
        }

        return Type;
    }

    public String SearchInSymbolTableReturnAddress(int Which){
        String Address = "";
        String oprand = "";
        if(Which == 1)
            oprand = Opr1;
        if(Which == 2)
            oprand = Opr2 ;
        for(int i = 0 ; i<ST.size() ; i++){
            if(ST.get(i).getName().equals(oprand)) {
                Address = ST.get(i).getDesc().getAddress();
                break ;
            }
        }

        return Address ;

    }

    public int getSize (String test){
        if(test.equals("int"))
            return 4;
        if(test.equals("float"))
            return 8;
        if(test.equals("char"))
            return 1;
        if(test.equals("bool"))
            return 1;

        return 0;

    }

    public boolean SearchInST(String tokenVAl){
        for(int i = 0 ; i<ST.size() ; i++){
            if(ST.get(i).getName().equals(tokenVAl)) {
               return true ;
            }
        }
        return false ;
    }


    public boolean CheckTypeAssign(String PopPs){
        boolean result = true ;
        String type = semanticStack.peek();
        String check =  "i"; //default = int;
        for(int i = 0; i<PopPs.length() ; i++){
            if(PopPs.length() == 1){
                check = "c";
                break;
            }

            if(PopPs.charAt(i) == 'f') {
                check = "b";
                break;
            }
            if(PopPs.charAt(i) == 't'){
                check = "b";
                break;
            }
            if(PopPs.charAt(i) == '.')
            {
                check = "f" ;
                break;
            }


        }

        if(! String.valueOf(type.charAt(0)).equals(check))
            result = false ;


        if(type.equals("int") && check.equals("b"))
            System.out.println("a boolena cannot assign to an integer");
        if(type.equals("int") && check.equals("f"))
            System.out.println("a float cannot assign to an integer");
        if(type.equals("int") && check.equals("c"))
            System.out.println("a charachter cannot assign to an integer");
        if(type.equals("float") && check.equals("b"))
            System.out.println("a boolean cannot assign to a float");
        if(type.equals("float") && check.equals("c"))
            System.out.println("a character cannot assign to a float");
        if(type.equals("bool") && check.equals("f"))
            System.out.println("a float cannot assign to a boolean");
        if(type.equals("bool") && check.equals("i"))
            System.out.println("an integer cannot assign to a boolean");
        if(type.equals("bool") && check.equals("i"))
            System.out.println("an integer cannot assign to a boolean");
        if(type.equals("bool") && check.equals("c"))
            System.out.println("an character cannot assign to a boolean");
        if(type.equals("char") && check.equals("i"))
            System.out.println("an integer cannot assign to a character");
        if(type.equals("char") && check.equals("f"))
            System.out.println("an float cannot assign to a character");
        if(type.equals("char") && check.equals("b"))
            System.out.println("an boolean cannot assign to a character");


        return result ;
    }






}