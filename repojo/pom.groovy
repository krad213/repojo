project {
    modelVersion '4.0.0'
    groupId 'su.kore.tools'
    artifactId 'repojo'
    version '1-SNAPSHOT'
    //
    // Possibly a more preferrable way to generate the groupId,
    // artifactId, and version elements:
    //
    // $artifact('io.takari.polyglot:groovy-project:0.0.1-SNAPSHOT')
    //
    String kotlinVersion = "1.1.1"

    dependencies {
        dependency{
            groupId "org.jetbrains.kotlin"
            artifactId "kotlin-stdlib"
            version kotlinVersion
        }
        dependency {
            groupId 'com.google.code.gson'
            artifactId 'gson'
            version '2.8.0'
        }
    }



    build {
        sourceDirectory "src/main/kotlin"
        plugins {
            plugin {
                groupId "org.jetbrains.kotlin"
                artifactId "kotlin-maven-plugin"
                version kotlinVersion
                executions {
                    execution {
                        id "compile"
                        goals {
                            goal "compile"
                        }
                    }
                }
            }
        }




    }
}