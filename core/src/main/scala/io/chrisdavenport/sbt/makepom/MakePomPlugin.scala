package io.chrisdavenport.sbt.makepom

import sbt._
import Keys._
import java.nio.file._
import scala.util.control.NoStackTrace

object MakePomPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = allRequirements

  object autoImport {
    val makePomExclude: SettingKey[Boolean] = settingKey[Boolean]("Exclude check and pom.xml creation")

    val makePomCheck: TaskKey[Unit] = taskKey[Unit]("Checks current pom.xml against generated pom.xml")

    val makePomMove: TaskKey[Unit] = taskKey[Unit]("Moves Pom File, overwriting current pom.xml")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    makePomExclude := false,
    makePomCheck := {
      if (makePomExclude.value) ()
      else {
        val f = makePom.value
        val path = Paths.get(f.toURI())
        val moveTo = path.getParent().getParent().getParent()
        val file = moveTo.resolve("pom.xml")

        if (!Files.exists(file)) throw new RuntimeException(s"MakePom: Files do not match as $file does not exist") with NoStackTrace
        else {
          val pathText = Files.readString(path)
          val fileText = Files.readString(file)
          if (pathText != fileText) throw new RuntimeException(s"MakePom: Generated file and current file do not match") with NoStackTrace
          else ()
        }
      }
    },

    makePomMove := {
      if (makePomExclude.value) ()
      else {
        val f = makePom.value
        val path = Paths.get(f.toURI())
        val moveTo = path.getParent().getParent().getParent()
        val file = moveTo.resolve("pom.xml")
        Files.move(path, file)
      }
    }
  )
}