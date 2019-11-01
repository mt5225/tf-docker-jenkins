# TF-DOCKER pipeline

this repository will create a tf pipeline for jenkins


# Op Log

## init terraform and format code

```bash
cd roles/petstore
export TF_VAR_REGION="us-west-1"
terraform init
terraform fmt
```

## plan
```bash
terraform plan -var-file ../../environments/petstore/petstore-qa-usw2.tfvars -target module.psdns -detailed-exitcode
```

## apply