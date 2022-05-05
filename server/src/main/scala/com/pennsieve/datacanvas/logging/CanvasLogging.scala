// Copyright (c) 2022 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.logging

import com.pennsieve.service.utilities.LogContext

final case class CanvasLogContext(
    organizationId: Option[Int] = None,
    datasetId: Option[Int] = None,
    userId: Option[Int] = None,
    datacanvasId: Option[Long] = None
) extends LogContext {
  override val values: Map[String, String] = inferValues(this)
}
