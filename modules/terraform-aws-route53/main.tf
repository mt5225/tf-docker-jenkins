#create zone
resource "aws_route53_zone" "primary" {
  name = var.zone_name
  tags = var.tags
}

#create sub domain
resource "aws_route53_record" "sub" {
  count   = "${length(var.sub_domains)}"
  zone_id = "${aws_route53_zone.primary.zone_id}"
  name    = "${lookup(var.sub_domains[count.index], "name")}"
  type    = "A"
  ttl     = var.ttl
  records = ["${lookup(var.sub_domains[count.index], "ipaddr")}"]
}

locals {
  subs          = "${aws_route53_record.sub}"
  cname_records = setproduct("${var.cnames}", "${aws_route53_record.sub}")
}

# create cname record for each subdomain
resource "aws_route53_record" "subsub" {
  count   = "${length(local.cname_records)}"
  zone_id = "${aws_route53_zone.primary.zone_id}"
  name    = "${local.cname_records[count.index][0]}.${local.cname_records[count.index][1].fqdn}"
  type    = "CNAME"
  ttl     = "${var.ttl}"
  records = ["${local.cname_records[count.index][1].fqdn}"]
}