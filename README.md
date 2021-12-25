# solr-search

1)install eclipse and tomcat in your computer
2)install spring boot framework in eclipse
3)install npm
4)to upload data to solr, please, download the csv file in this respository and run the ingestion script in python
5)open the initial project with eclipse
6)run the RestService application as spring boot application and now you can run the test with postman and the simple UI
7)to run the UI, go to the directory with the package.json and run ng serve

Examples: 

http://localhost:8080/titles?title=ing&start=5
This url to search all titles with "ing" and the "start" parameter in case you need to search in different pages

http://localhost:8080/count?firstParamater=6&secondParameter=8&start=0
This url to get the amount of movies per rate and the start parameter to create pagination

http://localhost:8080/groupTitles?firstParamater=6&secondParameter=8&genre=Drama&start=20
And this one to search movies according to specific genre and create pagination

