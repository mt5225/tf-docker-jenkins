module "pskeypairs" {
  source = "../../modules/terraform-aws-key-pair"
  region = var.region

  tags = {
      CreateBy = var.created_by
      Module = "keypairs"
      Environment = Environment = var.env_tag_map[element(split("-", var.environment), 1)]
      Class = "Low"
      Product = var.product
  }
}
