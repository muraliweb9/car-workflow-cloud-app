# AWS EC2 Tools

### Table of Contents

1. [AWS CLI](#awsCli)
2. [Consul](#consul)
2. [Terraform](#terraform)
3. [Nomad](#nomad)
4. [Vault](#vault)

<a name="awsCli"></a>
### AWS CLI
#### Install AWS CLI
https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
Ensure `C:\Program Files\Amazon\AWSCLIV2\ ` is in the PATH. 
#### Create Credentials
![Create Credentials 1](docs/aws_ec2_tools_4.png)<br>
![Create Credentials 2](docs/aws_ec2_tools_5.png)<br>
![Create Credentials 3](docs/aws_ec2_tools_6.png)<br>
![Create Credentials 4](docs/aws_ec2_tools_7.png)<br>
![Create Credentials 5](docs/aws_ec2_tools_8.png)<br>
![Create Credentials 6](docs/aws_ec2_tools_9.png)<br>
![Create Credentials 7](docs/aws_ec2_tools_10.png)<br>
![Create Credentials 8](docs/aws_ec2_tools_11.png)<br>
![Create Credentials 9](docs/aws_ec2_tools_12.png)<br>
![Create Credentials 10](docs/aws_ec2_tools_13.png)<br>
![Create Credentials 11](docs/aws_ec2_tools_14.png)<br>
![Create Credentials 12](docs/aws_ec2_tools_15.png)<br>
![Create Credentials 13](docs/aws_ec2_tools_15_1.png)<br>

#### Configure AWS CLI
##### Configure
![Configure AWS CLI 1](docs/aws_ec2_tools_16.png)<br><br>
<a name="awsCliCred"></a>
##### Validate Credentials
![Configure AWS CLI 2](docs/aws_ec2_tools_17.png)<br>

<a name="consul"></a>
### Consul

#### Install Consul
https://www.hashicorp.com/products/consul

#### Run Consul
`consul agent -dev -node MyConsul -http-port=8501`

-node : Name of the node<br>
-http-port : The port to use (if not used defaults to 8500)<br>
````shell
C:\Apps\Consul>consul agent -dev -node MyConsul -http-port=8501
==> Starting Consul agent...
              Version: '1.15.2'
           Build Date: '2023-03-30 17:51:19 +0000 UTC'
              Node ID: '994357ab-11b0-ea96-7fa3-34e7905e18dc'
            Node name: 'MyConsul'
           Datacenter: 'dc1' (Segment: '<all>')
               Server: true (Bootstrap: false)
          Client Addr: [127.0.0.1] (HTTP: 8501, HTTPS: -1, gRPC: 8502, gRPC-TLS: 8503, DNS: 8600)
         Cluster Addr: 127.0.0.1 (LAN: 8301, WAN: 8302)
    Gossip Encryption: false
     Auto-Encrypt-TLS: false
            HTTPS TLS: Verify Incoming: false, Verify Outgoing: false, Min Version: TLSv1_2
             gRPC TLS: Verify Incoming: false, Min Version: TLSv1_2
     Internal RPC TLS: Verify Incoming: false, Verify Outgoing: false (Verify Hostname: false), Min Version: TLSv1_2
````
Check in Conul.<br>
http://localhost:8501/ui/dc1/services
![Check in Consul](docs/aws_ec2_tools_1.png)<br>

#### Service registry
E.g. in ``application.yml``
````yaml
spring:
  cloud:
    discovery:
      enabled: true
    consul:
      host: localhost
      port: 8501
      discovery:
        instanceId: ${spring.application.name}:${spring.application.instance_id:${server.port}:${random.value}}
      config:
        enabled: false
````

<a name="terraform"></a>
### Terraform

#### Install Terraform
https://developer.hashicorp.com/terraform/downloads

#### Setup Terraform
Run Terraform<br>
![Run Terraform 1](docs/aws_ec2_tools_2.png)<br>
Validate it is installed<br>
``terraform -help``<br>
![Run Terraform 2](docs/aws_ec2_tools_3.png)<br>
Start to create the .tf file with the provider "aws".<br> 
The profile default must match the AWS CLI profile [See Here](#awsCliCred).<br>
![Run Terraform 2](docs/aws_ec2_tools_18.png)<br>
Install the aws provider<br>
``terraform init``<br>
![Run Terraform 2](docs/aws_ec2_tools_19.png)<br>


<a name="nomad"></a>
### Nomad

<a name="vault"></a>
### Vault