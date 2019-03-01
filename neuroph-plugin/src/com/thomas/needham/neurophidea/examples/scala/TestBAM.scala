import java.io._
import java.util

import org.neuroph.core.NeuralNetwork
import org.neuroph.core.learning.{SupervisedTrainingElement, TrainingSet}
import org.neuroph.nnet.BAM
import org.neuroph.nnet.learning.BackPropagation


object TestBAM {
  var inputSize: Int = 8
  var outputSize: Int = 1
  var network: NeuralNetwork = _
  var trainingSet: TrainingSet[SupervisedTrainingElement] = _
  var testingSet: TrainingSet[SupervisedTrainingElement] = _
  var layers: Array[Int] = Array(8, 8, 1)

  def loadNetwork() {
    network = NeuralNetwork.load("D:/GitHub/Neuroph-Intellij-Plugin/TestBAM.nnet")
  }

  def trainNetwork() {
    val list = new util.ArrayList[Integer]()
    for (layer <- layers) {
      list.add(layer)
    }
    network = new BAM(inputSize, outputSize);
    trainingSet = new TrainingSet[SupervisedTrainingElement](inputSize, outputSize)
    trainingSet = TrainingSet.createFromFile("D:/GitHub/NeuralNetworkTest/Classroom Occupation Data.csv", inputSize, outputSize, ",").asInstanceOf[TrainingSet[SupervisedTrainingElement]]
    val learningRule = new BackPropagation();
    network.setLearningRule(learningRule)
    network.learn(trainingSet)
    network.save("D:/GitHub/Neuroph-Intellij-Plugin/TestBAM.nnet")
  }

  def testNetwork() {
    var input = ""
    val fromKeyboard = new BufferedReader(new InputStreamReader(System.in))
    val testValues = new util.ArrayList[Double]()
    var testValuesDouble: Array[Double] = null
    do {
      try {
        println("Enter test values or \"\": ")
        input = fromKeyboard.readLine()
        if (input == "") {
          //break
        }
        input = input.replace(" ", "")
        val stringVals = input.split(",")
        testValues.clear()
        for (value <- stringVals) {
          testValues.add(value.toDouble)
        }
      } catch {
        case ioe: IOException => ioe.printStackTrace(System.err)
        case nfe: NumberFormatException => nfe.printStackTrace(System.err)
      }
      testValuesDouble = Array.ofDim[Double](testValues.size)
      for (t <- testValuesDouble.indices) {
        testValuesDouble(t) = testValues.get(t).doubleValue()
      }
      network.setInput(testValuesDouble: _*)
      network.calculate()
    } while (input != "")
  }

  def testNetworkAuto(setPath: String) {
    var total: Double = 0.0
    val list = new util.ArrayList[Integer]()
    val outputLine = new util.ArrayList[String]()
    for (layer <- layers) {
      list.add(layer)
    }
    testingSet = TrainingSet.createFromFile(setPath, inputSize, outputSize, ",").asInstanceOf[TrainingSet[SupervisedTrainingElement]]
    val count = testingSet.elements().size
    var averageDeviance = 0.0
    var resultString = ""
    try {
      val file = new File("Results " + setPath)
      val fw = new FileWriter(file)
      val bw = new BufferedWriter(fw)
      for (i <- 0 until testingSet.elements().size) {
        var expected: Double = 0.0
        var calculated: Double = 0.0
        network.setInput(testingSet.elementAt(i).getInput: _*)
        network.calculate()
        calculated = network.getOutput()(0)
        expected = testingSet.elementAt(i).getIdealArray()(0)
        println("Calculated Output: " + calculated)
        println("Expected Output: " + expected)
        println("Deviance: " + (calculated - expected))
        averageDeviance += math.abs(math.abs(calculated) - math.abs(expected))
        total += network.getOutput()(0)
        resultString = ""
        for (cols <- testingSet.elementAt(i).getInputArray.indices) {
          resultString += testingSet.elementAt(i).getInputArray()(cols) + ", "
        }
        for (t <- network.getOutput.indices) {
          resultString += network.getOutput()(t) + ", "
        }
        resultString = resultString.substring(0, resultString.length - 2)
        resultString += ""
        bw.write(resultString)
        bw.flush()
      }
      println()
      println("Average: " + total / count)
      println("Average Deviance % : " + (averageDeviance / count) * 100)
      bw.flush()
      bw.close()
    } catch {
      case ex: IOException => ex.printStackTrace()
    }
  }

}

