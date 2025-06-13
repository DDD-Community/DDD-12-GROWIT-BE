variable "cluster_name" {
  description = "Name of the ECS Cluster"
  type        = string
}

variable "ami_id" {
  description = "AMI ID to use for the ECS EC2 instances"
  type        = string
}

variable "key_name" {
  description = "Key pair name to allow SSH access to the instances"
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type for ECS (e.g. t3.micro)"
  type        = string
}

variable "private_subnet_ids" {
  description = "List of private subnet IDs for ECS instances"
  type = list(string)
}
