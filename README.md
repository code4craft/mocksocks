mocksocks
--------
> A socks proxy in Java. It can be used to record network traffics and replay them for tests. 

### Goals

Record network traffics and replay them for tests. Then you can run tests isolately without considering the unstable external dependencies.

### Ways

Set global socks proxy for Java.

### Planning

## Client:

-DmockFile=/Users/yihua/dp_workspace/mocksocks/mocksocks-client/target/mocksocks-client-0.0.2-SNAPSHOT.jar -javaagent:/Users/yihua/dp_workspace/mocksocks/mocksocks-client/target/mocksocks-client-0.0.2-SNAPSHOT.jar

void java.nio.channels.spi.AbstractSelectableChannel.removeKey(java.nio.channels.SelectionKey)