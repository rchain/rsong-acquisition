package coop.rchain.repo

import java.io._
import com.typesafe.scalalogging.Logger
import coop.rchain.utils.Globals._
import coop.rchain.utils.HexUtil._
import coop.rchain.domain._
import scala.util._

object RSongRepo {
  val SONG_OUT = "SONG-OUT"
  val threshold = 1024
  val log = Logger("SongRepo")

  private def logDepth(s: String): String = {
    if (s.length <= threshold)
      s""""$s""""
    else {
      val mid = s.length / 2
      val l = logDepth(s.substring(0, mid))
      val r = logDepth(s.substring(mid))
      s"""(\n$l\n++\n$r\n)"""
    }
  }

  private def readFileAsByteArray(fileName: String): Array[Byte] = {
    val bis = new BufferedInputStream(new FileInputStream(fileName))
    Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray
  }

  def asRholang(asset: RSongJsonAsset) = {
    log.info(s"name to retrieve song: ${asset.id}")
    s"""@["Immersion", "store"]!(${asset.assetData}, ${asset.jsonData}, "${asset.id}")"""
  }

  def asHexConcatRsong(filePath: String): Either[Err, String] = {
    Try {
      (readFileAsByteArray _
        andThen bytes2hex _
        andThen logDepth)(filePath)
    } match {
      case Success(s) =>
        Right(s)
      case Failure(e) =>
        Left(
          Err(ErrorCode.rsongHexConversion,
              s"${e.getMessage}",
              Some(s"fileName is ${filePath}")))
    }
  }

}
