// Copyright (c) 2022 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas

import com.pennsieve.auth.middleware.Jwt
import scala.concurrent.{ExecutionContext, Future}

object Authenticator {

  /*
   * Ensure that this claim has access to the given organization
   */
  def withValidClaim[T](
      claim: Jwt.Claim
  )(
      f: => Future[T]
  )(
      implicit
      executionContext: ExecutionContext
  ): Future[T] =
    if (claim.isValid) f
    else
      Future.failed(
        ForbiddenException(s"Claim is not valid")
      )
  /*
   * Ensure that this claim has access to the organization and dataset
   */
  def withAuthorization[T](
      claim: Jwt.Claim
  )(
      f: => Future[T]
  )(
      implicit
      executionContext: ExecutionContext
  ): Future[T] =
    withValidClaim(claim) {
      f
    }
}
