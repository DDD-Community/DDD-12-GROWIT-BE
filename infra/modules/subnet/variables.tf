variable "name" {
  type        = string
  description = "Prefix for naming subnets"
}

variable "vpc_id" {
  type        = string
  description = "VPC ID to associate with subnets"
}

variable "public_subnet_cidrs" {
  type = list(string)
  description = "List of public subnet CIDRs"
}

variable "private_subnet_cidrs" {
  type = list(string)
  description = "List of private subnet CIDRs"
}

variable "availability_zones" {
  type = list(string)
  description = "List of AZs for subnet distribution"
}
