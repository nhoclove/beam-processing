# beam-processing

## Description

Implement a pipeline to fetch top 1k Javascript repositories ranked by stars from github APIv3.
Then calculate the health score of each repository with the pre-defined formula:

```
    health_score = (num_stars/max_num_stars) * (num_folks/max_num_folks) *
                   (commits_per_day/max_commits_per_day) * (num_opened_issues/max_num_opened_issues)
```
Then save the repositories ranked by healthiest to lease health in SCV format as bellow:

```
repo_id,repo_name,health_score,num_stars,num_folks,created_at,avg_commits_per_day,avg_time_first_response_to_issues,avg_time_opened_issues,num_maintainers,avg_time_merged_pull_request,ratio_closed_open_issues,num_people_open_issues,ratio_commit_per_devs
2126244,bootstrap,0.7,134041,65691,2011-07-29T21:19:00Z,37.84251,72.8793,93.42848,89,73.0398,0.9817419,66,53
10270250,react,0.3,131107,24150,2013-05-24T16:15:54Z,56.24272,24.065191,13.832375,28,16.782827,0.9284099,49,70
...
```

## Dependencies
1. [beam-sdks-java-core 2.13.0](https://mvnrepository.com/artifact/org.apache.beam/beam-sdks-java-core/2.13.0)
2. [beam-runners-direct-java 2.13.0](https://mvnrepository.com/artifact/org.apache.beam/beam-runners-direct-java/2.13.0)
3. [google-http-client 1.30.1](https://mvnrepository.com/artifact/com.google.http-client/google-http-client/1.30.1)
4. [gson 2.8.4](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.4)
5. [log4j 1.7.25](https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12/1.7.25)

## Build

`$ mvn clean package`

## Configuration
Due to the [rate-limit](https://developer.github.com/v3/#rate-limiting) of Github API v3, we need `Personal access tokens` 
from Github to bypass this
1. Generate a new token [here](https://github.com/settings/tokens).
2. Set the newly generated token in `config.properties` file under section `github.token`.

## Run
### 1. Run unit test
    `$ mvn test`
### 2. Run locally
For testing and development purposes, It can run on local direct runner with the following command:

    `$ mvn clean package exec:java -Dexec.mainClass=com.nvbac.beam.Application -Pdirect-runner -Dexec.args="--runner=DirectRunner" -DskipTests`
### 3. Run on other runners
   Not tested on Spark, Flink, Apex runner yet.
   
## Technical decisions
1. Apache Beam:
    - Provides an advanced unified programming model.
    - The pipelines can execute on multiple execution engines such as: Apex, Flink, Spark, samza... 
    - Support both batch and streaming processing.
    - Provides a rich APIs and Interfaces for pipeline implementations.
2. Google HTTP Client Library For Java:
    - Flexible, efficient and powerful.
    - Pluggable HTTP transport abstraction that allows you to use any low-level library such as java.net.HttpURLConnection, Apache HTTP Client, or URL Fetch on Google App Engine.
    - Efficient JSON and XML data models for parsing and serialization of HTTP response and request content. The JSON and XML libraries are also fully pluggable, and they include support for Jackson and Android's GSON libraries for JSON.
3. Gson:
    - Provide simple `toJson()` and `fromJson()` methods to convert Java objects to JSON and vice-versa.
    - Allow custom representations for objects.
3. Log4j:
    - High performance especially in multi-threaded application.
    - Easy to configure.

### Future Improvements
1. Should implement as a general library by exposing interfaces: `ISink`, `ITransform`, `ISource` where devs only need to implement their own sinks, sources, transforms.
And when starting a pipeline the Java Runtime will pick the sources, sinks, transforms available in the classpath.
2. Handle exceptions more gracefully.