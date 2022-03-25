// Copyright (c) 2019 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas

case class NoDoiException(doi: String) extends Throwable {
  override def getMessage: String = s"No doi could be found for doi=$doi"
}

case class NoDatasetDoiException(organizationid: Int, datasetId: Int)
    extends Throwable {
  override def getMessage: String =
    s"No doi could be found for organizationId=$organizationid datasetId=$datasetId"
}

case object DuplicateDoiException extends Throwable

case class ForbiddenException(msg: String) extends Throwable {}
