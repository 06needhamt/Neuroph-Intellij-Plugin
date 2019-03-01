/* The MIT License (MIT)

Copyright (c) 2016 Tom Needham

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.thomas.needham.neurophidea.examples.groovy

import org.neuroph.core.NeuralNetwork
import org.neuroph.core.learning.SupervisedTrainingElement
import org.neuroph.core.learning.TrainingSet
import org.neuroph.nnet.SupervisedHebbianNetwork
import org.neuroph.nnet.learning.BackPropagation
import org.neuroph.util.TransferFunctionType

public class TestSHN {
    static int inputSize = 8;
    static int outputSize = 1;
    static NeuralNetwork network;
    static TrainingSet<SupervisedTrainingElement> trainingSet;
    static TrainingSet<SupervisedTrainingElement> testingSet;
    static int[] layers = [8, 8, 1];

    static void loadNetwork() {
        network = NeuralNetwork.load("D:/GitHub/Neuroph-Intellij-Plugin/TestSHN.nnet");
    }

    static void trainNetwork() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int layer : layers) {
            list.add(layer);
        }

        network = new SupervisedHebbianNetwork(inputSize, outputSize, TransferFunctionType.SIGMOID);
        trainingSet = new TrainingSet<SupervisedTrainingElement>(inputSize, outputSize);
        trainingSet = TrainingSet.createFromFile("D:/GitHub/NeuralNetworkTest/Classroom Occupation Data.csv", inputSize, outputSize, ",");
        BackPropagation learningRule = new BackPropagation();
        network.setLearningRule(learningRule);
        network.learn(trainingSet);
        network.save("D:/GitHub/Neuroph-Intellij-Plugin/TestSHN.nnet");
    }

    static void testNetwork() {
        String input = "";
        BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Double> testValues = new ArrayList<Double>();
        double[] testValuesDouble;
        Looper.loop {
            try {
                System.out.println("Enter test values or \"\": ");
                input = fromKeyboard.readLine();
                if (input == "") {
                    return;
                }
                input = input.replace(" ", "");
                String[] stringVals = input.split(",");
                testValues.clear();
                for (String val : stringVals) {
                    testValues.add(Double.parseDouble(val));
                }
            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace(System.err);
            }
            testValuesDouble = new double[testValues.size()];
            for (int t = 0; t < testValuesDouble.length; t++) {
                testValuesDouble[t] = testValues.get(t).doubleValue();
            }
            network.setInput(testValuesDouble);
            network.calculate();
        } until(input != "");
    }

    static void testNetworkAuto(String setPath) {
        double total = 0.0;
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<String> outputLine = new ArrayList<String>();
        for (int layer : layers) {
            list.add(layer);
        }

        testingSet = TrainingSet.createFromFile(setPath, inputSize, outputSize, ",");
        int count = testingSet.elements().size();
        double averageDeviance = 0;
        String resultString = "";
        try {
            File file = new File("Results " + setPath);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < testingSet.elements().size(); i++) {
                double expected;
                double calculated;
                network.setInput(testingSet.elementAt(i).getInput());
                network.calculate();
                calculated = network.getOutput()[0];
                expected = testingSet.elementAt(i).getIdealArray()[0];
                System.out.println("Calculated Output: " + calculated);
                System.out.println("Expected Output: " + expected);
                System.out.println("Deviance: " + (calculated - expected));
                averageDeviance += Math.abs(Math.abs(calculated) - Math.abs(expected));
                total += network.getOutput()[0];
                resultString = "";
                for (int cols = 0; cols < testingSet.elementAt(i).getInputArray().length; cols++) {
                    resultString += testingSet.elementAt(i).getInputArray()[cols] + ", ";
                }
                for (int t = 0; t < network.getOutput().length; t++) {
                    resultString += network.getOutput()[t] + ", ";
                }
                resultString = resultString.substring(0, resultString.length() - 2);
                resultString += "";
                bw.write(resultString);
                bw.flush();
            }
            System.out.println();
            System.out.println("Average: " + total / count);
            System.out.println("Average Deviance % : " + (averageDeviance / count) * 100);
            bw.flush();
            bw.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static class Looper {
        private Closure code

        static Looper loop(Closure code) {
            new Looper(code: code);
        }

        void until(Closure test) {
            code();
            while (!test()) {
                code();
            }
        }
    }

}

