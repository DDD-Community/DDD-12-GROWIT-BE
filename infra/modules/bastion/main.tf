resource "aws_instance" "this" {
  ami = var.ami_id                  # Amazon Linux 2 (region 별로 다름)
  instance_type = "t2.micro"
  subnet_id     = var.public_subnet_id
  key_name      = var.key_name
  vpc_security_group_ids = [var.sg_id]

  tags = {
    Name = "${var.name}-bastion"
  }
}
