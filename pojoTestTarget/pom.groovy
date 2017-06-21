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
            optional true
        }

        dependency {
            groupId 'su.kore.tools.test'
            artifactId 'pojoTestSource'
            version '1-SNAPSHOT'
            classifier 'sources'
            optional true
        }
    }

    build {
        plugins {
            plugin {
                groupId 'org.bsc.maven'
                artifactId 'maven-processor-plugin'
                version '3.3.1'
                executions {
                    execution {
                        id 'process'
                        goals {
                            goal 'process'
                        }
                        phase 'generate-sources'
                        configuration {
                            appendSourceArtifacts 'true'
                            processSourceArtifacts {
                                processSourceArtifact 'su.kore.tools.test:pojoTestSource:sources'
                            }
                        }
                    }
                }
            }
            plugin {
                groupId 'org.apache.maven.plugins'
                artifactId 'maven-compiler-plugin'
                configuration {
                    source '1.8'
                    target '1.8'
                    compilerArgument '-proc:none'
                }
            }
        }
    }
}