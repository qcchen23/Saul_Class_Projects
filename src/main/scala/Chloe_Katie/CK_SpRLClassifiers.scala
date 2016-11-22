/**
  * Created by chloechen on 11/14/16.
  */

package Chloe_Katie

import CMPS_3240_6240Fall16.SpRLExample.SpRLClassifiers.landmarkClassifier._
import CMPS_3240_6240Fall16.SpRLExample.SpRLClassifiers.spatialIndicatorClassifier._
import CMPS_3240_6240Fall16.SpRLExample.SpRLClassifiers.trajectorClassifier._
import CMPS_3240_6240Fall16.SpRLExample.SpRLDataModel._
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.Constituent
import edu.illinois.cs.cogcomp.lbjava.learn.SparseNetworkLearner
import edu.illinois.cs.cogcomp.saul.classifier.Learnable
import edu.illinois.cs.cogcomp.saul.datamodel.property.Property

object CK_SpRLClassifiers {
  import CK_SpRLDataModel._

  //creates classifier objects for each semantic role
  object spatialIndicatorClassifier extends Learnable[Constituent](tokens) {

    def label: Property[Constituent] = isSpatialIndicator
    override def feature = using(lemma, posTag, headword, subcategorization)
    override lazy val classifier = new SparseNetworkLearner()

    //taking all tokens labeled spatial indicator, using val isSpatialIndicator defined in dataModel, tells classifier
    //to learn on these tokens specifically
    //incorporates features into the classifier
    //defines classifier as a SparseNetworkLearner
  }

  object trajectorClassifier extends Learnable[Constituent](tokens) {

    def label: Property[Constituent] = isTrajector
    override def feature = using(lemma, posTag, headword, subcategorization)
    override lazy val classifier = new SparseNetworkLearner()

    //taking all tokens labeled spatial indicator, using val isTrajector defined in dataModel, tells classifier
    //to learn on these tokens specifically
    // incorporates features into the classifier
    //defines classifier as a SparseNetworkLearner
  }

  object landmarkClassifier extends Learnable[Constituent](tokens) {

    def label: Property[Constituent] = isLandmark
    override def feature = using(lemma, posTag, headword, subcategorization)
    override lazy val classifier = new SparseNetworkLearner()

    //taking all tokens labeled spatial indicator, using val isLandmark defined in dataModel, tells classifier
    //to learn on these tokens specifically
    //incorporates features into the classifier
    //defines classifier as a SparseNetworkLearner
  }
}
