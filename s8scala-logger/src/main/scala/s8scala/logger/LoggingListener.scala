package s8scala.logger

import ch.qos.logback.classic.spi.LoggerContextListener
import ch.qos.logback.classic.{Level, Logger, LoggerContext}
import ch.qos.logback.core.PropertyDefinerBase
import ch.qos.logback.core.spi.{ContextAwareBase, LifeCycle}

class LambdaLoggingListener extends ContextAwareBase with LoggerContextListener with LifeCycle {

  private var started             = false
  override def isStarted: Boolean = started

  override def start(): Unit = {
    if (!started) {
      val logLevel = sys.env.getOrElse("LOG_LEVEL", "INFO")
      getContext.putProperty("LAMBDA_LOG_LEVEL", logLevel)
      started = true
    }
  }

  override def stop(): Unit = {
    started = false
  }

  override def isResetResistant                                  = true
  override def onStart(context: LoggerContext): Unit             = {}
  override def onReset(context: LoggerContext): Unit             = {}
  override def onLevelChange(logger: Logger, level: Level): Unit = {}
  override def onStop(context: LoggerContext): Unit              = {}
}

class LambdaLogLevelPropertyDefiner extends PropertyDefinerBase {
  override def getPropertyValue: String =
    getContext.getProperty("LAMBDA_LOG_LEVEL")
}
