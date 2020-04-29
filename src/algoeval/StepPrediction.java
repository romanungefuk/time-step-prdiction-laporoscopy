/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoeval;

import java.io.File;
import java.util.ArrayList;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.TermCriteria;
import org.opencv.ml.CvRTParams;
import org.opencv.ml.CvRTrees;

/**
 *
 * @author Administrator
 */
public class StepPrediction {
    
    public double setup(String pathToTrainingsData,String pathToTestingData){
        System.out.println("Surgery "+pathToTestingData.substring(pathToTestingData.indexOf("Phantom"))+" als Test Surgery.");
        //ArrayList<String[]> trainingData = PreparationCSVforRF.ReadFromTrainingCSV("data_training"+File.separator+"training_PhantomV05_5.csv");
        ArrayList<String[]> trainingData = PreparationCSVforRF.ReadFromTrainingCSV(pathToTrainingsData);

         Mat trainingTraits = new Mat(
                 trainingData.size(), // we need as many rows as we have rows in the training table
                trainingData.get(0).length - 1, // we need one less column, as we're only interested in the traits
//                 test.size(), // we need one less column, as we're only interested in the traits
                CvType.CV_32FC1 // this tells opencv that we're saving numbers (floats) in the Mat
        );
         
         Mat trainingAnswers = new Mat(
                trainingData.size(), // we also need as many rows as we have rows in the training table here
                1, // we're only saving one number, the answer, so only use one column
                CvType.CV_32FC1 // this tells opencv that we're saving numbers (floats) in the Mat     
        );
         
         for (int row = 0; row < trainingData.size(); row++) {
            // and for each row, let's loop through each column
            for (int col = 0; col < trainingData.get(row).length; col++) {
                // if this is not the last column, it's a trait, so put it in the traits Mat object
                if (col < trainingData.get(row).length - 1) {
                    trainingTraits.put(row, col, Integer.parseInt(trainingData.get(row)[col]));
//                    System.out.print(Integer.parseInt(test.get(row)[col]));
                } // if it's the last column, it's an answer, so put it in the answers Mat object
                else {
                    trainingAnswers.put(row, 0, Integer.parseInt(trainingData.get(row)[col]));
//                    System.out.println(Integer.parseInt(test.get(row)[col]));
                }
            }
        }
       Mat varType = new Mat(trainingData.get(0).length, 1, CvType.CV_8U);
        varType.setTo(new Scalar(0)); // 0 = CV_VAR_NUMERICAL.  
//        varType.put(test.size() - 1, 0, 1); // 1 = CV_VAR_CATEGORICAL;
        for (int row = 0; row < trainingData.get(0).length; row++) {
            if (row % 2 == 0) {
                varType.put(row, 0, 1); // 1 = CV_VAR_CATEGORICAL;
            } else {
                varType.put(row, 0, 0); // 1 = CV_VAR_CATEGORICAL; 
            }
        }
         CvRTParams params = new CvRTParams();

        // The maximum depth of the tree. Setting this low will likely not create enough nodes to give correct answers. Setting this
        // too high is not a great thing either.
        //params.set_max_depth(25);
         params.set_max_depth(3);

        // random trees split nodes on randomly chosen data. This parameter tells the algorithm to at least have 5 samples before 
        // splitting the tree
        params.set_min_sample_count(5);

        // our algorithm is predicting classifications, not regression, so set this to 0. This is possibly not needed at all.
        //params.set_regression_accuracy(0);
        params.set_regression_accuracy(0.00000001f);

        // If you have missing data, the algorithm can create fake "surrogates" to maintain the idea of a full data set, and still
        // split leaf nodes on these values. We set this to false as our dataset it complete and not missing values.
        params.set_use_surrogates(false);

        // This is mostly speed optimization. Because the random forest algorithm is exponential, try to fit all samples into 
        // this many categories.
        //params.set_max_categories(15);

        // WE NEED PRIORS HERE?
        // ???????priors, // the array of priors ????
        //float[] priors = {1,1,1,1,1,1,1,1,1,1};  // weights of each classification for classes
        //// (all equal as equal samples of each digit) if we wanted to, we could weight some higher than other
        // disable calculation of a variable importance.
        params.set_calc_var_importance(false);

        // when a node in the tre is split, use this number of random data points. Set to 0 to automatically set it 
        // to the sqrt() of the number of traits.
//        params.set_nactive_vars(4);
        params.set_nactive_vars(0);

        // Tell the algorithm when to stop. We do this by passing a TermCriteria with specific settings to the param object
        params.set_term_crit(
                new TermCriteria(
                        // MAX_ITER tells the training algorithm to stop learning when reaching the maximum number of trees in the forest
                        // as specified below.
                        // EPS tells the algorithm to stop when reaching forest_accuray, as specific below.
                        // Plus them together, and it exits when reaching any of those two first.
                        TermCriteria.MAX_ITER + TermCriteria.EPS,
                        // The maximum number of trees in the forrest. Generally the higher the better, but this will also significantly 
                        // slow down the prediction time.
                        //100,
                        100,
                        // Forest acuracy.
                        //0.0f
                        0.01f
                )
        );

        // Now finally create our main random forest object that we're going to train
        CvRTrees forest = new CvRTrees();

        // Now call the train function, passing in all of the objects we created above. The bigger the dataset, the longer it 
        // takes to train. Tweaking the params like max_categories, max_depth, max_trees and forest_accuracy can also significantly cut down on time.
        //forest.train(trainingTraits, 1, trainingAnswers, new Mat(), new Mat(), varType, new Mat(), params); // 1 = CV_ROW_SAMPLE
//        System.out.println(trainingTraits.cols());
//        System.out.println(trainingTraits.rows());
//        System.out.println(trainingAnswers.cols());
//        System.out.println(trainingAnswers.rows());
        forest.train(trainingTraits, 1, trainingAnswers, new Mat(), new Mat(), varType, new Mat(), params); // 1 = CV_ROW_SAMPLE
        
//         ArrayList<String[]> testingData = PreparationCSVforRF.ReadFromTrainingCSV("data_testing"+File.separator+"testing_PhantomV05_5.csv");
         ArrayList<String[]> testingData = PreparationCSVforRF.ReadFromTrainingCSV(pathToTestingData);
          Mat testingTraits = new Mat(
                testingData.size(),
                testingData.get(0).length-1,
                CvType.CV_32FC1
        );
        
        Mat testingAnswers = new Mat(
                testingData.size(),
                1,
                CvType.CV_32FC1
        );
        
        for (int row = 0; row < testingData.size(); row++) {
            // and for each row, let's loop through each column
            for (int col = 0; col < testingData.get(row).length; col++) {
                // if this is not the last column, it's a trait, so put it in the traits Mat object
                if (col < testingData.get(row).length - 1) {
                    testingTraits.put(row, col, Integer.parseInt(testingData.get(row)[col]));
                } // if it's the last column, it's an answer, so put it in the answers Mat object
                else {
                    testingAnswers.put(row, 0, Integer.parseInt(testingData.get(row)[col]));
                }
            }
        }
        long allFault = 0L;
        long allSurgeryTime = 0L;
        for (int i = 0; i < testingData.size(); i++) {
            Mat testRow = testingTraits.row(i);
             double prediction = forest.predict(testRow);
             allFault=allFault+Math.abs(Math.round(prediction)-Integer.parseInt(testingData.get(i)[testingData.get(i).length-1]));
             allSurgeryTime=allSurgeryTime+(long)Integer.parseInt(testingData.get(i)[testingData.get(i).length-1]);
             System.out.println("Surgerystep "+PreparationCSVforRF.getStepName(Integer.parseInt(testingData.get(i)[testingData.get(i).length-2]))+" dauert "+testingData.get(i)[testingData.get(i).length-1]+" ms.");
             System.out.println("Prophet hat gesagt: "+ Math.round(prediction)+" ms.");
             System.out.println("Fehler beträgt: "+ Math.abs(Math.round(prediction)-Integer.parseInt(testingData.get(i)[testingData.get(i).length-1]))+" ms.");
        }
        System.out.println("Gesamtfehler beträgt: "+allFault+" ms.");
        System.out.println("Die Dauer der Test OP beträgt: "+allSurgeryTime+" ms.");
        System.out.println("Gesamtfehlerqoute von der Test Op "+pathToTestingData.substring(pathToTestingData.indexOf("Phantom"))+" beträgt: "+ Math.round((allFault * 1.0 / allSurgeryTime) * 100) + " %.");
        return Math.round((allFault * 1.0 / allSurgeryTime) * 100);
    }
     private ArrayList<String> exclude(ArrayList<String> samples, int sampleForEval) {
        ArrayList<String> rSamples = (ArrayList<String>) samples.clone();
        rSamples.remove(sampleForEval);
        return rSamples;
    }
}
