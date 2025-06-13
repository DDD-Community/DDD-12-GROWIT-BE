output "alb_web_sg_id" {
  value = aws_security_group.alb_web.id
}

output "ecs_service_sg_id" {
  value = aws_security_group.ecs_service.id
}

output "db_sg_id" {
  value = aws_security_group.db.id
}

output "bastion_sg_id" {
  value = aws_security_group.bastion.id
}
