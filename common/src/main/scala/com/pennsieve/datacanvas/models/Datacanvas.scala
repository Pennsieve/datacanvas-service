// Copyright (c) 2022 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.models

import java.time.{OffsetDateTime, ZoneOffset}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Datacanvas(
  id: Int,
  name: String,
  description: String,
  createdAt: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
  updatedAt: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
  nodeId: String,
  permissionBit: Int,
  role: String,
  statusId: Int
)

object Datacanvas {
  implicit val decoder: Decoder[Datacanvas] = deriveDecoder[Datacanvas]
  implicit val encoder: Encoder[Datacanvas] = deriveEncoder[Datacanvas]

  /*
   * This is required by slick when using a companion object on a case
   * class that defines a database table
   */
  val tupled = (this.apply _).tupled
}
