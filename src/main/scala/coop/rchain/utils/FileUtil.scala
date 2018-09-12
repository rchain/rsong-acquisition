package coop.rchain.utils

import java.io.File

import coop.rchain.domain.{Err, ErrorCode}

import scala.util._

object FileUtil {

  val fileFromClasspath: String => Either[Err, String] = fileName => {
    val stream = getClass.getResourceAsStream(fileName)
    Try(
      scala.io.Source.fromInputStream(stream).getLines.reduce(_ + _ + "\n")
    ) match {
      case Success(s) =>
        stream.close
        Right(s)
      case Failure(e) =>
        stream.close
        Left(Err(ErrorCode.contractFile, fileName, None))
    }
  }
  // todo will be used in v2
  // ideally there will be acquisition related manifest outlining asset metadata and location
  private def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }
}
