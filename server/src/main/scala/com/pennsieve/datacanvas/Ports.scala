// Copyright (c) 2022 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.pennsieve.auth.middleware.Jwt
import com.pennsieve.datacanvas.db.profile.api._
import com.pennsieve.service.utilities.ContextLogger
import com.zaxxer.hikari.HikariDataSource
import slick.util.AsyncExecutor

import scala.concurrent.ExecutionContext

class Ports(
    val config: Config
)(
    implicit
    system: ActorSystem,
    executionContext: ExecutionContext
) {
  val logger = new ContextLogger()
  val log = logger.context
  implicit val logContext =
    com.pennsieve.datacanvas.logging.CanvasLogContext()

  val jwt: Jwt.Config = new Jwt.Config {
    val key: String = config.jwt.key
  }

  val db: Database = {
    val hikariDataSource = new HikariDataSource()

    log.info(
      s"ports.db => jdbcUrl: ${config.postgres.jdbcURL} user: ${config.postgres.user} password: ${config.postgres.password}"
    )

    hikariDataSource.setJdbcUrl(config.postgres.jdbcURL)
    hikariDataSource.setUsername(config.postgres.user)
    hikariDataSource.setPassword(config.postgres.password)
    hikariDataSource.setMaximumPoolSize(config.postgres.numConnections)
    hikariDataSource.setDriverClassName(config.postgres.driver)

    log.info(s"ports.db => hikariDataSource: ${hikariDataSource}")

    // Currently minThreads, maxThreads and maxConnections MUST be the same value
    // https://github.com/slick/slick/issues/1938
    val _db = Database.forDataSource(
      hikariDataSource,
      maxConnections = None, // Ignored if an executor is provided
      executor = AsyncExecutor(
        name = "AsyncExecutor.pennsieve",
        minThreads = config.postgres.numConnections,
        maxThreads = config.postgres.numConnections,
        maxConnections = config.postgres.numConnections,
        queueSize = config.postgres.queueSize
      )
    )

    log.info(
      s"ports.db => hikariDataSource: ${hikariDataSource} isRunning: ${hikariDataSource.isRunning} isClosed: ${hikariDataSource.isClosed}"
    )
    _db
  }

  log.info(s"ports => db: ${db}")

}
