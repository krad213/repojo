project {
    modelVersion '4.0.0'
    groupId 'su.kore.tools'
    artifactId 'repojoRoot'
    version '1-SNAPSHOT'
    //
    // Possibly a more preferrable way to generate the groupId,
    // artifactId, and version elements:
    //
    // $artifact('io.takari.polyglot:groovy-project:0.0.1-SNAPSHOT')
    //
    packaging "pom"

    modules {
        module("repojo")
        module("pojoTestSource")
        module("pojoTestTarget")
    }
}