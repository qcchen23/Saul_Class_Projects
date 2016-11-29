/**
  * Created by Chloe Chen and Katie Roberts on 11/14/16.
  */

package Chloe_Katie

import java.util.Properties

import edu.illinois.cs.cogcomp.core.datastructures.ViewNames
import edu.illinois.cs.cogcomp.core.datastructures.textannotation._
import edu.illinois.cs.cogcomp.nlp.common.PipelineConfigurator._
import edu.illinois.cs.cogcomp.nlp.tokenizer.StatefulTokenizer
import edu.illinois.cs.cogcomp.nlp.utility.TokenizerTextAnnotationBuilder
import edu.illinois.cs.cogcomp.saulexamples.nlp.SpatialRoleLabeling.SpRL2013.SpRL2013Document
import edu.illinois.cs.cogcomp.saulexamples.nlp.SpatialRoleLabeling.{SpRLAnnotation, SpRLDataReader}
import edu.illinois.cs.cogcomp.saulexamples.nlp.TextAnnotationFactory

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

object CK_PopulateDataModel {
  def getSentenceAnnotations(annotations: Seq[SpRLAnnotation], offset: Int, offsetEnd: Int): List[SpRLAnnotation] = {
    annotations
      .filter(x => x.getStart.intValue() >= offset && x.getStart.intValue() < offsetEnd).toList
    //extracts a list of annotations from
  }

