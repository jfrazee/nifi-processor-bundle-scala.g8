# nifi-processor-bundle-scala.g8

A [giter8](https://github.com/n8han/giter8) template for generating a new Scala NiFi processor bundle.

## Overview

The giter8 template is configured for Maven, Scala 2.11.7, and Java 1.8 and includes:

* `main` and `test` source directories
* [NiFi](https://github.com/apache/nifi)
* [Typesafe Config](https://github.com/typesafehub/config)
* [ScalaTest](http://www.scalatest.org/)
* [ScalaCheck](https://www.scalacheck.org)
* [scalariform-maven-plugin](https://github.com/scala-ide/scalariform)
* [Maven Central](http://search.maven.org), [Typesafe](https://bintray.com/typesafe) and [Sonatype](http://central.sonatype.org) resolvers

These variables can be modified when the template is applied:

* `basename`
* `name`
* `organization`
* `description`
* `version`
* `package`
* `classname`
* `scala_major_version`
* `scala_version`
* `nifi_version`
* `github_id`
* `developer_url`
* `project_url`

## Basic Usage

To get started using the template:

```
brew install giter8
g8 jfrazee/nifi-processor-bundle.g8
cd [NAME] && mvn compile test package
```

## Changelog

### 0.0.1

* Initial commit
* Refactored to be more idiomatic Scala, removed SSLContextService
