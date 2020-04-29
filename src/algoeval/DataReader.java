/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algoeval;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author Nicolas
 */
public class DataReader {

    private String path = null;
    private File file = null;

    public DataReader(String path)
    {
        this.file = new File(path);
    } 
    

    public DataReader(File file)
    {
        this.file = file;
    }

    public ArrayList<HashMap> ReadData()
    {
        if(this.file.getName().endsWith(".csv"))
        {
            return ReadFromCSV();
            
        }
        else if(this.file.getName().endsWith(".xml"))
        {
            return readFromXML();
        }

        return null; //ToDo
    }

    private ArrayList<HashMap> ReadFromCSV()
    {
        ArrayList<HashMap> result = new ArrayList();
        File csvFile = this.file;

        String line = null;
        String delim = ";";
        BufferedReader bReader = null;

        System.out.println("================== ");
        System.out.println("Start reading CSV-file");
        System.out.println("================== ");

        try {
            bReader = new BufferedReader(new FileReader(csvFile));

            boolean firstLine = true;
            ArrayList<String> columns = new ArrayList<String>();

            while ((line = bReader.readLine()) != null) //read line by line
            {
                line = line.replace(";;", "; ;");
                StringTokenizer strTokenizer = new StringTokenizer(line, ";");
                HashMap<String, String> hMap = new HashMap<String, String>();
                int index = 0;
		String currentToken = null;

                while (strTokenizer.hasMoreTokens()) {

		    currentToken = strTokenizer.nextToken();

                    if (firstLine) //store the column names
                    {
                        columns.add(currentToken);
                   //     System.out.println("ColumnName: " + columns.get(index) + ", Index: " + index);
                                    
                    } else //store the corresponding values
                    {
                        hMap.put(columns.get(index), currentToken);
                    }
                    index++;

                }
                
                if (!hMap.isEmpty()) {
                    result.add(hMap);
                   // System.out.println("hMap: " + hMap.toString());
                }
               
                firstLine = false; //after first iteration the column names are written to csvColumns and now the data can be read
                index = 0;
            }
            
        } catch (Exception exc) {
            exc.printStackTrace();
        }


        System.out.println("================== ");
        System.out.println("ArrayList contains " + result.size() + " HashMaps");
        System.out.println("================== ");

        return result;

    }
    
    /**
     * Die Methode dient dazu, um eine Array-Liste mit 
     * HashMap-Elementen,die wiederrum Keys und Values Paare von Typ 
     * String enthalten von einer CSV-Datei zurückzugewinnen.Ein 
     * HashMap-Element in der Array-Liste zum Beispiel sieht so aus:
     * stop=00:01:37, structure=, Workflow=PhantomV04.xml, Typ=state, 
     * start=00:00:00, Akteur=, action=, Koerperteil=, Kategorie=Phase, 
     * Id=107, Notizen=, Instrument=, Version=cor, Element=Diagnostic 
     * Laparoscopy, Wert=Diagnostic Laparoscopy aus.
     * 
     * @return die Array-Liste mit HashMap-Elementen
     */
    public static ArrayList<HashMap> ReadFromCSV(String pfad)
    {
        ArrayList<HashMap> result = new ArrayList();
        File csvFile = new File(pfad);

        String line = null;
        String delim = ";";
        BufferedReader bReader = null;
//        System.out.println("================== ");
//        System.out.println("Start reading CSV-file");
//        System.out.println("================== ");

        try {
            bReader = new BufferedReader(new FileReader(csvFile));

            boolean firstLine = true;
            ArrayList<String> columns = new ArrayList<String>();

            while ((line = bReader.readLine()) != null) //read line by line
            {
                line = line.replace(";;", "; ;");
                StringTokenizer strTokenizer = new StringTokenizer(line, ";");
                HashMap<String, String> hMap = new HashMap<String, String>();
                int index = 0;
		String currentToken = null;

                while (strTokenizer.hasMoreTokens()) {

		    currentToken = strTokenizer.nextToken();
                    //currentToken=currentToken.replace("\"", "");
                    //System.out.println(currentToken);

                    if (firstLine) //store the column names
                    {
                        columns.add(currentToken);
                   //     System.out.println("ColumnName: " + columns.get(index) + ", Index: " + index);
                                    
                    } else //store the corresponding values
                    {
                        //hMap.put(columns.get(index), currentToken);
                        hMap.put(columns.get(index).replace("\"", ""), currentToken.replace("\"", ""));
                    }
                    index++;

                }
                
                if (!hMap.isEmpty()) {
                    result.add(hMap);
                   // System.out.println("hMap: " + hMap.toString());
                }
               
                firstLine = false; //after first iteration the column names are written to csvColumns and now the data can be read
                index = 0;
            }
            
        } catch (Exception exc) {
            exc.printStackTrace();
        }


//        System.out.println("================== ");
//        System.out.println("ArrayList contains " + result.size() + " HashMaps");
//        System.out.println("================== ");

        return result;
    }
     
      private ArrayList<HashMap> readFromXML()
    {
        return null;

    }


}

