package com.thomas.needham.neurophidea.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.util.TransferFunctionType;

import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.core.transfer.Gaussian;


public class TestNetwork {
    static int inputSize = 8;
    static int outputSize = 1;
    static NeuralNetwork network;
    static TrainingSet<SupervisedTrainingElement> trainingSet;
    static TrainingSet<SupervisedTrainingElement> testingSet;
    static int[] layers = {8,8,1};

    static void loadNetwork() {
        network = NeuralNetwork.load("D:/GitHub/Neuroph-Intellij-Plugin/TestNetwork.nnet");
    }

    static void trainNetwork() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int layer : layers) {
            list.add(layer);
        }

        NeuralNetwork network = new MultiLayerPerceptron(list, TransferFunctionType.GAUSSIAN);
        trainingSet = new TrainingSet<SupervisedTrainingElement>(inputSize, outputSize);
        trainingSet = TrainingSet.createFromFile("D:/GitHub/NeuralNetworkTest/Classroom Occupation Data.csv", inputSize, outputSize, ",");
        BackPropagation learningRule = new BackPropagation();
        network.setLearningRule(learningRule);
        network.learn(trainingSet);
        network.save("D:/GitHub/Neuroph-Intellij-Plugin/TestNetwork.nnet");
    }

    static void testNetwork() {
        String input = "";
        BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Double> testValues = new ArrayList<Double>();
        double[] testValuesDouble;
        do {
            try {
                System.out.println("Enter test values or \"\": ");
                input = fromKeyboard.readLine();
                if(input.equals("")) {
                    break;
                }
                input = input.replace(" ", "");
                String[] stringVals = input.split(",");
                testValues.clear();
                for(String val : stringVals) {
                    testValues.add(Double.parseDouble(val));
                }
            } catch(IOException ioe) {
                ioe.printStackTrace(System.err);
            } catch(NumberFormatException nfe) {
                nfe.printStackTrace(System.err);
            }
            testValuesDouble = new double[testValues.size()];
            for(int t = 0; t < testValuesDouble.length; t++) {
                testValuesDouble[t] = testValues.get(t).doubleValue();
            }
            network.setInput(testValuesDouble);
            network.calculate();
        } while (!input.equals(""));
    }

    static void testNetworkAuto(String setPath) {
        double total = 0.0;
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<String> outputLine = new ArrayList<String>();
        for(int layer : layers) {
            list.add(layer);
        }

        testingSet = TrainingSet.createFromFile(setPath, inputSize, outputSize,",");
        int count = testingSet.elements().size();
        double averageDevience = 0;
        String resultString = "";
        try{
            File file = new File("Results " + setPath);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            for(int i = 0; i < testingSet.elements().size(); i ++){
                double expected;
                double calculated;
                network.setInput(testingSet.elementAt(i).getInput());
                network.calculate();
                calculated = network.getOutput()[0];
                expected = testingSet.elementAt(i).getIdealArray()[0];
                System.out.println("Calculated Output: " + calculated);
                System.out.println("Expected Output: " + expected);
                System.out.println("Devience: " + (calculated - expected));
                averageDevience += Math.abs(Math.abs(calculated) - Math.abs(expected));
                total += network.getOutput()[0];
                resultString = "";
                for(int cols = 0; cols < testingSet.elementAt(i).getInputArray().length; cols++) {
                    resultString += testingSet.elementAt(i).getInputArray()[cols] + ", ";
                }
                for(int t = 0; t < network.getOutput().length; t++) {
                    resultString += network.getOutput()[t] + ", ";
                }
                resultString = resultString.substring(0, resultString.length()-2);
                resultString += "";
                bw.write(resultString);
                bw.flush();
            }
            System.out.println();
            System.out.println("Average: " + total / count);
            System.out.println("Average Devience % : " + (averageDevience / count) * 100);
            bw.flush();
            bw.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

}

