// JWT CONFIGURATION
resource "aws_ssm_parameter" "jwt_secret_key" {
  name      = "/${var.environment_name}/${var.service_name}/jwt-secret-key"
  overwrite = false
  type      = "SecureString"
  value     = "dummy"

  lifecycle {
    ignore_changes = [value]
  }
}

// DATABASE (POSTGRES) CONFIGURATION
resource "aws_ssm_parameter" "pennsieve_postgres_database" {
  name  = "/${var.environment_name}/${var.service_name}/pennsieve-postgres-database"
  type  = "SecureString"
  value = var.pennsieve_postgres_db
}

resource "aws_ssm_parameter" "pennsieve_postgres_host" {
  name  = "/${var.environment_name}/${var.service_name}/pennsieve-postgres-host"
  type  = "SecureString"
  value = data.terraform_remote_state.pennsieve_postgres.outputs.master_fqdn
}

resource "aws_ssm_parameter" "pennsieve_postgres_port" {
  name  = "/${var.environment_name}/${var.service_name}/pennsieve-postgres-port"
  type  = "String"
  value = data.terraform_remote_state.pennsieve_postgres.outputs.master_port
}

resource "aws_ssm_parameter" "postgres_num_connections" {
  name  = "/${var.environment_name}/${var.service_name}/postgres-num-connections"
  type  = "String"
  value = var.postgres_num_connections
}

resource "aws_ssm_parameter" "postgres_queue_size" {
  name  = "/${var.environment_name}/${var.service_name}/postgres-queue-size"
  type  = "String"
  value = var.postgres_queue_size
}

resource "aws_ssm_parameter" "pennsieve_postgres_user" {
  name  = "/${var.environment_name}/${var.service_name}/pennsieve-postgres-user"
  type  = "String"
  value = "${var.environment_name}_${replace(var.service_name, "-", "_")}_user"
}

resource "aws_ssm_parameter" "pennsieve_postgres_password" {
  name      = "/${var.environment_name}/${var.service_name}/pennsieve-postgres-password"
  overwrite = false
  type      = "SecureString"
  value     = "dummy"

  lifecycle {
    ignore_changes = [value]
  }
}
