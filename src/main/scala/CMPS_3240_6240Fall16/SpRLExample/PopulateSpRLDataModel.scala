package CMPS_3240_6240Fall16.SpRLExample

import edu.illinois.cs.cogcomp.core.datastructures.textannotation._
import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager
import edu.illinois.cs.cogcomp.saulexamples.nlp.SpatialRoleLabeling.SpRL2013.SpRL2013Document
import edu.illinois.cs.cogcomp.saulexamples.nlp.SpatialRoleLabeling.SpRLConfigurator
import edu.illinois.cs.cogcomp.saulexamples.nlp.SpatialRoleLabeling.SpRLDataReader
import edu.illinois.cs.cogcomp.saulexamples.nlp.TextAnnotationFactory

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

/** Created by taher on 7/28/16.
  */
object PopulateSpRLDataModel {
  def apply() = {

    val isTraining = true
    SpRLDataModel.sentences.populate(readSpRLDocuments(), train = isTraining)

    def getDataPath(): String = {
      if (isTraining) "data/SpRL/2013/IAPR TC-12/gold"
      else  "data/SpRL/2013/IAPR TC-12/train"
    }

    def readSpRLDocuments(): List[Sentence] = {



      val path = getDataPath()
      val version = "2012"
      def readSpRL2013(): List[Sentence] = {

        //YOUR EXAMPLE
        val reader = new SpRLDataReader(path, classOf[SpRL2013Document])
        reader.readData()
        reader.documents.get(0).getTAGS.getTRAJECTOR.asScala.toList
        ///


        val sentences = ListBuffer[Sentence]()
        reader.documents.asScala.foreach(doc => {
          val ta = TextAnnotationFactory.createTextAnnotation("", "", doc.getTEXT().getContent)
          SetSpRLLabels(ta, doc.getTAGS.getTRAJECTOR.asScala.toList, "Trajector")
          SetSpRLLabels(ta, doc.getTAGS.getSPATIALINDICATOR.asScala.toList, "SpatialIndicator")
          SetSpRLLabels(ta, doc.getTAGS.getLANDMARK.asScala.toList, "Landmark")
          ta.sentences().asScala.foreach(s => sentences += s)
        })
        sentences.toList
      }

      def SetSpRLLabels(ta: TextAnnotation, tokens: List[HasSpan], label: String) = {
        tokens.foreach(t => {
          val start = t.getStart().intValue()
          val end = t.getEnd().intValue()
          if (start >= 0) {
            val startTokenId = ta.getTokenIdFromCharacterOffset(start)
            val view = ta.getView("sprl-" + label).asInstanceOf[TokenLabelView]
            val c = view.getConstituentAtToken(startTokenId)
            if (c == null)
              view.addTokenLabel(startTokenId, label, 1.0)
          }
        })
      }

      version match {
        case "2013" => readSpRL2013
        case _ =>
      }
    }
  }
}
