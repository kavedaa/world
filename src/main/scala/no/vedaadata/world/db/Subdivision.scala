package no.vedaadata.world.db

import org.shaqal._
import model._

trait SubdivisionMapper extends PKMapper[Subdivision, String] with TableDefinition {

  val subdivisionCode = new varchar(6)("subdivisionCode") with notnull
  val countryCode = new char(2)("countryCode") with notnull
  val subdivisionName = new varchar(512)("subdivisionName") with notnull

  def fields = Seq(countryCode, subdivisionCode, subdivisionName)

  val (pk, pkf) = PK(subdivisionCode, _.subdivisionCode)

  val (reader, writer) = RW(
    implicit rs => Subdivision(subdivisionCode.read, countryCode.read, subdivisionName.read),
    x => Seq(subdivisionCode := x.subdivisionCode, countryCode := x.countryCode, subdivisionName := x.subdivisionName))

  def constraints = Seq(PrimaryKey(subdivisionCode))
}