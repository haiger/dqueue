#!/bin/bash 

bin_path=`pwd`
cd ${bin_path}/..
base=`pwd`
cd $bin_path
dqueue_conf=$base/conf/dqueue.properties
logback_configurationFile=$base/conf/logback.xml

if [ -f $base/bin/dqueue.pid ] ; then
	echo "found dqueue.pid , Please run stop.sh first ,then startup.sh" 2>&2
    exit 1
fi

if [ ! -d $base/logs ] ; then 
	mkdir -p $base/logs
fi

## set java path
if [ -z "$JAVA" ] ; then
  JAVA=$(which java)
fi

if [ -z "$JAVA" ]; then
  	echo "Cannot find a Java JDK. Please set either set JAVA or put java (>=1.7) in your PATH." 2>&2
    exit 1
fi

case "$#" 
in
0 ) 
	;;
1 )	
	var=$*
	if [ -f $var ] ; then 
		dqueue_conf=$var
	else
		echo "THE PARAMETER IS NOT CORRECT.PLEASE CHECK AGAIN."
        exit
	fi;;
2 )	
	var=$1
	if [ -f $var ] ; then
		dqueue_conf=$var
	else 
		if [ "$1" = "debug" ]; then
			DEBUG_PORT=$2
			DEBUG_SUSPEND="n"
			JAVA_DEBUG_OPT="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=$DEBUG_SUSPEND"
		fi
     fi;;
* )
	echo "THE PARAMETERS MUST BE TWO OR LESS.PLEASE CHECK AGAIN."
	exit;;
esac

str=`file $JAVA_HOME/bin/java | grep 64-bit`
if [ -n "$str" ]; then
	JAVA_VERSION=`java -version 2>&1 |awk 'NR==1{ gsub(/"/,""); print $3 }'`
	if [[ "$JAVA_VERSION" > "1.7" ]]; then
		JAVA_OPTS="-server -Xms1024m -Xmx1024m -Xss256k -XX:SurvivorRatio=2 -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+HeapDumpOnOutOfMemoryError"
	else
		JAVA_OPTS="-server -Xms1024m -Xmx1024m -Xmn512m -XX:SurvivorRatio=2 -XX:PermSize=96m -XX:MaxPermSize=256m -Xss256k -XX:-UseAdaptiveSizePolicy -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError"
	fi
else
	JAVA_OPTS="-server -Xms1024m -Xmx1024m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:MaxPermSize=128m "
fi

JAVA_OPTS=" $JAVA_OPTS -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"
DELAY_OPTS="-Dlogback.configurationFile=$logback_configurationFile -Ddqueue.conf=$dqueue_conf -DappName=dqueue-server"

if [ -e $dqueue_conf -a -e $logback_configurationFile ]
then

    	MAIN_CLASS="com.github.haier.dqueue.server.DQueueLauncher"
    	for i in $base/lib/*;
    		do CLASSPATH=$i:"$CLASSPATH";
    	done
    	CLASSPATH="$base/conf:$CLASSPATH";

        echo "cd to $bin_path for workaround relative path"
        cd $bin_path

        echo LOG CONFIGURATION : $logback_configurationFile
        echo delay conf : $dqueue_conf
        echo CLASSPATH :$CLASSPATH
        $JAVA $JAVA_OPTS $JAVA_DEBUG_OPT $DELAY_OPTS -classpath $CLASSPATH $MAIN_CLASS 1>>$base/logs/dqueue.log 2>&1 &
        echo $! > $base/bin/dqueue.pid

        echo "cd to $bin_path for continue"
        cd $bin_path
else
        echo "dqueue conf("$dqueue_conf") OR log configration file($logback_configurationFile) is not exist,please create then first!"
fi