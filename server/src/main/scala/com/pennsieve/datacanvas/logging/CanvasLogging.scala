// Copyright (c) 2019 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.logging

import com.pennsieve.service.utilities.LogContext

final case class CanvasLogContext(
    organizationId: Option[Int] = None,
    datasetId: Option[Int] = None,
    userId: Option[Int] = None,
    doi: Option[String] = None,
    doiId: Option[Int] = None
) extends LogContext {
  override val values: Map[String, String] = inferValues(this)
}
