/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoeval;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author roman
 */
public class PreparationCSVforRF {

//    static void makeTrainingCSV(ArrayList<String> files, int n) {
//
//        try {
//            FileWriter f = new FileWriter("data/training.csv");
//            for (String file : files) {
//                ArrayList<HashMap> data = DataReader.ReadFromCSV(file);
//                String str = getStringForCSV(data, n);
//                f.write(str);
//            }
//            
//                f.close();
//        } catch (IOException e) {
//            System.out.println("Exception: " + e.toString());
//        }
//    }
//    
//    static void makeTestingingCSV(String file, int n) {
//        try {
//            FileWriter f = new FileWriter("data/testing.csv");
//            ArrayList<HashMap> data = DataReader.ReadFromCSV(file);
//            String str = getStringForCSV(data, n);
//            f.write(str);
//            f.close();
//        } catch (IOException e) {
//            System.out.println("Exception: " + e.toString());
//        }
//
//    }
    public static void makeTrainingCSV_MoreFiles(ArrayList<String> files, String eval, int n) {

        try {
            File surgery = new File(eval);
            FileWriter f = new FileWriter("data_training\\training_" + surgery.getName().substring(0, surgery.getName().indexOf(".csv")) + "_" + n + ".csv");
            for (String file : files) {
                ArrayList<HashMap> data = DataReader.ReadFromCSV(file);
                String str = getStringForCSV(data, n);
                f.write(str);
            }

            f.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e.toString());
        }
    }

    public static void makeTestingingCSV_MoreFiles(String file, int n) {
        try {
            File surgery = new File(file);
            //FileWriter f = new FileWriter("data_testing/testing_"+surgery.getName().substring(0, surgery.getName().indexOf(".csv"))+"_"+n+".csv");
            FileWriter f = new FileWriter("data_testing\\testing_" + surgery.getName().substring(0, surgery.getName().indexOf(".csv")) + "_" + n + ".csv");
            ArrayList<HashMap> data = DataReader.ReadFromCSV(file);
            String str = getStringForCSV(data, n);
            f.write(str);
            f.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e.toString());
        }

    }

    private static long getTimeInMilisek(String time) {

        String[] stra = time.split(":");
        int[] inta = new int[stra.length];
        for (int i = 0; i < inta.length; i++) {
            inta[i] = Integer.parseInt(stra[i]);
        }

        long milisek = (inta[0] * 360 + inta[1] * 60 + inta[2]) * 1000;
        return milisek;
    }

    static int getStepID(String stepName) {
        int id = 0;

        if (stepName == null) {
            return id;
        }
        switch (stepName.toLowerCase()) {
            case "check for metastasis and adhesions":
                id = 1;
                break;
            case "mobilization sigmoid":
                id = 2;
                break;
            case "mobilization descending colon":
                id = 3;
                break;
            case "mobilization splenic flexure":
                id = 4;
                break;
            case "inspection colon":
                id = 5;
                break;
            case "lancing retroperitoneum":
                id = 6;
                break;
            case "delineating of vessels":
                id = 7;
                break;
            case "division of artery":
                id = 8;
                break;
            case "division of vein":
                id = 9;
                break;
            case "opening of lesser pelvic peritoneum":
                id = 10;
                break;
            case "dissection rectum":
                id = 11;
                break;
            case "transect rectum":
                id = 12;
                break;
            case "salvage rectum":
                id = 13;
                break;
            case "visual inspectation lesser pelvis":
                id = 14;
                break;
            default:
                id = 0;
                break;
        }

        return id;
    }

    static String getStepName(int id) {
        String stepName = "";

        if (id == 0) {
            return stepName;
        }
        switch (id) {
            case 1:
                stepName = "check for metastasis and adhesions";
                break;
            case 2:
                stepName = "mobilization sigmoid";
                break;
            case 3:
                stepName = "mobilization descending colon";
                break;
            case 4:
                stepName = "mobilization splenic flexure";
                break;
            case 5:
                stepName = "inspection colon";
                break;
            case 6:
                stepName = "lancing retroperitoneum";
                break;
            case 7:
                stepName = "delineating of vessels";
                break;
            case 8:
                stepName = "division of artery";
                break;
            case 9:
                stepName = "division of vein";
                break;
            case 10:
                stepName = "opening of lesser pelvic peritoneum";
                break;
            case 11:
                stepName = "dissection rectum";
                break;
            case 12:
                stepName = "transect rectum";
                break;
            case 13:
                stepName = "salvage rectum";
                break;
            case 14:
                stepName = "visual inspectation lesser pelvis";
                id = 14;
                break;
            default:
                stepName = "";
                break;
        }

        return stepName;
    }

    private static String getStringForCSV(ArrayList<HashMap> hmData, int n) {
        String data = "";
        ArrayList<String> nextData = new ArrayList<String>();
        int z = n;
        for (HashMap current : hmData) {
//            String value = "";
//            long sTime = 0;
//            long eTime = 0;
//            long duration = 0;
            if (current.get("Kategorie").equals("Step")) {
                String value = current.get("Wert").toString();
                int id = getStepID(value);
                long sTime = getTimeInMilisek(current.get("start").toString());
                long eTime = getTimeInMilisek(current.get("stop").toString());
                long duration = eTime - sTime;
                //nextData.add(value + ";" + duration + ";");
                nextData.add(id + ";" + duration + ";");
                data = data + previousStringForCSV(z, nextData);
                z--;
            }
        }
        return data;
    }

    private static String previousStringForCSV(int n, ArrayList<String> nextData) {
        String data = "";
        for (int z = 0; z < n; z++) {
            data = data + "0;0;";
        }
        for (String str : nextData) {
            data = data + str;
        }
        if (n <= 0) {
            nextData.remove(0);
        }
        return data.replaceAll(data, data.substring(0, data.length() - 1)) + "\n";
    }

    public static ArrayList<String[]> ReadFromTrainingCSV(String path) {
        ArrayList<String[]> trainingSet = new ArrayList<String[]>();
        String[] alr = null;
        String csvFile = path;
        BufferedReader br = null;
        String row = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            int counter = 0;
            while ((row = br.readLine()) != null) {
//                System.out.print(counter + ": ");
                counter++;
                // use comma as separator
                alr = row.split(cvsSplitBy);
                for (int j = 0; j < alr.length; j++) {
                    if (j < alr.length - 1) {
//                        System.out.print( alr[j] + " ");
                    } else {
//                        System.out.print( alr[j] + " ");
//                        System.out.println("");
                    }
//                    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//                    String s1 = bufferRead.readLine();
                }

//                System.out.println("__________________");
                //System.out.println(alr[11].toString());
//                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//                String s = bufferRead.readLine();
//
//                System.out.println("Etwas eingegeben: "+s);
                trainingSet.add(alr);
                //trainingSet.put(row)
//			System.out.println("Country [code= " + country[4] 
//                                 + " , name=" + country[5] + "]");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return trainingSet;
    }

}
