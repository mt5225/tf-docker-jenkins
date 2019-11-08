resource "aws_key_pair" "this" {
  count = var.key_count

  key_name        = var.key_name
  key_name_prefix = var.key_name_prefix
  public_key      = var.public_key
}
