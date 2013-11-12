
java -classpath bin:conf:/usr/share/java/slf4j/slf4j-api.jar:/usr/share/java/slf4j/slf4j-simple.jar  se.krats.explorelogs.LogTimer slf4j

java -classpath bin:conf:/usr/share/java/log4j.jar  se.krats.explorelogs.LogTimer my

java -classpath bin:conf:/usr/share/java/log4j.jar  se.krats.explorelogs.LogTimer log4j

java -classpath bin:conf:/usr/share/java/commons-logging.jar:/usr/share/java/log4j.jar se.krats.explorelogs.LogTimer commons

java -classpath bin:conf  se.krats.explorelogs.LogTimer jul

java -classpath bin:conf:/home/kaj/play/play-1.2.7/framework/play-1.2.7.jar:/usr/share/java/log4j.jar  se.krats.explorelogs.LogTimer play

