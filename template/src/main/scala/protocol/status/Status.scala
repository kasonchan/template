package protocol.status

/**
  * @author kasonchan
  * @since 2020-03-21
  */
sealed trait Status
case object Ready extends Status
case object Fatal extends Status
case object Starting extends Status
