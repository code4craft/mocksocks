#!/bin/sh
mvn clean package
cp mocksocks-client/target/mocksocks-client*.jar bin/client.jar
cp mocksocks-gui/target/mocksocks-gui*.jar bin/mocksocks.jar
rsync -avz --delete mocksocks-gui/target/lib/ bin/lib/
