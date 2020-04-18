package protocol.command

/**
 * @author kasonchan
 * @since 2020-03-21
 */
sealed trait Command
case object Activate extends Command
case object Status extends Command
