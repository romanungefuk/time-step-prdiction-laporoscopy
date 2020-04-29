/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
//import algoeval.*;

import algoeval.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.ml.*;

/**
 *
 * @author Administrator
 */
import org.opencv.core.TermCriteria;

public class Main {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        ArrayList<String> arrAnotation = new ArrayList<String>();
        ArrayList<String> arrTrainingsFiles = new ArrayList<String>();
        ArrayList<String> arrTestingFiles = new ArrayList<String>();
        arrAnotation.add("data" + File.separator + "PhantomV05.csv");
        arrAnotation.add("data" + File.separator + "PhantomV06.csv");
        arrAnotation.add("data" + File.separator + "PhantomV07.csv");
        arrAnotation.add("data" + File.separator + "PhantomV08.csv");
        arrAnotation.add("data" + File.separator + "PhantomV09.csv");
        arrAnotation.add("data" + File.separator + "PhantomV10.csv");
        arrAnotation.add("data" + File.separator + "PhantomV11.csv");
        arrAnotation.add("data" + File.separator + "PhantomV12.csv");
        arrAnotation.add("data" + File.separator + "PhantomV13final.csv");
        arrAnotation.add("data" + File.separator + "PhantomV14.csv");
        arrAnotation.add("data" + File.separator + "PhantomV15.csv");
        arrAnotation.add("data" + File.separator + "PhantomV16.csv");
        arrAnotation.add("data" + File.separator + "PhantomV17.csv");
        for (int i = 0; i < arrAnotation.size(); i++) {
            for (int h = 0; h < 14; h++) {
                PreparationCSVforRF.makeTrainingCSV_MoreFiles(exclude(arrAnotation, i), arrAnotation.get(i), h);
                PreparationCSVforRF.makeTestingingCSV_MoreFiles(arrAnotation.get(i), h);
            }
        }
        for (int j = 0; j < 14; j++) {
            for (int i = 0; i < arrAnotation.size(); i++) {
                File surgery = new File(arrAnotation.get(i));
                String pathTraining = "data_training" + File.separator + "training_" + surgery.getName().substring(0, surgery.getName().indexOf(".csv")) + "_" + j + ".csv";
                String pathTesting = "data_testing" + File.separator + "testing_" + surgery.getName().substring(0, surgery.getName().indexOf(".csv")) + "_" + j + ".csv";
                arrTrainingsFiles.add(pathTraining);
                arrTestingFiles.add(pathTesting);
            }
            
        }

        double allFaultOneHistory=0.0;
        int counter=0;
        for (int i = 0; i < arrTrainingsFiles.size(); i++) {
            StepPrediction sp = new StepPrediction();
            allFaultOneHistory=allFaultOneHistory+sp.setup(arrTrainingsFiles.get(i), arrTestingFiles.get(i));
            ++counter;
            if(counter==13){
                System.out.println("Durchschnitt der Gesamtfehlerqoute von einer Historie beträgt ca. "+Math.round(allFaultOneHistory/13.0)+"%.");
                allFaultOneHistory=0.0;
                counter=0;
            }
            System.out.println("_______________________________________");
        }

    }

    private static ArrayList<String> exclude(ArrayList<String> samples, int sampleForEval) {
        ArrayList<String> rSamples = (ArrayList<String>) samples.clone();
        rSamples.remove(sampleForEval);
        return rSamples;
    }
}
