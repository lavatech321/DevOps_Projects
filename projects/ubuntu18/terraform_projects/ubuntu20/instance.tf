AWS_ACCESS_KEY = "AKIAZ2UALN2M4EU77KHF"
AWS_SECRET_KEY = "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"
AWS_REGION = "us-west-2"

variable  "AWS_ACCESS_KEY" {
}

variable "AWS_SECRET_KEY" {
}

variable "AWS_REGION"{
  default = "us-east-1"
}

variable "AMI"{
   default ="ami-026b57f3c383c2eec"
}

provider "aws" {  
  access_key = "${var.AWS_ACCESS_KEY}"
  secret_key = "${var.AWS_SECRET_KEY}"
  region = "${var.AWS_REGION}"
}

resource "aws_instance" "instance3" {
  ami = "${lookup(var.AMI,var.AWS_REGION)}"
  instance_type = "t2.micro"
}

