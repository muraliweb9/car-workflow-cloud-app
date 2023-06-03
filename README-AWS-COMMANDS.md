# AWS EC2 Management

### EC2 Connect

#### User Data
* These are items such as start up scripts to run on instance create and star.
![User Data step 1](docs/aws_ec2_connect_user_data_1.png)

* The "script" to run.
![User Data step 2](docs/aws_ec2_connect_user_data_2.png)

* Start the instance.
![User Data step 3](docs/aws_ec2_connect_user_data_3.png)

* The script.
````shell
Content-Type: multipart/mixed; boundary="//"
MIME-Version: 1.0

--//
Content-Type: text/cloud-config; charset="us-ascii"
MIME-Version: 1.0
Content-Transfer-Encoding: 7bit
Content-Disposition: attachment; filename="cloud-config.txt"

#cloud-config
cloud_final_modules:
- [scripts-user, always]

--//
Content-Type: text/x-shellscript; charset="us-ascii"
MIME-Version: 1.0
Content-Transfer-Encoding: 7bit
Content-Disposition: attachment; filename="userdata.txt"

#!/bin/bash
/bin/echo "Hello Murali K" >> /tmp/mk.txt
/bin/echo "Running custom script at Instance start !!"
--//--
````
* Check in Console.
![User Data step 4](docs/aws_ec2_connect_user_data_4.png)
![User Data step 5](docs/aws_ec2_connect_user_data_5.png)
![User Data step 6](docs/aws_ec2_connect_user_data_6.png) 

### CLI
