# variables.tf
variable "db_password" {
  description = "PostgreSQL database password"
  type        = string
  sensitive   = true
}

variable "bastion_key_name" {
  description = "bastion_key_name"
  type        = string
  sensitive   = true
}
