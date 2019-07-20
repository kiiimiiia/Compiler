package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Files {

    /* as each line of the files is read it is assigned to a Java String,
    with each String in turn being added to an ArrayList named records.*/
    public  List<String> records = new ArrayList<String>();

    public void open(){

    }

    public void Reader(String filename){

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null)
            {
                records.add(line);
            }
            reader.close();
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
        }

    }

    public void writer(ArrayList Print) throws IOException {
        Main main = new Main();
        List<Token> token = main.LA.getTokens();
        StringBuilder temp = new StringBuilder();
        FileWriter writer = new FileWriter("Tokenized.txt");
        for(int i =0; i < token.size(); i++) {
            temp.append(TokenToString(token.get(i)));
            if (i < token.size() - 1)
                temp.append(" , ");

        }
        writer.write(temp.toString());
        writer.close();
    }

    public List<String> getRecords(){
        return records;
    }

    public void deleteRecords()
    {
        records.remove(0);
    }


    public String getRecordsByIndex(int i){
        return records.get(i);
    }


    public String TokenToString(Token token){
        String res;
        res = "<" + token.getValue() + "," + token.getType() + ">";
        return res;
    }

}