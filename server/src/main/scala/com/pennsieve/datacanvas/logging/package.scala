// Copyright (c) 2022 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas

import akka.http.scaladsl.server.RouteResult
import akka.http.scaladsl.server.Directive
import akka.http.scaladsl.server.directives.LoggingMagnet
import akka.http.scaladsl.server.directives.DebuggingDirectives

package object logging {
  def logRequestAndResponse(ports: Ports): Directive[Unit] =
    DebuggingDirectives.logRequestResult(
      LoggingMagnet(
        _ =>
          req => {
            case RouteResult.Complete(resp) =>
              ports.logger.noContext
                .info(s"${req.method} ${req.uri} => ${resp.status}")
            case RouteResult.Rejected(rejects) =>
              ports.logger.noContext.info(
                s"${req.method} ${req.uri} => Rejected ${rejects.mkString(", ")}"
              )
          }
      )
    )
}
