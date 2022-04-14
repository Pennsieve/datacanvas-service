variable "aws_account" {}

variable "environment_name" {}

variable "service_name" {}

variable "vpc_name" {}

variable "pennsieve_postgres_host" {}

variable "pennsieve_postgres_db" {
  default = "pennsieve_postgres"
}

variable "postgres_num_connections" {
  default = "10"
}

variable "postgres_queue_size" {
  default = "1000"
}

variable "ecs_task_iam_role_id" {}

locals {
  service = element(split("-", var.service_name), 0)
  tier    = element(split("-", var.service_name), 1)
}
