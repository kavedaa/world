package no.vedaadata.world.db

import org.shaqal._

trait WorldSchema extends SchemaLike {
  
  object Currency extends Table("Currency") with CurrencyMapper
  object Language extends Table("Language") with LanguageMapper
  
  object Country extends Table("Country") with CountryMapper {
    def currencyTable = Currency
    def languageTable = Language
  }
  
  def createTables()(implicit c: -:[D]) {
    Currency create()
    Language create()
    Country create()
  }
}

trait WorldDB extends Database {
  
  object World extends Schema("World") with WorldSchema with SchemaDefinition
  
  def create()(implicit c: -:[D]) {

    World create ()
    World createTables ()
  }
  
}

object WorldDB extends WorldDB {
  type D = WorldDB
}