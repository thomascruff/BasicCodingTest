# BasicCodingTest
Coding test for interesting stuff

To make this code work, you need a "credentials.json" file in the directory you run it from.  
This file contains your Google service account credentials for using BigQuery

Building the code:
./mvnw clean  package

Executing the code:
java -jar ./unemployment/target/unemployment.jar

Postman
Post request to 
http://localhost:8080/retrieveStatistics

Body of the request:
{ "apikey":"superman","year":"","period":""}
 apikey is ignored in this version
 year can be any 4 digit year
 period is any month as designated in the unemployment statistics (M01, M02, M03,...)

Output
In the local directory the data retrieved is saved as "UnemploymentStats.csv"
