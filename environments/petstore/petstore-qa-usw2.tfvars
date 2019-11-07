vpc_cidr = "10.61.0.0/16"

azs = ["us-west-2a", "us-west-2b", "us-west-2c"]

environment = "petstore-qa-usw2"

account = "petstoreaws6"

product = "PetStore"

# map of role configuration
#  aws-asg = count min, desire, max, instance type, ami to use
#  aws-ec2 = count of instances, instance type, ami to use, disk size
#  aws-rds = count of instances, instance class
#  aws-elasticache = count, instance type

role_config_map = {
  petstoresvr = "1,2,2,t2.small,ami-082b5a644766e0e6f"
}

region = "us-west-2"