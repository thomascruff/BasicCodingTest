# BasicCodingTest
Coding test for interesting stuff

To make this code work, you need a "credentials.json" file in the directory you run it from.  
This file contains your Google service account credentials for using BigQuery

Building the code:
./mvnw clean  package

Executing the code:
java -jar ./unemployment/target/unemployment.jar

Output
In the local directory the data retrieved is saved as "UnemploymentStats.csv"
