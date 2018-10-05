addSbtPlugin("com.eed3si9n"       % "sbt-unidoc"                    % "0.4.2")
addSbtPlugin("com.github.gseitz"  % "sbt-release"                   % "1.0.8")
addSbtPlugin("com.jsuereth"       % "sbt-pgp"                       % "1.1.2")
addSbtPlugin("com.jsuereth"       % "sbt-pgp"                       % "1.1.1")
addSbtPlugin("org.xerial.sbt"     % "sbt-sonatype"                  % "2.3")
addSbtPlugin("org.scala-js"       % "sbt-scalajs"                   % "0.6.25")
addSbtPlugin("org.portable-scala" % "sbt-crossproject"              % "0.6.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "0.6.0")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "0.6.0")
addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % "0.3.8")
addSbtPlugin("com.47deg"          % "sbt-microsites"                % "0.7.18")

scalacOptions += "-deprecation"
