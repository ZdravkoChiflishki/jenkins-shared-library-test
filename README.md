JENKINS-LIB [![Build Status](https://travis-ci.org/paspao/jenkins-lib.svg?branch=master)](https://travis-ci.org/paspao/jenkins-lib)
===========

Plugins needed
--------------

In order to use this library you need to install on your Jenkins the following plugins:
 
* [Pipeline Maven Integration](https://plugins.jenkins.io/pipeline-maven/)
* [NodeJS](https://plugins.jenkins.io/nodejs/) (with @angular/cli globally installed)
* [Chucknorris](https://plugins.jenkins.io/chucknorris/)



Build
-----

To build this library you need to install JDK11 and then:

```bash
gradle build
```

Or you can build it with Docker enabling **buildkit** in *daemon.json* for Docker:

```json
{
  "debug": true,
  "experimental": true,
  "features": {
    "buildkit": true
  }
}
```

and build it with:

```bash
docker build -t jenkins-lib . 
```

or without enabling **buildkit**:

```bash
DOCKER_BUILDKIT=1 docker build -t jenkins-lib .
```

**BE ATTENTION!!!** It's not needed building library to use it (Groovy is script based so it's interpreted on evry usage)


Test Environment
----------------

To test the library on Jenkins instance you can use Docker (again), the following the first time:

```bash
docker run -p 8080:8080 -p 50000:50000 --name container_name jenkins/jenkins:lts
```

then you can restart the same container with (so you will haven't any loose of configurations):

```bash
docker container start -ai container_name
```

Example Pipeline
----------------

### Maven example

```Jenkinsfile
@Library('jenkins-lib') _

jenkinslib(this, "test")
    .withMavenVersion("maven3.6.3")
    .withJdkVersion("jdk11")
    .execute()

```

### Angular example

```Jenkinsfile
@Library('jenkins-lib') _

jenkinslib(this, "test")
    .withNodeVersion("node10.19")
    .execute()



```

### Example of what the library replace

```
pipeline {
    agent any 
    tools {
        jdk 'jdk11'
    }
    
    stages {
        stage('Checkout') { 
            steps {
                ansiColor('xterm') {
                    checkout scm
                }
            }
        }
        stage('Build') { 
            steps {
                ansiColor('xterm') {
                    withMaven(maven:'maven3.6.3'){
                        sh 'mvn package'
                    }
                }
            }
        }
        
    }
}
```

