module "pskeypairs" {
  source = "../../modules/terraform-aws-key-pair"

  tags = {
    CreateBy    = var.created_by
    Module      = "petstore keypairs"
    Environment = var.env_tag_map[element(split("-", var.environment), 1)]
    Class       = "Low"
    Product     = var.product
  }
}
