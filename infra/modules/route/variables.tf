variable "name" {
  type        = string
  description = "Prefix for naming"
}

variable "vpc_id" {
  type        = string
  description = "VPC ID"
}

variable "igw_id" {
  type        = string
  description = "Internet Gateway ID"
}

variable "public_subnet_ids" {
  type = list(string)
  description = "Public Subnet IDs"
}

variable "private_subnet_ids" {
  type = list(string)
  description = "Private Subnet IDs"
}
