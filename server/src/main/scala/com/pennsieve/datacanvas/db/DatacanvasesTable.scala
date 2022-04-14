// Copyright (c) 2019 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.db

import com.pennsieve.datacanvas.NoDatacanvasException
import com.pennsieve.datacanvas.db.profile.api._
import com.pennsieve.datacanvas.models._

import java.time.{OffsetDateTime, ZoneOffset}
import scala.concurrent.ExecutionContext

final class DatacanvasTable(tag: Tag)
    extends Table[Datacanvas](tag, Some(schema), "datacanvases") {

  def id: Rep[Long] = column[Long]("id")
  def name: Rep[String] = column[String]("name")
  def description: Rep[String] = column[String]("description")
  def createdAt: Rep[OffsetDateTime] = column[OffsetDateTime]("created_at")
  def updatedAt: Rep[OffsetDateTime] = column[OffsetDateTime]("updated_at")
  def nodeId: Rep[String] = column[String]("node_id")
  def permissionBit: Rep[Long] = column[Long]("permission_bit")
  def role: Rep[String] = column[String]("role")
  def statusId: Rep[Long] = column[Long]("status_id")

  def * =
    (
      id,
      name,
      description,
      createdAt,
      updatedAt,
      nodeId,
      permissionBit,
      role,
      statusId
    ).mapTo[Datacanvas]
}

object DatacanvasMapper extends TableQuery(new DatacanvasTable(_)) {
  def getById(
      id: Long
  )(
      implicit
      executionContext: ExecutionContext
  ): DBIOAction[Datacanvas, NoStream, Effect.Read with Effect] =
    this
      .filter(_.id === id)
      .result
      .headOption
      .flatMap {
        case None             => DBIO.failed(NoDatacanvasException(id))
        case Some(datacanvas) => DBIO.successful(datacanvas)
      }
}
