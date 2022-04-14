// Copyright (c) 2019 Pennsieve, Inc. All Rights Reserved.

package com.pennsieve.datacanvas.db

import com.github.tminglei.slickpg._

trait PostgresProfile
    extends ExPostgresProfile
    with PgArraySupport
    with PgDate2Support
    with PgHStoreSupport
    with PgCirceJsonSupport {

  override val pgjson = "jsonb" // jsonb support is in postgres 9.4.0 onward; for 9.3.x use "json"

  object PostgresAPI
      extends API
      with CirceImplicits
      with DateTimeImplicits
      with ArrayImplicits

  override val api = PostgresAPI
}
