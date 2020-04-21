package no.vedaadata.world.db

import org.shaqal._
import no.vedaadata.world.db.model.Language
import no.vedaadata.world.db.model.Currency
import no.vedaadata.world.db.model.CountryT
import no.vedaadata.world.domain.CurrencyImporter

trait WorldSchema extends SchemaLike {

  object Currency extends Table("Currency") with CurrencyMapper
  object Language extends Table("Language") with LanguageMapper

  object Continent extends Table("Continent") with ContinentMapper

  object Country extends Table("Country") with CountryMapper {
    def currencyTable = Currency
    def languageTable = Language
    def continentTable = Continent
  }

  def createTables()(implicit c: -:[D]) {
    Currency.create()
    Language.create()

    Continent.create()
    model.Continent.items foreach Continent.insertOrUpdate

    Country.create()
  }
}

trait WorldDB extends Database {

  object World extends Schema("World") with WorldSchema with SchemaDefinition

  def create(insertSampleData: Boolean = false)(implicit c: -:[D]) {

    World.create()
    World.createTables()

    if (insertSampleData) {
      World.Language ++= SampleData.sampleLanguageData
      World.Currency ++= SampleData.sampleCurrencyData
      World.Country ++= SampleData.sampleCountryData
    }
  }

}

object WorldDB extends WorldDB {
  type D = WorldDB
}

/**
 *  Small sets of sample data, useful for testing.
 */
object SampleData {

  val sampleLanguageData = Seq(
    Language("nor", Some("no"), 'M', 'L', "Norwegian"))

  //  TODO we might rather import these, but there is some type member problems in shaqal
  //  that prevents us from doing that in the database create method

  val sampleCurrencyData = Seq(
    Currency("AUD", Some(36), "Australian Dollar", Some(2)),
    Currency("BRL", Some(986), "Brazilian Real", Some(2)),
    Currency("CAD", Some(124), "Canadian Dollar", Some(2)),
    Currency("CHF", Some(756), "Swiss Franc", Some(2)),
    Currency("CZK", Some(203), "Czech Koruna", Some(2)),
    Currency("DKK", Some(208), "Danish Krone", Some(2)),
    Currency("EUR", Some(978), "Euro", Some(2)),
    Currency("GBP", Some(826), "Pound Sterlings", Some(2)),
    Currency("HKD", Some(344), "Hong Kong Dollar", Some(2)),
    Currency("INR", Some(356), "Indian Rupee", Some(2)),
    Currency("JPY", Some(392), "Yen", Some(0)),
    Currency("KRW", Some(410), "Won", Some(0)),
    Currency("NZD", Some(554), "New Zealand Dollar", Some(2)),
    Currency("NOK", Some(578), "Norwegian Krone", Some(2)),
    Currency("RUB", Some(643), "Russian Ruble", Some(2)),
    Currency("SEK", Some(752), "Swedish Krona", Some(2)),
    Currency("SGD", Some(702), "Singapore Dollar", Some(2)),
    Currency("TRY", Some(949), "Turkish Lira", Some(2)),
    Currency("USD", Some(840), "US Dollar", Some(2)))

  val sampleCountryData = Seq(
    CountryT("NO", Some("NOR"), Some(578), "Norway", None, None, Some("NOK"), Some("nor"), Some("EU")))
}