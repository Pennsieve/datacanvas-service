// Copyright (c) 2019 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.handlers

import akka.actor.ActorSystem
import com.pennsieve.datacanvas.Ports
import com.pennsieve.datacanvas.server.healthcheck.{
  HealthcheckHandler => GuardrailHandler,
  HealthcheckResource
}

import scala.concurrent.{ExecutionContext, Future}

class HealthcheckHandler(
    implicit
    executionContext: ExecutionContext
) extends GuardrailHandler {

  override def healthcheck(
      respond: HealthcheckResource.healthcheckResponse.type
  )(
      ): Future[HealthcheckResource.healthcheckResponse] = {

    Future.successful(HealthcheckResource.healthcheckResponse.OK)

  }
}

object HealthcheckHandler {
  def routes(
      implicit
      system: ActorSystem,
      executionContext: ExecutionContext
  ) = HealthcheckResource.routes(new HealthcheckHandler)
}
