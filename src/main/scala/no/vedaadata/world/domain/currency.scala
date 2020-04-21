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

//	Import av data fra https://www.currency-iso.org (XML-format)

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

  def fromWeb = fromURL(new URL("https://www.currency-iso.org/dam/downloads/lists/list_one.xml"))

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

  def importAll(entries: Seq[CurrencyEntry])(implicit c: -:[WorldDB]) = {

    logger info "Importing currencies..."

    val currencies = entries flatMap (_.toCurrency)
    val distinctCurrencies = currencies groupBy (_.alphaCode) map { case (alphaCode, currencies) => currencies.head }

    val results = distinctCurrencies map { currency =>
      val res = Try { WorldDB.World.Currency insertOrUpdate currency }
      res match {
        case Success(Left(_))  => logger info s"Inserted ${currency.defaultName}"
        case Success(Right(_)) => logger info s"Updated ${currency.defaultName}"
        case Failure(ex)       => logger info s"Could not import $currency: ${ex.getMessage}"
      }
      res
    }

    val successes = results filter (_.isSuccess)

    logger info s"Imported ${successes.size} of ${distinctCurrencies.size} currencies."
  }
}