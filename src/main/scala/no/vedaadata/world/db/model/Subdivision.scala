package no.vedaadata.world.db.model


/**
  * ISO 3166-2 Subdivision Code
  * 
  * https://www.ip2location.com/free/iso3166-2
  */
case class Subdivision(
  subdivisionCode: String,
  countryCode: String,
  subdivisionName: String)