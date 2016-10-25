ecs-zimbra-store-manager
===
Zimbra storage manager plugin for ECS S3

Compiling instructions
---
1. Install Gradle and Maven.
2. Build and install Zimbra so you have the zimbra jars in your local maven repository: http://wiki.zimbra.com/wiki/Building_Zimbra_using_Git.
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
2. Compile.

How to configure compiled library
---
1. Open a compiled ecs-zimbra-store-manager-VERSION.jar with any jar or zip archive program.
2. Edit the file ecs.properties (at the root of the jar) to change the configuration.
3. Replace the reconfigured ecs.properties file inside the opened jar using the archive program.

How to install Zimbra StoreManager Extension plugin to Zimbra Server
---
1. Create the folder /opt/zimbra/lib/ext/ecs-zimbra-store-manager
2. Copy ecsstoremanager-VERSION.jar to /opt/zimbra/lib/ext/ecs-zimbra-store-manager
3. Switch to zimbra user: sudo -su zimbra
4. Change default zimbra StoreManager: /opt/zimbra/bin/zmlocalconfig -e zimbra_class_store=com.emc.ecs.zimbra.integration.EcsStoreManager
5. Restart Zimbra Server

To restart the server
---
1. Stop Zimbra: /opt/zimbra/bin/zmcontrol stop
2. Check tha Zimbra has stopped: /opt/zimbra/bin/zmcontrol status 
3. Start Zimbra: /opt/zimbra/bin/zmcontrol start.
4. Alternative one-step restart: /opt/zimbra/bin/zmcontrol restart.

**NOTE:**
This plugin will not migrate any existing accounts and account data to ECS store. In order to migrate
that data manual migration must be performed!

Developer Info
---Documentation on how the StoreManagerSDK works is here: http://wiki.zimbra.com/wiki/StoreManagerSDK

