package no.vedaadata.world.db.model

/**
 * 	Valutakoder. Basert på ISO 4217.
 * 	Kan importeres fra http://www.currency-iso.org/en/home/tables/table-a1.html
 */
case class Currency(
  alphaCode: String,
  numericCode: Option[Int],
  defaultName: String,
  minorUnits: Option[Int]) {
  
  def render = s"$alphaCode - $defaultName"
}

object Currency {

  implicit val currencyOrdering: Ordering[Currency] = Ordering.by(_.alphaCode)
}