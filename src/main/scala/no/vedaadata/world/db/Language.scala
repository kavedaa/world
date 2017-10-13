package no.vedaadata.world.db

import org.shaqal._
import model._

trait LanguageMapper extends PKMapper[Language, String] with TableDefinition {

  val alpha3Code = new char(3)("alpha3Code") with notnull
  val alpha2Code = new char(2)("alpha2Code") with nullable
  val languageScope = new char1("languageScope") with notnull
  val languageType = new char1("languageType") with notnull
  val name = new varchar(500)("defaultName") with notnull

  def fields = Seq(alpha3Code, alpha2Code, languageScope, languageType, name)

  val (pk, pkf) = PK(alpha3Code, _.alpha3Code)

  val (reader, writer) = RW(
    implicit rs => Language(
      alpha3Code.read,
      alpha2Code.read,
      languageScope.read,
      languageType.read,
      name.read),
    l => Seq(
      alpha3Code := l.alpha3Code,
      alpha2Code := l.alpha2Code,
      languageScope := l.languageScope,
      languageType := l.languageType,
      name := l.defaultName))
      
  val uqAlpha2 = Unique(alpha2Code)

   def constraints = Seq(
     PrimaryKey(alpha3Code),
     uqAlpha2)
}