// Copyright (c) 2022 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.db

import com.pennsieve.datacanvas.NoDatacanvasException
import com.pennsieve.datacanvas.db.profile.api._
import com.pennsieve.datacanvas.models._

import java.time.{OffsetDateTime, ZoneOffset}
import scala.concurrent.ExecutionContext
import slick.dbio.{DBIOAction, Effect}

final class DatacanvasTable(tag: Tag)
    extends Table[Datacanvas](tag, Some(schema), "datacanvases") {

  def id: Rep[Int] = column[Int]("id")
  def name: Rep[String] = column[String]("name")
  def description: Rep[String] = column[String]("description")
  def createdAt: Rep[OffsetDateTime] = column[OffsetDateTime]("created_at")
  def updatedAt: Rep[OffsetDateTime] = column[OffsetDateTime]("updated_at")
  def nodeId: Rep[String] = column[String]("node_id")
  def permissionBit: Rep[Int] = column[Int]("permission_bit")
  def role: Rep[String] = column[String]("role")
  def statusId: Rep[Int] = column[Int]("status_id")

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

  def get(id: Int): Query[DatacanvasTable, Datacanvas, Seq] = {
    this.filter(_.id === id)
  }

  def getById(
      id: Int
  )(
      implicit
      executionContext: ExecutionContext
  ): DBIOAction[Datacanvas, NoStream, Effect.Read with Effect] =
    this
      .get(id)
      .result
      .headOption
      .flatMap {
        case None             => DBIO.failed(NoDatacanvasException(id))
        case Some(datacanvas) => DBIO.successful(datacanvas)
      }
}
