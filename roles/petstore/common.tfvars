########
# common
########

variable "region" {
  description = "name of the aws region"
}

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

variable "product" {
  description = "the product"
}

variable "role_config_map" {
  description = "map of vm count, disk type, disk size, vm size (in this order)"
  type        = map(string)
}

variable "chef_endpoint" {
}

variable "chef_organization" {
  default = "default"
}

variable "chef_environment" {
  default = "_default"
}

########
# vpn
########
variable "transit1_config" {
  description = "map for transit 1 containing tunnel1_inside_cidr, tunnel2_inside_cidr, customer_gateway_id"
  type        = map(string)
}

########
# network
########
variable "azs" {
  type        = list(string)
  description = "Number of availability zones in the region"
}

variable "internal_allow_list" {
  type        = list(string)
  description = "list of internal ips"
}

variable "vpc_cidr" {
  description = "The CIDR block for the VPC. Default value is a valid CIDR, but not acceptable by AWS and should be overriden"
}

variable "transit_gateway_id" {
  description = "Identifier of EC2 Transit Gateway."
}

variable "tgw_share_invitation_arn" {
  description = "The arn of the ARM share invetation (aws ram get-resource-share-invitations)"
}
