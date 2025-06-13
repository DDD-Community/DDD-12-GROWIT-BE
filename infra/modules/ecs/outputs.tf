output "cluster_id" {
  description = "The ID of the ECS cluster"
  value       = aws_ecs_cluster.cluster.id
}

output "cluster_name" {
  description = "The name of the ECS cluster"
  value       = aws_ecs_cluster.cluster.name
}

output "asg_name" {
  value = aws_autoscaling_group.ecs.name
}

output "launch_template_id" {
  value = aws_launch_template.ecs.id
}

output "execution_role_arn" {
  description = "The ARN of the execution role for ECS tasks"
  value       = aws_iam_role.ecs_instance_role.arn
}
