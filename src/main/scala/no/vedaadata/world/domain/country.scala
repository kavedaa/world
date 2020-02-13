package no.vedaadata.world.domain

import scala.util._

import java.net.URL
import org.shaqal._

import com.typesafe.scalalogging.LazyLogging
import com.github.tototoshi.csv._

import no.vedaadata.world.db.model._
import no.vedaadata.world.db.WorldDB
import scala.io.Source

//  Import av data fra https://raw.githubusercontent.com/datasets/country-codes/master/data/country-codes.csv

object CountryImporter extends LazyLogging {

  def fromWeb = fromURL(new URL("https://raw.githubusercontent.com/datasets/country-codes/master/data/country-codes.csv"), "UTF-8")

  def fromURL(url: URL, enc: String) = Try {

//    val conn = url.openConnection
    //val inputStream = conn.getInputStream
    val source = Source.fromURL(url, enc)
    val reader = CSVReader open source
    val rows = reader.all
    val countries = rows drop 1 map fromRow
    //    inputStream close()
    countries
  }

  def fromRow(elems: Seq[String]) = {

    val name = elems(41)

    val alpha2Code = elems(9)

    val alpha3Code = elems(2) match { 
      case c if c.nonEmpty => Some(c)
      case _ => None 
    }

    val numericCode = Try(elems(5).toInt).toOption

    //  wrong code for Venezuela in file
    def fixCurrencyCode(c: String) = c match {
      case "VEF" => "VES"
      case x => x
    }

    val currencyCode = elems(22) match { 
      case c if c.nonEmpty => Some(fixCurrencyCode(c take 3))
      case _ => None
    }

    val continentCode = elems(50) match { 
      case c if c.nonEmpty => Some(c)
      case _ => None
    }

    CountryT(alpha2Code, alpha3Code, numericCode, name, None, None, currencyCode, None, continentCode)
  }

  def importAll(countries: Seq[CountryT])(implicit c: -:[WorldDB]) = {

    logger info "Importing countries..."

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

//  Format

// 0: FIFA
// 1: Dial
// 2: ISO3166-1-Alpha-3
// 3: MARC
// 4: is_independent
// 5: ISO3166-1-numeric
// 6: GAUL
// 7: FIPS
// 8: WMO
// 9: ISO3166-1-Alpha-2
// 10: ITU
// 11: IOC
// 12: DS
// 13: UNTERM Spanish Formal
// 14: Global Code
// 15: Intermediate Region Code
// 16: official_name_fr
// 17: UNTERM French Short
// 18: ISO4217-currency_name
// 19: Developed / Developing Countries
// 20: UNTERM Russian Formal
// 21: UNTERM English Short
// 22: ISO4217-currency_alphabetic_code
// 23: Small Island Developing States (SIDS)
// 24: UNTERM Spanish Short
// 25: ISO4217-currency_numeric_code
// 26: UNTERM Chinese Formal
// 27: UNTERM French Formal
// 28: UNTERM Russian Short
// 29: M49
// 30: Sub-region Code
// 31: Region Code
// 32: official_name_ar
// 33: ISO4217-currency_minor_unit
// 34: UNTERM Arabic Formal
// 35: UNTERM Chinese Short
// 36: Land Locked Developing Countries (LLDC)
// 37: Intermediate Region Name
// 38: official_name_es
// 39: UNTERM English Formal
// 40: official_name_cn
// 41: official_name_en
// 42: ISO4217-currency_country_name
// 43: Least Developed Countries (LDC)
// 44: Region Name
// 45: UNTERM Arabic Short
// 46: Sub-region Name
// 47: official_name_ru
// 48: Global Name
// 49: Capital
// 50: Continent
// 51: TLD
// 52: Languages
// 53: Geoname ID
// 54: CLDR display name
// 55: EDGAR