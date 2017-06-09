project {
    modelVersion '4.0.0'
    groupId 'su.kore.tools.test'
    artifactId 'pojoTestTarget'
    version '1-SNAPSHOT'
    //
    // Possibly a more preferrable way to generate the groupId,
    // artifactId, and version elements:
    //
    // $artifact('io.takari.polyglot:groovy-project:0.0.1-SNAPSHOT')
    //
    dependencies {
        dependency {
            groupId 'su.kore.tools'
            artifactId 'repojo'
            version '1-SNAPSHOT'
        }

        dependency {
            groupId 'su.kore.tools.test'
            artifactId 'pojoTestSource'
            version '1-SNAPSHOT'
            classifier 'sources'
        }
    }

    build {
        plugins {
            plugin {
                groupId 'org.apache.maven.plugins'
                artifactId 'maven-compiler-plugin'
                configuration {
                    source 1.8
                    target 1.8
                }
            }
            plugin {
                groupId 'org.apache.maven.plugins'
                artifactId 'maven-dependency-plugin'
                executions {
                    execution {
                        id 'unpack'
                        phase 'process-sources'
                        goals {
                            goal 'unpack'
                        }
                        configuration {
                            artifactItems {
                                artifactItem {
                                    groupId 'su.kore.tools.test'
                                    artifactId 'pojoTestSource'
                                    version '1-SNAPSHOT'
                                    classifier 'sources'
                                    overWrite true
                                    outputDirectory '${project.build.directory}/pojoTestSource'
                                }
                            }
                        }
                    }
                }
            }
            plugin {
                groupId 'org.codehaus.mojo'
                artifactId 'build-helper-maven-plugin'
                executions {
                    execution{
                        id 'add-source'
                        phase 'generate-sources'
                        goals {
                            goal 'add-source'
                        }
                        configuration {
                            sources {
                                source '${project.build.directory}/pojoTestSource'
                            }
                        }
                    }
                }
            }
        }
    }
}