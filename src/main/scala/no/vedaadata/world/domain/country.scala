package no.vedaadata.world.domain

import java.net.URL
import scala.util._
import no.vedaadata.world.db.model._
import com.bizo.mighty.csv.CSVReader
import org.shaqal._
import no.vedaadata.world.db.WorldDB
import com.typesafe.scalalogging.LazyLogging

//  Import av data fra https://raw.githubusercontent.com/datasets/country-codes/master/data/country-codes.csv

object CountryImporter extends LazyLogging {

  def fromWeb = fromURL(new URL("https://raw.githubusercontent.com/datasets/country-codes/master/data/country-codes.csv"), "UTF-8")

  def fromURL(url: URL, enc: String) = Try {

    val conn = url.openConnection
    val inputStream = conn.getInputStream
    val rows: Iterator[Array[String]] = CSVReader(inputStream)
    val countries = rows.toSeq drop 1 map fromRow
    //    inputStream close()
    countries
  }

  def fromRow(elems: Array[String]) = {

    val name = elems(0)
    val alpha2Code = elems(3)
    val alpha3Code = elems(4) match { case c if c.nonEmpty => Some(c); case _ => None }
    val numericCode = Try(elems(5).toInt).toOption
    val currencyCode = elems(15) match { case c if c.nonEmpty => Some(c); case _ => None }
    val continentCode = elems(22) match { case c if c.nonEmpty => Some(c); case _ => None }

    CountryT(alpha2Code, alpha3Code, numericCode, name, None, None, currencyCode, None, continentCode)
  }

  def importAll()(implicit c: -:[WorldDB]) = {

    logger info "Importing countries..."

    fromWeb foreach { countries =>

      val results = countries map { country =>
        val res = Try { WorldDB.World.Country insertOrUpdate country }
        res match {
          case Success(Left(_))  => logger info s"Inserted ${country.name}"
          case Success(Right(_)) => logger info s"Updated ${country.name}"
          case Failure(ex)       => logger info s"Could not import $country: ${ex.getMessage}"
        }
        res
      }

      val successes = results filter (_.isSuccess)

      logger info s"Imported ${successes.size} of ${countries.size} countries."

    }
  }
}