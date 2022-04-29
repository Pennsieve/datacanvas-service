// Copyright (c) 2019 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
//import com.pennsieve.datacanvas.handlers.{DatacanvasHandler, HealthcheckHandler}
import com.pennsieve.datacanvas.handlers.HealthcheckHandler
import com.typesafe.scalalogging.StrictLogging
import pureconfig.generic.auto._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

object Server extends App with StrictLogging {
  val config: Config = pureconfig.loadConfigOrThrow[Config]

  implicit val system: ActorSystem = ActorSystem("datacanvas-service")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  implicit val ports: Ports = new Ports(config)

  def createRoutes(
      ports: Ports
  )(
      implicit
      system: ActorSystem,
      materializer: ActorMaterializer,
      executionContext: ExecutionContext
  ): Route =
    concat(
      HealthcheckHandler.routes(ports)
    )

  val routes: Route = Route.seal(HealthcheckHandler.routes(ports))

  Http().bindAndHandle(routes, config.host, config.port)
  logger.info(s"Server online at http://${config.host}:${config.port}")

  Await.result(system.whenTerminated, Duration.Inf)
}
