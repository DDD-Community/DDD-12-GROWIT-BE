resource "aws_ecs_cluster" "cluster" {
  name = var.cluster_name
}

resource "aws_iam_role" "ecs_instance_role" {
  name = "${var.cluster_name}-ecs-instance-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          Service = "ec2.amazonaws.com"
        },
        Action = "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_instance_role_attachment" {
  role       = aws_iam_role.ecs_instance_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
}

data "aws_iam_instance_profile" "ecs_instance_profile" {
  name = "ecsInstanceRole"
}

resource "aws_launch_template" "ecs" {
  name_prefix   = "${var.cluster_name}-lt-"
  image_id = var.ami_id         # Amazon Linux 2 with ECS Agent
  instance_type = var.instance_type
  key_name      = var.key_name

  iam_instance_profile {
    name = data.aws_iam_instance_profile.ecs_instance_profile.name
  }

  credit_specification {
    cpu_credits = "standard"
  }

  user_data = base64encode(<<EOF
#!/bin/bash
echo "ECS_CLUSTER=${var.cluster_name}" >> /etc/ecs/ecs.config
echo "ECS_AVAILABLE_LOGGING_DRIVERS=[\"json-file\",\"awslogs\"]" >> /etc/ecs/ecs.config
systemctl enable --now ecs
EOF
  )
}

resource "aws_ecs_capacity_provider" "ec2_provider" {
  name = "${var.cluster_name}-capacity-provider"

  auto_scaling_group_provider {
    auto_scaling_group_arn = aws_autoscaling_group.ecs.arn
    managed_scaling {
      status                    = "ENABLED"
      target_capacity           = 100
      minimum_scaling_step_size = 1
      maximum_scaling_step_size = 1
      instance_warmup_period    = 300
    }
    managed_termination_protection = "DISABLED"
  }
}

resource "aws_ecs_cluster_capacity_providers" "attach" {
  cluster_name = aws_ecs_cluster.cluster.name

  capacity_providers = [
    aws_ecs_capacity_provider.ec2_provider.name
  ]

  default_capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.ec2_provider.name
    weight            = 1
    base              = 1
  }
}

resource "aws_autoscaling_group" "ecs" {
  desired_capacity          = 1
  min_size                  = 1
  max_size                  = 1
  vpc_zone_identifier       = var.private_subnet_ids
  health_check_grace_period = 300
  launch_template {
    id      = aws_launch_template.ecs.id
    version = "$Latest"
  }

  tag {
    key                 = "Name"
    value               = "${var.cluster_name}-ecs-node"
    propagate_at_launch = true
  }
}
