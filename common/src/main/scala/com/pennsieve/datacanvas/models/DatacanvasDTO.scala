// Copyright (c) 2022 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.models

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import java.time.{OffsetDateTime, ZoneOffset}

case class DatacanvasDTO(
                          id: Long,
                          name: String,
                          description: String,
                          createdAt: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
                          updatedAt: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),
                          nodeId: String,
                          permissionBit: Long,
                          role: String,
                          statusId: Long
                 )

object DatacanvasDTO {
  implicit val decoder: Decoder[DatacanvasDTO] = deriveDecoder[DatacanvasDTO]
  implicit val encoder: Encoder[DatacanvasDTO] = deriveEncoder[DatacanvasDTO]

  def apply(internalDatacanvas: Datacanvas): DatacanvasDTO =
    DatacanvasDTO(
      id = internalDatacanvas.id,
      name = internalDatacanvas.name,
      description = internalDatacanvas.description,
      createdAt = internalDatacanvas.createdAt,
      updatedAt = internalDatacanvas.updatedAt,
      nodeId = internalDatacanvas.nodeId,
      permissionBit = internalDatacanvas.permissionBit,
      role = internalDatacanvas.role,
      statusId = internalDatacanvas.statusId
    )
}