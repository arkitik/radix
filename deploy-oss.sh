mvn clean
mvn -DperformRelease=true package dokka:javadocJar gpg:sign nexus-staging:deploy
