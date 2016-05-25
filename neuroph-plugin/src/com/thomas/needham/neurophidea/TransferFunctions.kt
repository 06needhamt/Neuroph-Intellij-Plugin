package com.thomas.needham.neurophidea

/**
 * Created by thoma on 25/05/2016.
 */
class TransferFunctions {
    enum class Functions(nameString: String) {
        SIGMOID("Sigmoid"),
        GAUSSIAN("Gaussian");
        //TODO Add More Transfer Functions
    }
    companion object Names{
        @JvmStatic val classNames = arrayOf("Sigmoid", "Gaussian")
        @JvmStatic val friendlyNames = arrayOf("Sigmoid", "Gaussian")
        //TODO Add More Transfer Functions
    }
}