ecs-zimbra-store-manager
===
Zimbra storage manager plugin for ECS S3

Compiling instructions
---
1. Install Gradle and Maven.
2. Build and install Zimbra so you have the zimbra-store JAR in your local maven repository: http://wiki.zimbra.com/wiki/Building_Zimbra_using_Git.
If you can't get the code for the Zimbra version you want, you can download the rpm and pull the files from there. You will need the following jars to build,
all of which will be provided by the zimbra server at runtime:
  - zimbracommon
  - zimbracore
  - zimbrasoap
  - zimbrastore
  - mail
  - dom4j
  - log4j
3. Go to sources root
4. Run from command line: ./gradlew

How to configure the library from sources
---
1. Change configuration of: sources-folder/src/main/resources/ecs.properties
2. Compile

How to configure compiled library
---
1. Change configuration of: sources-folder/src/main/resources/ecs.properties
2. Open compiled ecs-zimbra-store-manager-VERSION.jar with any Zip archive program.
3. Replace configured ecs.properties file inside the root of opened Jar using Zip archive program.

How to install Zimbra StoreManager Extension plugin to Zimbra Server
---
1. Create folder /opt/zimbra/lib/ext/ecs-zimbra-store-manager
2. Copy ecsstoremanager-VERSION.jar to /opt/zimbra/lib/ext/ecs-zimbra-store-manager
3. Switch to zimbra user: sudo -su zimbra
4. Change default zimbra StoreManager: /opt/zimbra/bin/zmlocalconfig -e zimbra_class_store=com.emc.ecs.zimbra.integration.EcsStoreManager
5. Restart Zimbra Server

To restart the server
---
1. /opt/zimbra/bin/zmcontrol stop
2. shutdown -r 0
3. Wait until server restarts
4. Check if Zimbra started: /opt/zimbra/bin/zmcontrol status 
5. If haven't started, run: /opt/zimbra/bin/zmcontrol start.

**NOTE:**
This plugin will not migrate any existing accounts and account data to ECS store. In order to migrate
that data manual migration must be performed!

Developer Info
---Documentation on how the StoreManagerSDK works is here: http://wiki.zimbra.com/wiki/StoreManagerSDK

