variable "name" {
  description = "Name prefix for ALB and its related resources"
  type        = string
}

variable "vpc_id" {
  description = "VPC ID where the ALB will be deployed"
  type        = string
}

variable "public_subnet_ids" {
  description = "List of public subnet IDs where ALB will be attached"
  type = list(string)
}
