// Copyright (c) 2019 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.handlers

import com.pennsieve.auth.middleware.Jwt
import com.pennsieve.datacanvas.Authenticator.withAuthorization
import com.pennsieve.datacanvas.{
  ForbiddenException,
  NoDatacanvasException,
  Ports
}
import com.pennsieve.datacanvas.db.DatacanvasMapper
import com.pennsieve.datacanvas.logging.CanvasLogContext
import com.pennsieve.datacanvas.models.DatacanvasDTO
import com.pennsieve.datacanvas.server.datacanvas.{
  DatacanvasHandler => GuardrailHandler,
  DatacanvasResource => GuardrailResource
}
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
      id: Long
  ): Future[GuardrailResource.getByIdResponse] = {
    implicit val logContext = CanvasLogContext(datacanvasId = Some(id))

    ports.db
      .run(DatacanvasMapper.getById(id))
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
            GuardrailResource.getByIdResponse.Forbidden
          case NonFatal(e) =>
            GuardrailResource.getByIdResponse.InternalServerError
        }
      }
      .recover {
        case NoDatacanvasException(_) =>
          GuardrailResource.getByIdResponse.NotFound
      }
  }
}
