package coop.rchain.utils

import org.specs2._

class HexUtilsSpec extends Specification {
  def is =
    s2"""
       Hext to Bytes conversion specs
          to base 16 encoding $e1
    """
  import HexUtil._

  def e1 = {
    val data = "48656c6c6f20576f726c642121"
    bytes2hex(hex2bytes(data)) === data
  }

}
