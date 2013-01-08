---------------------------------------------------------------------------------------------------------
HOWTO setup chemicalinventory - Claus Stie Kallesøe and Dann Vestergaard 2007
---------------------------------------------------------------------------------------------------------
Updated by Claus Stie Kallesøe, 2013, to reflect the port to postgresql
---------------------------------------------------------------------------------------------------------

The following text is supposed to be a help to install the chemicalInventory system. The setup of
PostgreSQL, JAVA and Tomcat depends on the OS and is described very well at many other sites so we
will skip that here and expect that the Tomcat servlet container, the postgreSQL Database and JAVA 
is installed and running on your system.


What is needed
---------------

The following software is needed (assuming you have hardware with an OS and some disc space)
in order to make the system work:

1) Postgresql to hold the data (Many other databases can most likely be used but in that case 
you need to edit the sql scripts to create the tables and the way you communicate with the database - another JDBC driver - to fit your type of database)

2) A postgresql jdbc driver to be able to communicate with the database from the javabeans.

3) Tomcat as servlet container (Again you might make it work with other tools but we have only
used Tomcat).

4) JAVA. You need a J2SDK in order to load the servlet container and to compile the .java
files if/when you edit them.

5) The JChem JAVA library and the Marvin applets from ChemAxon (www.chemaxon.com). The library
handles the registration of and searching in the structures and the applets gives us the
structure drawing tools. You can get a free academia license to all these tools if you ....yes
if you are academia!

The postgresql based version is developed using: Ubuntu 10.04, Tomcat 6.0.35, posgresql 9.1, java version 1.6.0_24 (OpenJDK)


Platform support
-------------------

All the above mentioned tools are available on a large number of platforms so in theory
chemicalInventory should run on all of them, but we cannot be sure. We have had the system
running on Mac OS X, Linux (Debian, RedHat, Slackware, Ubuntu) and Windows so there is a fair chance
that your OS will work as well.


What to do
-----------

1) PostgreSQL 

Install and setup your postgresql database and make sure it is working.
Create a user who will own the chemicalInventory tables. 

The default is: 

schema name = chemicals
user name = chemicals
password = chemicals

Give the user privileges to the chemicals database.

The name of the database to use, is specified in two places. - Under tomcat/conf/server.xml (see tomcat setup)
and under tomcat/webapps/chemicalinventory/WEB-INF/configuration/CIConfiguration.txt (see section 2)

------------------------------------------------------------------------------------------------------
WE RECOMMEND THAT YOU CHANGE THE DEFAULT VALUES, BUT REMEMBER TO ALSO DO IT IN THE CONFIGURATION FILES
------------------------------------------------------------------------------------------------------

Run the sql script shipped with chemicalInventory in order to setup the required tables.
The script will also setup the structure tables required by the JChem library.

A default user "ADMINISTRATOR" with password "master" is created.

------------------------


2) Tomcat

Install and setup Tomcat and make sure it is running (that you see the default Welcome page in
a webbrowser).

On Ubuntu 10.04 LTS this is what the welcome page tells me:

"... pleased to learn that this system instance of Tomcat is installed with CATALINA_HOME in /usr/share/tomcat6 and CATALINA_BASE in /var/lib/tomcat6..."

So..

CATALINA_HOME = /usr/share/tomcat6
CATALINA_BASE = /var/lib/tomcat6


Copy the chemicalinventory folder to /var/lib/tomcat6/webapps/

/where_ever_your_tomcat_is_located/webapps/chemicalinventory

Copy the content in /chemicalinventory/text/CI_context_tomcat5.txt to the server.xml (not recommended anymore!) file placed in /CATALINA_BASE/conf/server.xml.

Alter the values if your system is different from the default values. This should be placed just before the last </HOST> tag.

Modify the chemicalinventory configuration file located under 
/where_ever_your_tomcat_is_located/webapps/chemicalinventory/WEB-INF/configuration/CIconfiguration.txt
with values relevant for your setup.

---------------------------------

3) postgresql jdbc driver

A version of the relevant jdbc driver can be found here:

Tomcat/webapps/chemicalinventory/text/postgresql-9.2-1002.jdbc4.jar.

Copy to /CATALINA_HOME/lib

--------------------------------

4) JChem library and the Marvin applets

The chemicalinventory is bundled with JChem version 4.0.5 and the marvin applets. The default licence is 1 (=one) structure search pr. minute. Contact www.chemaxon.com for further licence privileges.


--------------------------------


5) You are ready to rock and roll!

All you need to do is restart the Tomcat server and point your webbrowser to

http://serverip:port/chemicalinventory

and you should see the beatifull front end of the application.


