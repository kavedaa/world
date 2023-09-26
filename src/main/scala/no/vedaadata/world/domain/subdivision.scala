package no.vedaadata.world.domain

import scala.util._

import java.io.File

import com.typesafe.scalalogging.LazyLogging

import org.shaqal._

import com.github.tototoshi.csv._

import no.vedaadata.world.db.model._
import no.vedaadata.world.db.WorldDB

object SubdivisionImporter extends LazyLogging {

  def fromFile(file: File) = Try {

    val reader = CSVReader.open(file.getAbsolutePath)
    val rows = reader.all()
    rows.drop(1).map(fromRow)
  }

  def fromRow(elems: Seq[String]) = {

    val countryCode = elems(0)
    val subdivisionName = elems(1)
    val subDivisionCode = elems(2)

    Subdivision(subDivisionCode, countryCode, subdivisionName)
  }

  def importAll(subdivisions: Seq[Subdivision])(implicit c: -:[WorldDB]) = {

    logger info "Importing subdivisions..."

    val results = subdivisions map { subdivision =>
      val res = Try { WorldDB.World.Subdivision.insertOrUpdate(subdivision) }
      res match {
        case Success(Left(_))  => logger info s"Inserted ${subdivision.subdivisionName}"
        case Success(Right(_)) => logger info s"Updated ${subdivision.subdivisionName}"
        case Failure(ex)       => logger info s"Could not import $subdivision: ${ex.getMessage}"
      }
      res
    }

    val successes = results filter (_.isSuccess)

    logger info s"Imported ${successes.size} of ${subdivisions.size} subdivisions."
  }
}
