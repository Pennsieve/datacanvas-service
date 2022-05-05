// Copyright (c) 2022 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.handlers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Route
import com.pennsieve.auth.middleware.Jwt
import com.pennsieve.auth.middleware.AkkaDirective.authenticateJwt
import com.pennsieve.datacanvas.Authenticator.withAuthorization
import com.pennsieve.datacanvas.{
  ForbiddenException,
  NoDatacanvasException,
  Ports
}
import com.pennsieve.datacanvas.db.DatacanvasMapper
import com.pennsieve.datacanvas.logging.CanvasLogContext
import com.pennsieve.datacanvas.logging.logRequestAndResponse
import com.pennsieve.datacanvas.models.DatacanvasDTO
import com.pennsieve.datacanvas.server.datacanvas.{
  DatacanvasHandler => GuardrailHandler,
  DatacanvasResource => GuardrailResource
}
import com.pennsieve.service.utilities.LogContext
import com.typesafe.scalalogging.LoggerTakingImplicit
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class DatacanvasHandler(
    claim: Jwt.Claim
)(
    implicit
    ports: Ports,
    executionContext: ExecutionContext
) extends GuardrailHandler {

  override def getById(respond: GuardrailResource.getByIdResponse.type)(
      organizationId: Int,
      datacanvasId: Int
  ): Future[GuardrailResource.getByIdResponse] = {
    implicit val logContext = CanvasLogContext(
      organizationId = Some(organizationId),
      datacanvasId = Some(datacanvasId)
    )
    ports.log.info(
      s"getById() organizationId: ${organizationId} datacanvasId: ${datacanvasId}"
    )
    ports.db
      .run(DatacanvasMapper.getById(datacanvasId))
      .flatMap { internalDatacanvas =>
        withAuthorization[GuardrailResource.getByIdResponse](
          claim
        ) {
          Future(
            GuardrailResource.getByIdResponse.OK(
              DatacanvasDTO.apply(internalDatacanvas).asJson
            )
          )
        }.recover {
          case ForbiddenException(e) =>
            GuardrailResource.getByIdResponse.Forbidden(
              "operation is forbidden"
            )
          case NonFatal(e) =>
            GuardrailResource.getByIdResponse.InternalServerError(e.toString)
        }
      }
      .recover {
        case NoDatacanvasException(_) =>
          GuardrailResource.getByIdResponse.NotFound("data-canvas not found")
      }
  }
}

object DatacanvasHandler {

  def routes(
      ports: Ports
  )(
      implicit
      system: ActorSystem,
      materializer: ActorMaterializer,
      executionContext: ExecutionContext
  ): Route = {
    logRequestAndResponse(ports) {
      authenticateJwt(system.name)(ports.jwt) { claim =>
        GuardrailResource.routes(
          new DatacanvasHandler(claim)(ports, executionContext)
        )
      }
    }
  }
}
