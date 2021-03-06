#!/bin/sh
FILE="mocksocks.tar.gz"
URL="http://code4craft.qiniudn.com/${FILE}"
LPATH="/usr/local/mocksocks"
mkdir -p ${LPATH};
curl ${URL} > ${LPATH}/${FILE}
cd ${LPATH}
tar -xzf ${FILE}
echo "Done!"
echo "Add vm parameters '-javaagent:${LPATH}/client.jar -DmockFile=${LPATH}/client.jar' to your program"
echo "try java -jar ${LPATH}/mocksocks.jar to run the GUI!"

