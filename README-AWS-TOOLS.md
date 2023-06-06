# AWS EC2 Tools

### Table of Contents
1. [Consul](#consul)
2. [Terraform](#terraform)
3. [Nomad](#nomad)
4. [Vault](#vault)

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

<a name="nomad"></a>
### Nomad

<a name="vault"></a>
### Vault