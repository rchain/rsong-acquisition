package coop.rchain.utils

import com.typesafe.config.{Config, ConfigFactory}
import coop.rchain.repo.RholangProxy

object Globals {
  lazy val cfg = ConfigFactory.load
  val appCfg = cfg.getConfig("coop.rchain.rsong")

  val artpath = "v1/art"
  val songpath = "v1/song/music"
  val rsongHostUrl: String = "http://dev-rchain.com"
  val rsongPath = "/home/kayvan/dev/assets/RCHAIN Assets"

}
