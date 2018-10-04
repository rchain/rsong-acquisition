package coop.rchain.repo

import coop.rchain.casper.protocol._
import coop.rchain.domain.{Err, ErrorCode}
import com.google.protobuf.empty._
import io.grpc.{ManagedChannel, ManagedChannelBuilder}
import com.typesafe.scalalogging.Logger
import coop.rchain.casper.protocol.DeployServiceGrpc.DeployServiceBlockingStub
import coop.rchain.utils.FileUtil

import scala.util._

object RholangProxy {

  private final val MAXGRPCSIZE = 1024 * 1024 * 500000

  def apply(host: String, port: Int): RholangProxy = {

    val channel =
      ManagedChannelBuilder
        .forAddress(host, port)
        .maxInboundMessageSize(MAXGRPCSIZE)
        .usePlaintext(true)
        .build

    new RholangProxy(channel)
  }

}

class RholangProxy(channel: ManagedChannel) {

  private lazy val grpc: DeployServiceBlockingStub =
    DeployServiceGrpc.blockingStub(channel)
  private lazy val log = Logger[RholangProxy]

  def shutdown = channel.shutdownNow()

  def deploy(contract: String): Either[Err, String] = {
    val resp = grpc.doDeploy(
      DeployData()
        .withTerm(contract)
        .withTimestamp(System.currentTimeMillis())
        .withPhloLimit(coop.rchain.casper.protocol.PhloLimit(1000000))
        .withPhloPrice(coop.rchain.casper.protocol.PhloPrice(1))
        .withNonce(0)
        .withFrom("0x1")
    )

    if (resp.success)
      Right(resp.message)
    else Left(Err(ErrorCode.grpcDeploy, resp.message, Some(contract)))
  }

  val deployFromFile: String => Either[Err, String] = path =>
    for {
      c <- FileUtil.fileFromClasspath(path)
      d <- deploy(c)
    } yield d

  def proposeBlock: Either[Err, String] =
Try(   grpc.createBlock(Empty()) ) match {
  case Success(response) if response.success =>
    Right(response.message)
  case Success(response) if ! response.success =>
    log.error(s"grpc error. error return: ${response}")
    Left(Err(ErrorCode.grpcPropose, response.message, None))
  case Failure(e) =>
    println(e)
    Left(Err(ErrorCode.grpcPropose, e.getMessage, None))
  }

}
