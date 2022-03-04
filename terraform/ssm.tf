// DATABASE (POSTGRES) CONFIGURATION


// JWT CONFIGURATION
resource "aws_ssm_parameter" "doi_jwt_secret_key" {
  name      = "/${var.environment_name}/${var.service_name}/doi-jwt-secret-key"
  overwrite = false
  type      = "SecureString"
  value     = "dummy"

  lifecycle {
    ignore_changes = [value]
  }
}