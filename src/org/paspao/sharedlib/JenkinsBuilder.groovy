package org.paspao.sharedlib

import groovy.transform.PackageScope


class JenkinsBuilder {

    private final def script
    private final String projectName
    private final String branchName
    private String agentName
    private String mavenVersion
    private String jdkVersion
    private String nodeVersion

    JenkinsBuilder(def script, String projectName, String branchName) {
        this.script = script
        this.projectName = projectName
        this.branchName = branchName
    }

    JenkinsBuilder withMavenVersion(String mavenVersion) {
        this.mavenVersion = mavenVersion
        return this
    }

    JenkinsBuilder withJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion
        return this
    }
    JenkinsBuilder withNodeVersion(String nodeVersion) {
        this.nodeVersion = nodeVersion
        return this
    }

    JenkinsBuilder withAgent(String agent) {
        this.agentName = agent
        return this
    }


    void execute() {
        pipeline {
            node {
                try {
                    maven()
                    jdk()
                    nodeEnv()

                    stage('Checkout') {
                        ansiColor {
                            checkout()
                        }
                    }
                    if(mavenVersion) {
                        stage('Build Maven') {
                            ansiColor {

                                mavenBuild()

                            }
                        }
                    }
                    if(nodeVersion) {
                        stage('Build Node') {
                            ansiColor {

                                nodeBuild()

                            }
                        }
                    }
                } finally {
                    script.chuckNorris()
                }
            }


        }
    }


    @PackageScope
    def checkout() {
        script.checkout(script.scm)
    }

    @PackageScope
    def stage(String title, Closure closure) {
        script.stage(title) {
            closure.call()
        }
    }

    @PackageScope
    def node(String label = null, Closure closure) {
        if (label) {
            script.node(label) {
                closure.call()
            }
        } else {
            script.node {
                closure.call()
            }
        }
    }

    @PackageScope
    def ansiColor(Closure closure) {
        script.timestamps {
            script.ansiColor('xterm') {
                closure.call()
            }
        }
    }

    @PackageScope
    def pipeline(Closure closure) {

        script.pipeline {
            closure.call()
        }

    }

    @PackageScope
    def maven() {
        if (mavenVersion) {
            script.tool(type: 'maven', name: mavenVersion)
        }
    }

    @PackageScope
    def mavenBuild()
    {
        if (mavenVersion) {
            script.withMaven(
                    // Maven installation declared in the Jenkins "Global Tool Configuration"
                    maven: mavenVersion, jdk: jdkVersion) {

                // Run the maven build
                script.sh "mvn clean package"

            }
        }
    }

    @PackageScope
    def nodeBuild()
    {
        if (nodeVersion) {
            script.nodejs(
                    nodeJSInstallationName: nodeVersion) {


                script.sh "npm install"
                script.sh "npm run-script build"

            }
        }
    }

    @PackageScope
    def jdk() {
        if (jdkVersion) {
            script.tool(type: 'jdk', name: jdkVersion)
        }
    }

    @PackageScope
    def nodeEnv() {
        if (nodeVersion) {
            script.tool(type: 'nodejs', name: nodeVersion)
        }
    }


}