  def apply(isTrain: Boolean, requestInput: Boolean) = {
    if (isTrain == true && requestInput == false) {
      def readSpRLDocuments(): List[Sentence] = {
        val path = if (isTrain)
          "/Users/chloechen/Documents/machineLearning/Saul_Class_Projects/data/chloedata/sprl2013-master/IAPR TC-12/gold"
        else "/Users/chloechen/Documents/machineLearning/Saul_Class_Projects/data/chloedata/sprl2013-master/IAPR TC-12/train"
        //defines path for documents to be trained or tested, based on the value of isTrain

        //YOUR EXAMPLE
        val reader = new SpRLDataReader(path, classOf[SpRL2013Document])
        //new dataReader that takes the path of desired documents
        reader.readData()
        //reads data

        val settings = new Properties()
        TextAnnotationFactory.disableSettings(settings, USE_SRL_NOM, USE_NER_ONTONOTES, USE_SRL_VERB, USE_SHALLOW_PARSE,
          USE_STANFORD_DEP, USE_NER_CONLL)
        val as = TextAnnotationFactory.createPipelineAnnotatorService(settings)

        val sentences = ListBuffer[Sentence]()
        //creates listbuffer for list of sentences

        reader.documents.asScala.foreach(doc => {
          //for each document read, apply the following
          var offset = 0

          doc.getTEXT.getContent.split("(?<=.\\n\\n)").foreach(sentence => {
            //for each sentence read, split on
            val ta = TextAnnotationFactory.createTextAnnotation(as, "", "", sentence,
              "sprl-Trajector", "sprl-Landmark", "sprl-SpatialIndicator")
            //creates annotation of spatial roles

            val offsetEnd = offset + sentence.length

            val trajectors = getSentenceAnnotations(doc.getTAGS.getTRAJECTOR.asScala, offset, offsetEnd)
            SetSpRLLabels(ta, trajectors, "Trajector", offset, offset + sentence.length)

            val sp = getSentenceAnnotations(doc.getTAGS.getSPATIALINDICATOR.asScala, offset, offsetEnd)
            SetSpRLLabels(ta, sp, "SpatialIndicator", offset, offset + sentence.length)

            val landmarks = getSentenceAnnotations(doc.getTAGS.getLANDMARK.asScala, offset, offsetEnd)
            SetSpRLLabels(ta, landmarks, "Landmark", offset, offset + sentence.length)

            ta.sentences().asScala.foreach(s => sentences += s)
            offset = offset + sentence.length
          })
        })
        sentences.toList
        //puts the sentences back into the list
      }
      CK_SpRLDataModel.sentences.populate(readSpRLDocuments(), train = isTrain)
    }

    else if (requestInput == true && isTrain == false) {

        def interactiveSpRL():edu.illinois.cs.cogcomp.core.datastructures.textannotation.Sentence= {
          print("input sentence here: ")
          val input = readLine()
          val viewsToAdd = Array(
            ViewNames.LEMMA, ViewNames.POS, ViewNames.SHALLOW_PARSE,
            ViewNames.PARSE_GOLD, ViewNames.SRL_VERB
          )
          /*val taBuilder = new TokenizerTextAnnotationBuilder(new StatefulTokenizer())
          input match {

            case input: String if input.trim.nonEmpty =>
              val settings = new Properties()
              TextAnnotationFactory.disableSettings(settings, USE_SRL_NOM, USE_NER_ONTONOTES, USE_SRL_VERB, USE_SHALLOW_PARSE,
                USE_STANFORD_DEP, USE_NER_CONLL)
              val as = TextAnnotationFactory.createPipelineAnnotatorService(settings)
              val ta = TextAnnotationFactory.createTextAnnotation(as, "", "", input,
                "sprl-Trajector", "sprl-Landmark", "sprl-SpatialIndicator")
              val textA = ta.sentences.get(0)
              print(ta.getView("sprl-Landmark"))
              return textA

            case _ => null
          }*/

          val settings = new Properties()
          TextAnnotationFactory.disableSettings(settings, USE_SRL_NOM, USE_NER_ONTONOTES, USE_SRL_VERB, USE_SHALLOW_PARSE,
            USE_STANFORD_DEP, USE_NER_CONLL)
          val as = TextAnnotationFactory.createPipelineAnnotatorService(settings)
          val ta = TextAnnotationFactory.createTextAnnotation(as, "", "", input,
            "sprl-Trajector", "sprl-Landmark", "sprl-SpatialIndicator")
          val textA = ta.sentences.get(0)
          return textA
        }

      val TextList = List(interactiveSpRL())
      CK_SpRLDataModel.sentences.populate(TextList, train = isTrain)

    }

    else {
      //the third case when it's testing on the test file
      def readSpRLDocuments(): List[Sentence] = {
        val path = if (isTrain)
          "/Users/chloechen/Documents/machineLearning/Saul_Class_Projects/data/chloedata/sprl2013-master/IAPR TC-12/gold"
        else "/Users/chloechen/Documents/machineLearning/Saul_Class_Projects/data/chloedata/sprl2013-master/IAPR TC-12/train"
        //defines path for documents to be trained or tested, based on the value of isTrain

        //YOUR EXAMPLE
        val reader = new SpRLDataReader(path, classOf[SpRL2013Document])
        //new dataReader that takes the path of desired documents
        reader.readData()
        //reads data

        val settings = new Properties()
        TextAnnotationFactory.disableSettings(settings, USE_SRL_NOM, USE_NER_ONTONOTES, USE_SRL_VERB, USE_SHALLOW_PARSE,
          USE_STANFORD_DEP, USE_NER_CONLL)
        val as = TextAnnotationFactory.createPipelineAnnotatorService(settings)

        val sentences = ListBuffer[Sentence]()
        //creates listbuffer for list of sentences

        reader.documents.asScala.foreach(doc => {
          //for each document read, apply the following
          var offset = 0

          doc.getTEXT.getContent.split("(?<=.\\n\\n)").foreach(sentence => {
            //for each sentence read, split on
            val ta = TextAnnotationFactory.createTextAnnotation(as, "", "", sentence,
              "sprl-Trajector", "sprl-Landmark", "sprl-SpatialIndicator")
            //creates annotation of spatial roles

            val offsetEnd = offset + sentence.length

            val trajectors = getSentenceAnnotations(doc.getTAGS.getTRAJECTOR.asScala, offset, offsetEnd)
            SetSpRLLabels(ta, trajectors, "Trajector", offset, offset + sentence.length)

            val sp = getSentenceAnnotations(doc.getTAGS.getSPATIALINDICATOR.asScala, offset, offsetEnd)
            SetSpRLLabels(ta, sp, "SpatialIndicator", offset, offset + sentence.length)

            val landmarks = getSentenceAnnotations(doc.getTAGS.getLANDMARK.asScala, offset, offsetEnd)
            SetSpRLLabels(ta, landmarks, "Landmark", offset, offset + sentence.length)

            ta.sentences().asScala.foreach(s => sentences += s)
            offset = offset + sentence.length

          })
        })
        sentences.toList

        //puts the sentences back into the list
      }
      CK_SpRLDataModel.sentences.populate(readSpRLDocuments(), train = isTrain)

    }
  }

    def SetSpRLLabels(ta: TextAnnotation, tokens: List[SpRLAnnotation], label: String, sentStart: Int, sentEnd: Int) = {
      tokens.foreach(t => {
        val start = t.getStart().intValue() - sentStart
        if (start >= 0 && start < sentEnd) {
          val startTokenId = ta.getTokenIdFromCharacterOffset(start)
          val view = ta.getView("sprl-" + label).asInstanceOf[TokenLabelView]
          val c = view.getConstituentAtToken(startTokenId)
          if (c == null)
            view.addTokenLabel(startTokenId, label, 1.0)
        }
      })
    }
}