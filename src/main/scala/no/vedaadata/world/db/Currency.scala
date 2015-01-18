package no.vedaadata.world.db

import org.shaqal._
import model._

trait CurrencyMapper extends PKMapper[Currency, String] with TableDefinition {

  val alphaCode = new char(3)("alphaCode") with notnull
  val numericCode = new int("numericCode") with nullable
  val name = new varchar(500)("name") with notnull
  val minorUnit = new int("minorUnit") with nullable

  def fields = Seq(alphaCode, numericCode, name, minorUnit)

  val (pk, pkf) = PK(alphaCode, _.alphaCode)

  val (reader, writer) = RW(
    implicit rs => Currency(alphaCode.read, numericCode.read, name.read, minorUnit.read),
    c => Seq(alphaCode := c.alphaCode, numericCode := c.numericCode, name := c.defaultName, minorUnit := c.minorUnits))

  def constraints = Seq(PrimaryKey(alphaCode))
}
