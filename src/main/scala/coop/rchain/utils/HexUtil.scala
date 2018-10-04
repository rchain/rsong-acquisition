package coop.rchain.utils

object HexUtil {

  def hex2bytes(hexString: String): Array[Byte] =
    Base16.decode(hexString)

  def bytes2hex(bytes: Array[Byte]): String =
    Base16.encode(bytes)

  def chunk(buf: String): List[(String, Int)] =
    buf.grouped(1 + buf.size / 50000).zipWithIndex.toList

}
