// Copyright (c) 2022 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas

case object UnauthorizedException extends Throwable {}

case class NoDatacanvasException(id: Long) extends Throwable {
  override def getMessage: String = s"No Data-canvas could be found with id=$id"
}
case object DuplicateDatacanvasException extends Throwable

case class ForbiddenException(msg: String) extends Throwable {}
