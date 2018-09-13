package coop.rchain.utils

import org.specs2.Specification
import FileUtil._

class FileUtilSpec extends Specification {
  def is = s2"""
 fileutils specs
    compute rsong contract $readRsongContract
 """

  def readRsongContract = {
    val filename = Globals.appCfg.getString("contract.file.name")
    println(s"contract file name is: ${filename}")

    val computed = fileFromClasspath("/rho/rsong.rho")
    println(s"contract is: ${computed}")

    computed.isRight === true
  }
}
