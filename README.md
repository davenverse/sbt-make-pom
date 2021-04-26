# sbt-make-pom - Make Poms For Github Dependency Tracking [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/sbt-make-pom_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/sbt-make-pom_2.12) ![Code of Consuct](https://img.shields.io/badge/Code%20of%20Conduct-Scala-blue.svg)



## Quick Start

To use sbt-make-pom in an existing SBT project with Scala 2.11 or a later version, add the following dependencies to your
`project/plugins.sbt` depending on your needs:

```scala
addSbtPlugin("io.chrisdavenport" %% "sbt-make-pom" % "<version>")
```

### How to use

- `sbt makePomMove` - This moves files to their associated projects folder. Overwriting the current files in the location.
- `sbt makePomCheck` - This can be added to CI to make sure that the commited version matches the version that would be generated. Letting you fail CI if these are not as you expect.

- `makePomExclude` - SettingKey for SBT that will make this plugin ignore that project entirely. Can also just manually disable the plugin. `.disablePlugin(MakePomPlugin)`