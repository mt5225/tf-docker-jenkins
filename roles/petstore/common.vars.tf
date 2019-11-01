########
# common
########

variable "account" {
  description = "name of the aws account"
}

variable "created_by" {
  description = "who created me?"
  default     = "terraform"
}

variable "environment" {
  description = "environment name containing products-environment-region"
}

variable "env_tag_map" {
  type = map(string)

  default = {
    prod = "production"
    stag = "staging"
    qa   = "qa"
    dev  = "dev"
    dr   = "dr"
  }
}

variable "region" {
  type    = string
  default = "us-west-1"
}


variable "product" {
  description = "the product"
}

variable "role_config_map" {
  description = "map of vm count, disk type, disk size, vm size (in this order)"
  type        = map(string)
}
