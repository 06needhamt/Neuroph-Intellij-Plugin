/*
The MIT License (MIT)

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
package com.thomas.needham.neurophidea.datastructures

import java.io.Serializable

/**
 * Created by thoma on 25/05/2016.
 */

class LearningRules : Serializable {
    enum class Rules(nameString : String) {
        //Enum must be nested within a class to avoid a NoClassDefFoundException possible Kotlin bug???
        BACK_PROPAGATION("Back Propagation"),
        DYNAMIC_BACK_PROPAGATION("Dynamic Back Propagation"),
        ANTI_HEBBAN_LEARNING("Anti Hebbian Learning"),
        BINARY_HEBBIAN_LEARNING("Binary Hebbian Learning"),
        COMPETITIVE_LEARNING("Competitive Learning"),
        GENERALIZED_HEBBIAN_LEARNING("Generalized Hebbian Learning"),
        HOPFIELD_LEARNING("Hopfield Learning"),
        INSTAR_LEARNING("Instar Learning"),
        KOHONEN_LEARNING("Kohonen Learning"),
        LMS("LMS"),
        MOMENTUM_BACK_PROPAGATION("Momentum Back Propagation"),
        OJA_LEARNING("Oja Learning"),
        OUTSTAR_LEARNING("Outstar Learning"),
        PERCEPTRON_LEARNING("Perceptron Learning"),
        RESILIENT_PROPAGATION("Resilient Propagation"),
        SIGMOID_DELTA_RULE("Sigmoid Delta Rule"),
        SIMULATED_ANNEALING_LEARNING("Simulated Annealing Learning"),
        SUPERVISED_HEBBIAN_LEARNING("Supervised Hebbian Learning"),
        UNSUPERVISED_HEBBIAN_LEARNING("Unsupervised Hebbian Learning");
    }

    companion object Names {
        @JvmStatic val classNames = arrayOf("BackPropagation", "DynamicBackPropagation", "AntiHebbianLearning",
                "BinaryHebbianLearning", "CompetitiveLearning", "GeneralizedHebbianLearning", "HopfieldLearning",
                "InstarLearning", "KohonenLearning", "LMS", "MomentumBackPropagation", "OjaLearning", "OutstarLearning",
                "PerceptronLearning", "ResilientPropagation", "SigmoidDeltaRule", "SimulatedAnnealingLearning",
                "SupervisedHebbianLearning", "UnsupervisedHebbianLearning")

        @JvmStatic val friendlyNames = arrayOf("Back Propagation", "Dynamic Back Propagation", "Anti Hebbian Learning",
                "Binary Hebbian Learning", "Competitive Learning", "Generalized Hebbian Learning", "Hopfield Learning",
                "Instar Learning", "Kohonen Learning", "LMS", "Momentum Back Propagation", "Oja Learning", "Outstar Learning",
                "Perceptron Learning", "Resilient Propagation", "Sigmoid Delta Rule", "Simulated Annealing Learning",
                "Supervised Hebbian Learning", "Unsupervised Hebbian Learning")

        @JvmStatic fun GetClassName(v : LearningRules.Rules) : String {
            return classNames[v.ordinal]
        }
    }
}