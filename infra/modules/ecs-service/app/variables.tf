variable "name" {
  description = "The name of the ECS service and task"
  type        = string
}

variable "image" {
  description = "Docker image to run in the ECS task"
  type        = string
}

variable "container_port" {
  description = "Port the container listens on"
  type        = number
}

variable "execution_role_arn" {
  description = "IAM Role ARN for ECS task execution"
  type        = string
}

variable "cluster_id" {
  description = "ID of the ECS cluster to deploy into"
  type        = string
}

variable "private_subnet_ids" {
  description = "List of private subnet IDs for the ECS tasks"
  type = list(string)
}

variable "sg_id" {
  description = "Security Group ID for the ECS tasks"
  type        = string
}

variable "target_group_arn" {
  description = "ARN of the ALB Target Group to register the service with"
  type        = string
}

variable "alb_listener_arn" {
  description = "ARN of the ALB listener to attach the target group"
  type        = string
}
