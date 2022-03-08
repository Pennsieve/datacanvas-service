// Copyright (c) 2019 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.pennsieve.auth.middleware.Jwt
//import com.pennsieve.doi.clients.{
//  CitationClient,
//  CitationClientImpl,
//  DataCiteClient,
//  DataCiteClientImpl
//}
//import com.pennsieve.doi.db.profile.api._
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

  val jwt: Jwt.Config = new Jwt.Config {
    val key: String = config.jwt.key
  }

//  val db: Database = {
//    val hikariDataSource = new HikariDataSource()
//
//    hikariDataSource.setJdbcUrl(config.postgres.jdbcURL)
//    hikariDataSource.setUsername(config.postgres.user)
//    hikariDataSource.setPassword(config.postgres.password)
//    hikariDataSource.setMaximumPoolSize(config.postgres.numConnections)
//    hikariDataSource.setDriverClassName(config.postgres.driver)
//
//    // Currently minThreads, maxThreads and maxConnections MUST be the same value
//    // https://github.com/slick/slick/issues/1938
//    Database.forDataSource(
//      hikariDataSource,
//      maxConnections = None, // Ignored if an executor is provided
//      executor = AsyncExecutor(
//        name = "AsyncExecutor.pennsieve",
//        minThreads = config.postgres.numConnections,
//        maxThreads = config.postgres.numConnections,
//        maxConnections = config.postgres.numConnections,
//        queueSize = config.postgres.queueSize
//      )
//    )
//  }

  val logger = new ContextLogger()
  val log = logger.context
}
