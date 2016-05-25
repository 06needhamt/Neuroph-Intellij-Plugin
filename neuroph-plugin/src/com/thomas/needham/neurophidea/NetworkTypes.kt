package com.thomas.needham.neurophidea

/**
 * Created by thoma on 25/05/2016.
 */
class NetworkTypes {
    enum class Types(nameString: String){
        PERCEPTRON("Perceptron"),
        MULTI_LAYER_PERCEPTRON("Multi Layer Perceptron");
        //TODO add more network types
    }
    companion object Names{
        @JvmStatic val classNames = arrayOf("Perceptron", "MultiLayerPerceptron")
        @JvmStatic val friendlyNames = arrayOf("Perceptron", "Multi Layer Perceptron")

        //TODO add more network types
    }
}