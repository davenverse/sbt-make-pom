package io.chrisdavenport.sbt.makepom

import sbt._
import Keys._
import java.nio.file._
import scala.util.control.NoStackTrace

object ManualMakePomPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = PluginTrigger.NoTrigger

  object autoImport {
    val pomTargetLocation: SettingKey[Option[File]] = settingKey("Location for destination of POM file")

    val makePomCheck: TaskKey[Unit] = taskKey[Unit]("Checks current pom.xml against generated pom.xml")

    val makePomMove: TaskKey[Unit] = taskKey[Unit]("Moves Pom File, overwriting current pom.xml")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    pomTargetLocation := None,
    makePomCheck := {
      val f = makePom.value
      val origin = Paths.get(f.toURI())
      val destination = pomTargetLocation.value.map(f => f.toPath)
        .getOrElse(origin.getParent().getParent().getParent().resolve("pom.xml"))

      if (!Files.exists(destination)) throw new RuntimeException(s"MakePom: Files do not match as $destination does not exist") with NoStackTrace
      else {
        val pathText = new String(Files.readAllBytes(origin))
        val fileText = new String(Files.readAllBytes(destination))
        if (pathText != fileText) throw new RuntimeException(s"MakePom: Generated file and current file do not match") with NoStackTrace
        else ()
      }
    },

    makePomMove := {
      val f = makePom.value
      val origin = Paths.get(f.toURI())
      val destination = pomTargetLocation.value.map(f => f.toPath)
        .getOrElse(origin.getParent().getParent().getParent().resolve("pom.xml"))
      if (Files.exists(destination)) Files.delete(destination) else ()
      Files.move(origin, destination)
    }
  )
}