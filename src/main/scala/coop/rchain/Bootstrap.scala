package coop.rchain

import cats.effect._
import cats.syntax.all._
import com.typesafe.config.ConfigFactory
import coop.rchain.domain._
import coop.rchain.service.moc.MocSongMetadata.mocSongs
import io.circe.generic.auto._
import io.circe.syntax._
import coop.rchain.repo.RSongRepo._
import coop.rchain.repo.RholangProxy


object Bootstrap extends IOApp {
  val cfg = ConfigFactory.load("rsong-acquisition.conf")
  val appCfg = cfg.getConfig("coop.rchain.rsong")
  val rsongPath = "/home/kayvan/dev/assets/RCHAIN Assets"

  def run(args: List[String]): IO[ExitCode] =
    args.headOption match {
      case Some(a) if a.equals("Install") =>
        IO(installContract("/rho/rsong.rho"))
          .as(ExitCode.Success)

      case Some(a) if a.equals("Deploy") =>
        IO(aquireAssets(rsongPath)).as(ExitCode.Success)

      case None =>
        IO(aquireAssets(rsongPath)).as(ExitCode.Success)

    }
  def installContract(contractFile: String) = {
    val ret =
      for {
        _ <- proxy.deployFromFile(contractFile)
        propose <- proxy.proposeBlock
      } yield (propose)
    ret match {
      case Right(a) =>
        log.info(s"contract installed successfully with return code: ${a}")
      case Left(e) => log.error(s"contract failed to install with error: ${e}")
    }
  }

  // lazy val proxy = RholangProxy("localhost", 40401)
  lazy val proxy = RholangProxy(appCfg.getString("grpc.host"), 40401)

  def aquireAssets(path: String) = {
    val ret =
      for {
        _ <- proxy.deployFromFile("/rho/rsong.rho")
        _ <- proxy.proposeBlock

        _ <- loadeAsset("Broke_Immersive.izr",
                        s"$path/Songs/Broke_Immersive.izr",
                        mocSongs("Broke"))
        _ <- loadeAsset("Broke_Stereo.izr",
                        s"$path/Songs/Broke_Stereo.izr",
                        mocSongs("Broke"))
        _ <- loadeAsset("Broke.jpg",
                        s"$path/Labels/Broke.jpg",
                        mocSongs("Broke"))

        _ <- proxy.proposeBlock
        _ <- loadeAsset("Euphoria_Immersive.izr",
                        s"$path/Songs/Euphoria_Immersive.izr",
                        mocSongs("Euphoria"))
        _ <- loadeAsset("Euphoria_Stereo.izr",
                        s"$path/Songs/Euphoria_Stereo.izr",
                        mocSongs("Euphoria"))
        _ <- loadeAsset("Euphoria.jpg",
                        s"$path/Labels/Euphoria.jpg",
                        mocSongs("Euphoria"))
        _ <- proxy.proposeBlock

        _ <- loadeAsset("Tiny_Human_Immersive.izr",
                        s"$path/Songs/Tiny_Human_Immersive.izr",
                        mocSongs("Tiny_Human"))
        _ <- loadeAsset("Tiny_Human_Stereo.izr",
                        s"$path/Songs/Tiny_Human_Stereo.izr",
                        mocSongs("Tiny_Human"))
        _ <- loadeAsset("Tiny Human.jpg",
                        s"$path/Labels/Tiny Human.jpg",
                        mocSongs("Tiny_Human"))
        propose <- proxy.proposeBlock

      } yield (propose)
    ret match {
      case Right(a) =>
        log.info(s"assets are deployed and proposed. return code: ${a} ")
      case Left(e) => log.error(s"deploying assets failed.  Error code:  ${e}")
    }

  }

  def loadeAsset(assetId: String,
                 assetPath: String,
                 metadata: SongMetadata): Either[Err, String] = {
    val ret = RSongJsonAsset(id = assetId,
                             assetData =
                               asHexConcatRsong(s"$assetPath").toOption.get,
                             jsonData = metadata.asJson.toString)
    (asRholang _ andThen proxy.deploy _)(ret)
  }

}
