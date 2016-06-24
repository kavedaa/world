package no.vedaadata.world.db.model

import java.time.LocalDate

/**
 *  Landkoder basert på ISO 3166.
 *  Kan importeres fra https://raw.githubusercontent.com/datasets/country-codes/master/data/country-codes.csv
 *  (og evt. ISO offisiell)
 */
case class Country(
  alpha2Code: String,
  alpha3Code: Option[String],
  numericCode: Option[Int],
  name: String,
  validFrom: Option[LocalDate],
  expires: Option[LocalDate],
  currency: Option[Currency],
  defaultLanguage: Option[Language]) {
  
  def toCountryT = CountryT(
    alpha2Code,
    alpha3Code,
    numericCode,
    name,
    validFrom,
    expires,
    currency map(_.alphaCode),
    defaultLanguage map(_.alpha3Code))
}

case class CountryT(
  alpha2Code: String,
  alpha3Code: Option[String],
  numericCode: Option[Int],
  name: String,
  validFrom: Option[LocalDate],
  expires: Option[LocalDate],
  currencyCode: Option[String],
  defaultLanguageCode: Option[String])
