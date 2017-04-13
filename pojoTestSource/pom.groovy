project {
    modelVersion '4.0.0'
    groupId 'su.kore.tools.test'
    artifactId 'pojoTestSource'
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
    }

    build {
        plugins {
            plugin {
                groupId 'org.apache.maven.plugins'
                artifactId 'maven-source-plugin'
                executions {
                    execution{
                        id 'attach-sources'
                        goals {
                            goal "jar"
                        }
                    }
                }
            }
        }
    }
}