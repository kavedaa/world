package no.vedaadata.world.db.model

case class Continent(code: String, name: String)

/**
 *	Note: there (probably) does not exists any official ISO standard for continent codes.
 * 	These are (probably) the de facto standard codes and names.
 *
 */
object Continent {

  val AF = Continent("AF", "Africa")
  val EU = Continent("EU", "Europe")
  val AS = Continent("AS", "Asia")
  val NA = Continent("NA", "North America")
  val SA = Continent("SA", "South America")
  val AN = Continent("AN", "Antarctica")
  val OC = Continent("OC", "Oceania")
  
  val items = Seq(AF, EU, AS, NA, SA, OC, AN)
}