variable "name" {
  type        = string
  description = "Name prefix"
}

variable "private_subnet_ids" {
  type = list(string)
  description = "Private subnet IDs for DB"
}

variable "db_sg_id" {
  type        = string
  description = "DB security group ID"
}

variable "db_name" {
  type    = string
  default = "appdb"
}

variable "db_username" {
  type    = string
  default = "postgres"
}

variable "db_password" {
  type      = string
  sensitive = true
}
