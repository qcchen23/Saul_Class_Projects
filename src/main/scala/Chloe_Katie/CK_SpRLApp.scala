/**
  * Created by Chloe Chen and Katie Roberts on 11/14/16.
  */

package Chloe_Katie

import java.io.File

import edu.illinois.cs.cogcomp.core.datastructures.ViewNames
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.{Constituent, TextAnnotation, TokenLabelView}
import edu.illinois.cs.cogcomp.core.utilities.configuration.{Configurator, Property, ResourceManager}
import edu.illinois.cs.cogcomp.nlp.common.PipelineConfigurator._
import edu.illinois.cs.cogcomp.nlp.tokenizer.StatefulTokenizer
import edu.illinois.cs.cogcomp.nlp.utility.TokenizerTextAnnotationBuilder
import edu.illinois.cs.cogcomp.saul.classifier.{ClassifierUtils, Learnable}
import edu.illinois.cs.cogcomp.saulexamples.nlp.POSTagger.POSAnnotator
import edu.illinois.cs.cogcomp.saulexamples.nlp.POSTagger.POSClassifiers.{BaselineClassifier, MikheevClassifier, POSTaggerKnown, POSTaggerUnknown}
import edu.illinois.cs.cogcomp.saulexamples.nlp.SpatialRoleLabeling.SpRLAnnotation
import edu.illinois.cs.cogcomp.saulexamples.nlp.TextAnnotationFactory
import edu.illinois.cs.cogcomp.saulexamples.nlp.SpatialRoleLabeling.SpRL2013.SpRL2013Document

import scala.io.StdIn
//imports necessary file reader, textannotation service and learnable for project

object CK_SpRLApp extends App {
  //creates object CK_SpRLApp
  import CK_SpRLClassifiers._
  import CK_SpRLDataModel._
  //imports classifier and datamodel scala files

  val modelDir = "models/"
  //not certain what it refers to, seems connected tsodasdfso the Learnable.scala module
  val isTrain = false
  //sets boolean attribute that switches between the training and testing mode
  val requestInput = false
  //third val, boolean for switch to input data
  //if requestinput = true, populate data model with sentence read from scanner,

  CK_PopulateDataModel(isTrain, requestInput)

  val trajectors = tokens().filter(x=> isTrajector(x).equals("true"))
  val landmarks = tokens().filter(x=> isLandmark(x).equals("true"))
  val spatialIndicators = tokens().filter(x=> isSpatialIndicator(x).equals("true"))
  //three major semantic pieces we're focusing on for Spatial Role Labeling

  /*def runClassifier(classifier: Learnable[Constituent],name: String): Unit ={
    //defines runClassifier that takes specific classifier and label name of semantic roles as arguments and returns type unit

    classifier.modelDir = modelDir + name + File.separator
    //a method of classifier, but not sure precisely what it does

    if (isTrain) {
      classifier.learn(100)
      classifier.save()
      landmarkClassifier.save()
      trajectorClassifier.save()
      spatialIndicatorClassifier.save()
      //if training, iterates learning 100 times
    }
    else{
      classifier.load()
      classifier.test()
      //if not training, tests on the testing data
    }
  }

  if (requestInput == false) {
    runClassifier(trajectorClassifier, "trajectors")
    runClassifier(landmarkClassifier, "landmarks")
    runClassifier(spatialIndicatorClassifier, "spatialIndicators")
  }*/
  if (isTrain) {
    spatialIndicatorClassifier.learn(100)
    trajectorClassifier.learn(100)
    landmarkClassifier.learn(100)
    spatialIndicatorClassifier.save()
    trajectorClassifier.save()
    landmarkClassifier.save()
    //if training, iterates learning 100 times
  }
  else if (isTrain == false && requestInput == false){
    spatialIndicatorClassifier.load()
    trajectorClassifier.load()
    landmarkClassifier.load()
    println("now testing spatial indicators")
    tokens().foreach(x => println(x, spatialIndicatorClassifier(x)))
    println("now testing trajectors")
    CK_SpRLDataModel.tokens().foreach(x => println(x, trajectorClassifier.classifier.discreteValue(x)))
    println("now testing landmarks")
    CK_SpRLDataModel.tokens().foreach(x => println(x, landmarkClassifier.classifier.discreteValue(x)))
    spatialIndicatorClassifier.test()
    trajectorClassifier.test()
    landmarkClassifier.test()
  }
  else {
    spatialIndicatorClassifier.load()
    trajectorClassifier.load()
    landmarkClassifier.load()
    println("now testing spatial indicators")
    tokens().foreach(x => println(x, spatialIndicatorClassifier(x)))
    println("now testing trajectors")
    CK_SpRLDataModel.tokens().foreach(x => println(x, trajectorClassifier.classifier.discreteValue(x)))
    println("now testing landmarks")
    CK_SpRLDataModel.tokens().foreach(x => println(x, landmarkClassifier.classifier.discreteValue(x)))

    //xClassifier takes argument type edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent
  }
}

