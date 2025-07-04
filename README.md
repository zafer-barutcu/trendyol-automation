# Soostone QA Assignment - Trendyol Automation

This project contains automated test cases for Trendyol's web application, built with Java, Selenium, Maven, and Cucumber.

# Fresh Install
1.Install IntelliJ Community Edition. You can purchase the full version, but the Community Edition is free (including commercial development), 
and none of the additional functionality is a hard requirement.

2.Clone the project somewhere. Assuming you already have your git set up, `mkdir -p ~/src/soostone`, then `git clone https://github.com/zafer-barutcu/trendyol-automation.git ~/src/soostone

3.In IntelliJ, open the project. Import maven dependencies

4.In the top right corner, you should see a "Select SDK" button. Click this, select "Download SDK", version 17, Azul Zulu Community.

5.If you get a message about shared indices, say "Always Download"

6.At this point, things should at least be able to compile and run (not necessarily successfully).

## Running locally
To run tests locally and generate Extent HTML reports, use Maven commands:
```
mvn clean verify
or
mvn clean test
```
Reports will be generated at : reports/extent-report.html

Alternatively, you can run tests via IntelliJ:
Click the Run button in TestRunner class or directly from feature files.

## Browser Configuration
Set browser type in the config.properties file:
```
browser=chrome
```

## Running Tests Concurrently
Enable parallel test execution in pom.xml:

```
<parallel>methods</parallel>
<threadCount>4</threadCount>
```

## Selenium Grid
In order to simulate Selenium Grid infrastructure in local, Docker desktop should be installed in local

1.Do **remote=true** in config.properties

2.Open Docker Desktop

3.Open terminal and execute command in **grid-command** file
```
docker run -d --name selenium-grid -p 4444:4444 selenium/standalone-chrome
```
4.Check container is up and running

4.Run the test

5.Open http://localhost:4444/ui

6.You can watch the execution by clicking camera icon.

7.If password prompted **password=secret**

