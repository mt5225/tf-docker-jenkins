terraform {
  backend "s3" {
    bucket = "tf-state-jerry"
    key    = "usw1.tfstate"
    region = "us-west-1"
  }
}