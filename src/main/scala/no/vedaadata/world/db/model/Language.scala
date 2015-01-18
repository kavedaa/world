package no.vedaadata.world.db.model

/**
 *  Språkkoder basert på ISO 639.
 *  Kan importeres fra http://www-01.sil.org/iso639-3/download.asp
 */
case class Language(
  alpha3Code: String,
  alpha2Code: Option[String],
  languageScope: Char,
  languageType: Char,
  defaultName: String)