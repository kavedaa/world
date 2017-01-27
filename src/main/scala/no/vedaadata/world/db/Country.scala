package no.vedaadata.world.db

import org.shaqal._
import model._

trait CountryMapper extends DualPKMapper[Country, CountryT, String] with TableDefinition {

  val alpha2Code = new char(2)("alpha2Code") with notnull
  val alpha3Code = new char(3)("alpha3Code") with nullable
  val numericCode = new int("numericCode") with nullable
  val name = new varchar(500)("name") with notnull
  val validFrom = new date("validFrom") with nullable
  val expires = new date("expires") with nullable
  val currencyCode = new char(3)("currencyCode") with nullable
  val defaultLanguageCode = new char(3)("defaultLanguageCode") with nullable
  val continentCode = new char(2)("continentCode") with nullable

  def currencyTable: CurrencyMapper
  def languageTable: LanguageMapper

  val currency = new LeftJoin(currencyCode) with MapperJoin[Currency] with CurrencyMapper {
    def tableName = currencyTable.tableName
  }

  val defaultLanguage = new LeftJoin(defaultLanguageCode) with MapperJoin[Language] with LanguageMapper {
    def tableName = languageTable.tableName
  }

  def fields = Seq(alpha2Code, alpha3Code, numericCode, name, validFrom, expires, currency, defaultLanguage, continentCode)

  val (pk, pkf) = PK(alpha2Code, _.alpha2Code)

  val (reader, writer) = RW(
    implicit rs => Country(
      alpha2Code.read,
      alpha3Code.read,
      numericCode.read,
      name.read,
      validFrom.read map (_.toLocalDate),
      expires.read map (_.toLocalDate),
      currency.read,
      defaultLanguage.read,
      continentCode.read),
    c => Seq(
      alpha2Code := c.alpha2Code,
      alpha3Code := c.alpha3Code,
      numericCode := c.numericCode,
      name := c.name,
      validFrom := c.validFrom map java.sql.Date.valueOf,
      expires := c.expires map java.sql.Date.valueOf,
      currencyCode := c.currencyCode,
      defaultLanguageCode := c.defaultLanguageCode,
      continentCode := c.continentCode))

  def continentTable: ContinentMapper

  val fkContinent = ForeignKey(continentCode) references continentTable(_.code)
  
  def constraints = Seq(
    PrimaryKey(alpha2Code),
    ForeignKey(currencyCode) references currencyTable(_.alphaCode),
    ForeignKey(defaultLanguageCode) references languageTable(_.alpha3Code),
    fkContinent)
}