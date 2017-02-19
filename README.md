# ExtremeWorldServer
extreme world server

#安装一下taobaosdk到本地mvn仓库
mvn install:install-file -DgroupId=taobao-sdk -DartifactId=taobao -Dversion=1.0 -Dpackaging=jar -Dfile=./src/main/resources/lib/taobao-sdk-java-auto_1438276914491-20150812.jar

#安装一下qqConnect到本地mvn仓库
mvn install:install-file -DgroupId=com.qq.connect -DartifactId=qqconnect -Dversion=2.0 -Dpackaging=jar -Dfile=src/main/resources/lib/Sdk4J.jar
