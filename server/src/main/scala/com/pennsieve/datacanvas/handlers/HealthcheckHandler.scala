// Copyright (c) 2019 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.handlers

import akka.stream.ActorMaterializer

import com.pennsieve.datacanvas.Ports
import com.pennsieve.datacanvas.server.healthcheck.{
  HealthcheckHandler => GuardrailHandler,
  HealthcheckResource
}

import com.pennsieve.datacanvas.server.healthcheck.{HealthcheckHandler => Huh}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class HealthcheckHandler(
    ports: Ports
)(
    implicit
    executionContext: ExecutionContext
) extends GuardrailHandler {

  override def healthcheck(
      respond: HealthcheckResource.healthcheckResponse.type
  )(
      ): Future[HealthcheckResource.healthcheckResponse] = {

    Future.successful(HealthcheckResource.healthcheckResponseOK)

  }
}

object HealthcheckHandler {
  def routes(
      ports: Ports
  )(
      implicit
      materializer: ActorMaterializer,
      executionContext: ExecutionContext
  ) = HealthcheckResource.routes(new HealthcheckHandler(ports))
}
