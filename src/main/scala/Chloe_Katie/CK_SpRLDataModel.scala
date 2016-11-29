/**
  * Created by Chloe Chen and Katie Roberts on 11/14/16.
  */

package Chloe_Katie

import CMPS_3240_6240Fall16.SpRLExample.SpRLSensors._
import edu.illinois.cs.cogcomp.core.datastructures.ViewNames
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.{Constituent, Sentence}
import edu.illinois.cs.cogcomp.edison.features.factory.{ParseHeadWordPOS, SubcategorizationFrame}
import edu.illinois.cs.cogcomp.saul.datamodel.DataModel
import edu.illinois.cs.cogcomp.saulexamples.nlp.CommonSensors
import edu.illinois.cs.cogcomp.saulexamples.nlp.CommonSensors._

object CK_SpRLDataModel extends DataModel {

  val sentences = node[Sentence]
  val tokens = node[Constituent]
  //creates value sentences, which is a list of sentences
  //creates value tokens, which is a list of words

  val sentencesToTokens = edge(sentences, tokens)
  //creates a relationship between sentences and tokens

  val parseView = ViewNames.PARSE_STANFORD
  //a way to incorporate feature, used in calling classifiers

  sentencesToTokens.addSensor(sentenceToTokens _)
  //adds sensors to val sentencetoTokens


  // Classification labels
  val isSpatialIndicator = property(tokens) {
    x: Constituent => x.getTextAnnotation.getView("sprl-SpatialIndicator").getLabelsCovering(x).contains("SpatialIndicator")
  }
  //creates list of constituents/words with label containing spatialIndicator

  val isLandmark = property(tokens) {
    x: Constituent => x.getTextAnnotation.getView("sprl-Landmark").getLabelsCovering(x).contains("Landmark")
  }
  //creates list of constituents/words with label containing landmark

  val isTrajector = property(tokens) {
    x: Constituent => x.getTextAnnotation.getView("sprl-Trajector").getLabelsCovering(x).contains("Trajector")
  }
  //creates list of constituents/words with label containing trajector


  // these are the different features relevant for classification
  val posTag = property(tokens) {
    x: Constituent => getPosTag(x)
  }
  //gets POS tags of constituents

  val lemma = property(tokens) {
    x: Constituent => CommonSensors.getLemma(x)
  }
  //lemmatizes all constituents

  val subcategorization = property(tokens) {
    x: Constituent => fexFeatureExtractor(x, new SubcategorizationFrame(parseView))
  }
  //creates sub-categories for constituents

  val headword = property(tokens) {
    x: Constituent => fexFeatureExtractor(x, new ParseHeadWordPOS(parseView))
  }
  //finds head word of the sentence

  //add sensors here
}
