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
  
  def codeAndName = s"$alphaCode - $defaultName"
}

object Currency {

  //  default ordering

  implicit object CurrencyOrdering extends Ordering[Currency] {
    def compare(a: Currency, b: Currency) = a.defaultName compare b.defaultName
  }
}