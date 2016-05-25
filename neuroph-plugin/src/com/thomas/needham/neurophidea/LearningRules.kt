package com.thomas.needham.neurophidea

/**
 * Created by thoma on 25/05/2016.
 */

class LearningRules {
    enum class Rules(nameString: String) {
        //Enum must be nested within a class to avoid a NoClassDefFoundException possible Kotlin bug???
        BACK_PROPAGATION("Back Propagation"),
        DYNAMIC_BACK_PROPAGATION("Dynamic Back Propagation");
        //TODO Add more learning rules
    }
    companion object Names{
        @JvmStatic val classNames = arrayOf("BackPropagation", "DynamicBackPropagation")
        @JvmStatic val friendlyNames = arrayOf("Back Propagation", "Dynamic Back Propagation")
        //TODO Add more learning rules
    }
}