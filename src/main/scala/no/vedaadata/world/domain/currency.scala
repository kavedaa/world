package no.vedaadata.world.domain

import scala.util.Try
import java.io.File
import scala.xml.XML
import java.net.URL
import no.vedaadata.world.db.model._
import org.shaqal._
import no.vedaadata.world.db.WorldDB
import scala.util._
import com.typesafe.scalalogging.LazyLogging

//	Import av data fra http://http://www.currency-iso.org/dam/downloads/lists/list_one.xml (XML-format)

case class CurrencyEntry(
  countryName: String,
  currencyName: String,
  currency: Option[String],
  currencyNumber: Option[Int],
  currencyMinorUnits: Option[Int],
  isFund: Boolean) {

  def toCurrency = currency map { curr =>
    Currency(curr, currencyNumber, currencyName, currencyMinorUnits)
  }
}

object CurrencyImporter extends LazyLogging {

  def fromWeb = fromURL(new URL("http://www.currency-iso.org/dam/downloads/lists/list_one.xml"))

  def fromURL(url: URL) = fromXml(XML load url)

  def fromFile(file: File) = fromXml(XML loadFile file)

  private def fromXml(xml: scala.xml.Node) = xml \\ "CcyNtry" map fromCcyNtry

  private def fromCcyNtry(xml: scala.xml.Node) = {

    val countryNameNode = xml \ "CtryNm"
    val currencyNameNode = xml \ "CcyNm"
    val currencyNode = xml \ "Ccy"
    val currencyNumberNode = xml \ "CcyNbr"
    val currencyMinorUnitsNode = xml \ "CcyMnrUnts"

    CurrencyEntry(
      countryNameNode.text,
      currencyNameNode.text,
      currencyNode match { case ns if ns.isEmpty => None; case ccy => Some(ccy.text) },
      Try(currencyNumberNode.text.toInt).toOption,
      Try(currencyMinorUnitsNode.text.toInt).toOption,
      (currencyNameNode \ "@IsFund").text == "true")
  }

  def importAll()(implicit c: -:[WorldDB]) = {

    logger info "Importerer currencies..."
    
    val currencies = fromWeb flatMap (_.toCurrency)
    val distinctCurrencies = currencies groupBy (_.alphaCode) map { case (alphaCode, currencies) => currencies.head }

    val results = distinctCurrencies map { currency =>
      Try { WorldDB.World.Currency += currency } recoverWith {
        case ex =>
          logger error s"Could not import $currency: ${ex.getMessage}"
          new Failure(ex)
      }
    }

    val successes = results filter (_.isSuccess)

    logger info s"Imported ${successes.size} of ${currencies.size} currencies."
  }
}