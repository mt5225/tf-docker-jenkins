provider "aws" {
  region = var.region
}

provider "aws" {
  alias  = "east"
  region = "us-east-1"
}