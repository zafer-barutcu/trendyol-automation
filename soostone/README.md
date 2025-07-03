# Soostone QA Assignment Fresh Install
1.Install IntelliJ Community Edition. You can purchase the full version, but the Community Edition is free (including commercial development), 
and none of the additional functionality is a hard requirement.
2.Clone the project somewhere. Assuming you already have your git set up, `mkdir -p ~/src/soostone`, then `git clone git@github.com:zafer-barutcu/trendyol-automation.git ~/src/soostone
3.In IntelliJ, open the project. Import maven dependencies
4.In the top right corner, you should see a "Select SDK" button. Click this, select "Download SDK", version 17, Azul Zulu Community.
5.If you get a message about shared indices, say "Always Download"
6.At this point, things should at least be able to compile and run (not necessarily successfully).

## Running locally
To run locally and generate extend-HTML reports, you can either use maven goal *verify* or *test* with optional *clean* command. Clean command removes target folder of previous build(s).
Or use run button in TestRunner class or in feature file  HTML reports should be generated under reports/extend-report.html
```
mvn clean verify
or
mvn clean test
```
## Browsers
You can define the browser either in ConfigReader file or using command line argument BROWSER
```
mvn test -DBROWSER=firefox

```
## Tags
You can pass a custom tag using terminal. Only available tag is **@productsearch**. You can add additional tags for additional tests based on your needs

```
mvn test -Dcucumber.filter.tags="@productsearch"
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
6.You should see
7.You can watch the execution by clicking camera icon.
8.If password prompted **password=secret**

## Headless Mode in Docker
Even if you do not configure headless in the config.properties file, 
it will be overridden by the Docker container since the following Maven command is automatically executed inside the container:
```
mvn clean test -Dheadless=true -Dbrowser=chrome
```
Build Docker image
```
docker build -t trendyol -f soostone/Dockerfile .
```
Run the Test:
```
docker run --rm trendyol
```
Test reports are generated inside the container under /app/target
To copy them to your host machine:
docker run --rm -v $(pwd)/test-reports:/app/target my-selenium-tests
