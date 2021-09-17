mvn clean
mvn package dokka:javadocJar gpg:sign nexus-staging:deploy