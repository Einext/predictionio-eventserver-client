name := "PowerPlanEventServerClient"
version := "1.0"
scalaVersion := "2.10.6"

mainClass in Compile := Some("PowerEventIngester")


libraryDependencies ++= Seq(
    "io.prediction" 			% "client" 		% "0.9.5",
    "joda-time" 			% "joda-time" 		% "2.9.6",
    "org.joda" 				% "joda-convert" 	% "1.7"
)
