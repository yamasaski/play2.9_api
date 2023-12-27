package tag

import org.scalatest.Tag

case object NotDB extends Tag("NotDB")
case object DbSelect extends Tag("DbSelect")
case object DbUpdate extends Tag("DbUpdate")