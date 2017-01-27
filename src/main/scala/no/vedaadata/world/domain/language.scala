package no.vedaadata.world.domain

import java.net.URL
import no.vedaadata.world.db.model._
import org.shaqal._
import no.vedaadata.world.db.WorldDB
import com.typesafe.scalalogging.LazyLogging
import scala.util._

//	Import av data fra http://www-01.sil.org/iso639-3/iso-639-3.tab (tab-separert format)

object LanguageImporter extends LazyLogging {

  def fromWeb = fromURL(new URL("http://www-01.sil.org/iso639-3/iso-639-3.tab"), "UTF-8")

  def fromURL(url: URL, enc: String) = fromTabSepLines((io.Source fromURL (url, enc)).getLines.toList.tail)

  def fromTabSepLines(lines: Seq[String]) = lines map fromTabSep

  def fromTabSep(s: String) = {

    val elems = (s split "\t").toList map (_.trim)

    val alpha3Code = elems(0)
    val alpha2Code = elems(3) match { case c if c.nonEmpty => Some(c); case _ => None }
    val languageScope = elems(4) charAt 0
    val languageType = elems(5) charAt 0
    val defaultName = elems(6)

    Language(alpha3Code, alpha2Code, languageScope, languageType, defaultName)
  }

  def importAll()(implicit c: -:[WorldDB]) = {

    logger info "Importing languages..."

    val languages = fromWeb

    val results = languages map { language =>
      val res = Try { WorldDB.World.Language insertOrUpdate language }
      res match {
        case Success(Left(_))  => logger info s"Inserted ${language.defaultName}"
        case Success(Right(_)) => logger info s"Updated ${language.defaultName}"
        case Failure(ex)       => logger info s"Could not import language: ${ex.getMessage}"
      }
      res
    }

    val successes = results filter (_.isSuccess)

    logger info s"Imported ${successes.size} of ${languages.size} languages."
  }
}