module "psdns" {
  source    = "../../modules/terraform-aws-route53"
  zone_name = "qa.mt5225.com"
  ttl       = "300"
  sub_domains = [
    {
      name   = "app1"
      ipaddr = "10.0.0.1"
    },
    {
      name   = "app2"
      ipaddr = "10.0.0.2"
    },
    {
      name   = "app3"
      ipaddr = "10.0.0.3"
    },
  ]
  cnames = ["www", "api", "mail"]

  tags = {
    CreateBy    = var.created_by
    Module      = "Petstore DNS"
    Environment = var.env_tag_map[element(split("-", var.environment), 1)]
    Class       = "Low"
    Product     = var.product
  }
}

