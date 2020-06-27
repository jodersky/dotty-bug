package macroman

case class Position()

object Position {
  import scala.quoted._

  inline given Position = ${ here }

  def here(using qctx: QuoteContext) = '{???}
}
