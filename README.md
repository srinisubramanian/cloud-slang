CloudSlang
==============
 
CloudSlang is a YAML based language for writing human-readable workflows for the CloudSlang Orchestration Engine (Score). This project includes the CLI to trigger flows.

[![Build Status](https://travis-ci.org/CloudSlang/cloud-slang.svg?branch=master)](https://travis-ci.org/CloudSlang/cloud-slang)

#### Getting started:

###### Pre-Requisite: Java JRE >= 7

1. Download the cslang zip from [here](https://github.com/CloudSlang/cloud-slang/releases/latest).
2. Unzip it.
3. Go to the folder /cslang/bin/
4. Run the executable :
  - For Windows : cslang.bat 
  - For Linux : bash cslang
5. Run the Docker example flow:  run --f ../content/org/openscore/slang/docker/containers/demo_dev_ops.sl  --cp ../content/  --inputs dockerHost=[*dockerHost*],dockerUsername=[*dockerHostUser*],dockerPassword=[*dockerHostPasword*],emailHost=[*emailHost*],emailPort=[*Emailport*],emailSender=[*EmailSender*],emailRecipient=[*EmailRecipient*]



#### Documentation :

All documentation is available on the [CloudSlang website](http://www.cloudslang.io/#/docs).

#### Get Involved

Contact us at [here](mailto:support@cloudslang.io).

#### Building and Testing from Source

###### Pre-Requisites:

1. Maven version >= 3.0.3
2. Java JDK version >= 7

###### Steps:

1. ```git clone``` the source code
2. ```mvn clean install```
3. Run the CLI executable from cloudslang-cli\target\cslang\bin 

### Another way of getting the score command line interface.
###### score-cli
> The score command line interface.

Install this globally and you'll have access to the `slang` command anywhere on your system.

```shell
npm install -g score-cli
```

Now you can just use the `slang` command anywhere
```shell
slang
```

###### Pre-Requisites:
Node.js & Java installed.

score-cli page in the [NPM repository](https://www.npmjs.com/package/score-cli).
