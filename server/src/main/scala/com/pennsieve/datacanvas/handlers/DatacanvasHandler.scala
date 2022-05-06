// Copyright (c) 2022 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.handlers

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.directives.HeaderDirectives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import cats.implicits._
import com.pennsieve.auth.middleware.Jwt
import com.pennsieve.auth.middleware.AkkaDirective.authenticateJwt
import com.pennsieve.datacanvas.Authenticator.withAuthorization
import com.pennsieve.datacanvas.{
  ForbiddenException,
  NoDatacanvasException,
  Ports,
  UnauthorizedException
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
import slick.dbio.DBIO

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class DatacanvasHandler(
    ports: Ports,
    authorization: Option[Authorization],
    claim: Option[Jwt.Claim]
)(
    implicit executionContext: ExecutionContext
) extends GuardrailHandler {

  def authorized(): DBIO[Unit] = {
    DBIO.from {
      authorization match {
        case Some(_) => Future.successful(())
        case None    => Future.failed(UnauthorizedException)
      }
    }
  }

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

    val query = for {
      _ <- authorized()

      canvas <- DatacanvasMapper.getById(datacanvasId)
    } yield canvas

    ports.db
      .run(query)
      .flatMap { internalDatacanvas =>
        Future(
          GuardrailResource.getByIdResponse.OK(
            DatacanvasDTO.apply(internalDatacanvas).asJson
          )
        )
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
      optionalHeaderValue {
        case header @ Authorization(OAuth2BearerToken(_)) => Some(header)
        case _                                            => None
      } {
        case authorization @ Some(_) =>
          authenticateJwt(system.name)(ports.jwt) { claim =>
            GuardrailResource.routes(
              new DatacanvasHandler(ports, authorization, claim.some)(
                executionContext
              )
            )
          }
        case _ =>
          GuardrailResource.routes(
            new DatacanvasHandler(ports, None, None)(executionContext)
          )
      }
    }
  }
}
