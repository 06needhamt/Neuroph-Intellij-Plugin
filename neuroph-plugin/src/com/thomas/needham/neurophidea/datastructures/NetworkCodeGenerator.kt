package com.thomas.needham.neurophidea.datastructures

import com.intellij.ide.util.PropertiesComponent
import com.thomas.needham.neurophidea.Constants
import org.jetbrains.annotations.NotNull

/**
 * Created by thoma on 28/05/2016.
 */
class NetworkCodeGenerator {
    val network : NetworkConfiguration?
    val outputPath : String
    var sourceOutput : String = ""

    companion object Data{
        @JvmStatic var properties = PropertiesComponent.getInstance()
        @JvmStatic var version = properties.getValue(Constants.VERSION_KEY)
        @JvmStatic var path : String? = ""
        val coreImports = "import java.io.BufferedReader;" + "\n" +
                            "import java.io.BufferedWriter;" + "\n" +
                            "import java.io.File;" + "\n" +
                            "import java.io.FileWriter;" + "\n" +
                            "import java.io.IOException;" + "\n" +
                            "import java.io.InputStreamReader;" + "\n" +
                            "import java.util.ArrayList;" + "\n" +
                            "import java.util.logging.Level;" + "\n" +
                            "import java.util.logging.Logger;" + "\n" +
                            "import org.neuroph.core.NeuralNetwork;" + "\n" +
                            "import org.neuroph.core.learning.SupervisedTrainingElement;" + "\n" +
                            "import org.neuroph.core.learning.TrainingSet;" + "\n" +
                            "import org.neuroph.util.TransferFunctionType;" + "\n" + "\n";
    }

    constructor(@NotNull network: NetworkConfiguration?, outputPath: String){
        this.network = network
        this.outputPath = outputPath
        path = outputPath
    }

    fun GenerateCode() : String{
        sourceOutput += AddImports()
        sourceOutput += "\n" + "\n"
        return sourceOutput
    }

    private fun AddImports() : String {
        var imports = coreImports
        when (network?.networkType){
            NetworkTypes.Types.PERCEPTRON -> imports += "import org.neuroph.nnet.Perceptron;" + "\n"
            NetworkTypes.Types.MULTI_LAYER_PERCEPTRON -> imports += "org.neuroph.nnet.MultiLayerPerceptron" + "\n"
            else -> imports += "import UnknownType;" + "\n"
            //TODO Add More Network Types
        }
        when(network?.networkLearningRule){
            LearningRules.Rules.BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.BackPropagation;" + "\n"
            LearningRules.Rules.DYNAMIC_BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.DynamicBackPropagation;" + "\n"
            else -> imports += "import UnknownLearningRule;" + "\n"
        }
        when(network?.networkTransferFunction){
            TransferFunctions.Functions.GAUSSIAN -> imports += "import org.neuroph.core.transfer.Gaussian" + "\n"
            TransferFunctions.Functions.SIGMOID -> imports += "import org.neuroph.core.transfer.Sigmoid" + "\n"
            else -> imports += "import UnknownTransferFunction;" + "\n"
        }
        return imports
    }
}