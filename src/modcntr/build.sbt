name := "test"
version := "1.0"
scalaVersion := "2.12.18"

libraryDependencies ++= Seq(
  "com.github.spinalhdl" %% "spinalhdl-core" % "1.9.4",
  "com.github.spinalhdl" %% "spinalhdl-lib" % "1.9.4"
)

fork := true
