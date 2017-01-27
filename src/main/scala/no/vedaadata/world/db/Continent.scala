package no.vedaadata.world.db

import org.shaqal._
import model._

trait ContinentMapper extends PKMapper[Continent, String] with TableDefinition {

  val code = new char(2)("code") with notnull
  val name = new varchar(100)("name") with notnull

  def fields = Seq(code, name)

  val (pk, pkf) = PK(code, _.code)

  val (reader, writer) = RW(
    implicit rs => Continent(code.read, name.read),
    x => Seq(code := x.code, name := x.name))

  def constraints = Seq(PrimaryKey(code))
}