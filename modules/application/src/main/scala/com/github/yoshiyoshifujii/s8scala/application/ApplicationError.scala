package com.github.yoshiyoshifujii.s8scala.application

sealed trait ApplicationError {
  protected val stackTrace: Array[StackTraceElement] = {
    val traces = Thread.currentThread().getStackTrace
    traces.drop(traces.lastIndexWhere(t => t.getClassName == getClass.getName) + 1)
  }

  override def toString = {
    s"""${getClass.getName}
       |${stackTrace.map(s => s"  at $s").mkString("\n")}
    """.stripMargin
  }
}

trait NoContentsError       extends ApplicationError
case object NoContentsError extends NoContentsError

trait NotFoundError       extends ApplicationError
case object NotFoundError extends NotFoundError

case class BadRequestError(message: Option[String] = None,
                           args: Seq[Map[String, String]] = Seq.empty)
    extends ApplicationError {

  override def toString = {
    s"""${getClass.getName}($message, [${args.mkString(", ")}])
       |${stackTrace.map(s => s"  at $s").mkString("\n")}
    """.stripMargin
  }

}

trait ConflictError       extends ApplicationError
case object ConflictError extends ConflictError

case class InternalServerError(cause: Throwable) extends ApplicationError
