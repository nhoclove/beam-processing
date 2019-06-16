package com.nvbac.beam.pipeline.source.github;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nvbac.beam.conf.AppConf;
import com.nvbac.beam.conf.ConfigKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GithubClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubClient.class);

    private static final int DEFAULT_PAGES = 10;
    private static final int PER_PAGE = 100;
    private static final double DOUBLE_RANDOM_MIN = 10.0;
    private static final double DOUBLE_RANDOM_MAX = 100.0;
    private static final int INT_RANDOM_MIN = 10;
    private static final int INT_RANDOM_MAX = 100;

    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final String GITHUB_URI_PREFIX = "https://api.github.com/";
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private final static HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(
            (HttpRequest request) -> {
                request.setParser(new JsonObjectParser(JSON_FACTORY));
            });

    private HttpHeaders headers;

    public GithubClient(AppConf appConf) {
        this.headers = new HttpHeaders();
        String token = appConf.get(ConfigKeys.GITHUB_TOKEN, null);
        if (token != null) {
            headers.setAuthorization("token " + token);
        } else {
            LOGGER.warn("No github token provided. Falling to default authorization");
        }
    }

    public List<GithubRepo> fetchJsRepoRankedByStars() {
        LOGGER.info("Fetching Javascript repositories ranked by stars");
        LocalDate now = LocalDate.now();
        LinkedList<GithubRepo> repos = new LinkedList<>();

        for(int i = 0; i < DEFAULT_PAGES; i++) {
            try {
                GithubUrl url = new GithubUrl(String.format(GITHUB_URI_PREFIX + "search/repositories?q=language:javascript&sort=stars&order=desc&page=%d", i + 1));
                url.setPerPage(PER_PAGE);

                HttpRequest request = requestFactory.buildGetRequest(url);
                request.setHeaders(this.headers);

                GithubSearchResponse repo = request
                        .execute()
                        .parseAs(GithubSearchResponse.class);

                repo.getItems().forEach(rp -> {
                    GithubRepo nRepo = this.reconstructGithubRepo(rp, now);
                    repos.add(nRepo);
                });

                // Cleanup
                url.clear();
                repo.clear();
            } catch (Exception e) {
                LOGGER.error("Failed to fetch repositories. Exception follows. ", e);
                break;
            }
        }
        return repos;
    }

    public void close() {
        LOGGER.info("Closing Github client");
        try {
            HTTP_TRANSPORT.shutdown();
        } catch (IOException e) {
            LOGGER.error("Failed to shutdown http transport. Exception follows. ", e);
        }
    }

    // Construct a GithubRepo
    private GithubRepo reconstructGithubRepo(GithubRepo repo, LocalDate now) {
        /* LocalDate createdTime = LocalDateTime
                .ofInstant(Instant.parse(repo.getCreatedAt()), ZoneId.of(ZoneOffset.UTC.getId()))
                .toLocalDate();
        repo.setAvgCommitPerDay(calcNumberOfCommitsPerDay(repo.getFullName(), createdTime, now)); */
        // Add number of commits per day
        repo.setAvgCommitPerDay((float)this.randomDoubleBetweenRange(DOUBLE_RANDOM_MIN, DOUBLE_RANDOM_MAX));
        // Add average time first response to issues
        repo.setAvgTimeFirstResponseToIssues((float)this.randomDoubleBetweenRange(DOUBLE_RANDOM_MIN, DOUBLE_RANDOM_MAX));
        // Add average time opened issues
        repo.setAvgTimeOpenedIssues((float)this.randomDoubleBetweenRange(DOUBLE_RANDOM_MIN, DOUBLE_RANDOM_MAX));
        // Add number of maintainers
        repo.setNumMaintainers(this.randomIntegerBetweenRange(INT_RANDOM_MIN, INT_RANDOM_MAX));
        // Add average time merged pull requests
        repo.setAvgTimeMergedPullRequests((float)this.randomDoubleBetweenRange(DOUBLE_RANDOM_MIN, DOUBLE_RANDOM_MAX));
        // Add ratio of closed and open issues
        repo.setRatioClosedOpenIssues(calcRatioClosedOpenIssues(repo.getFullName()));
        // Add people opening issues
        repo.setNumPeopleOpeningIssues(this.randomIntegerBetweenRange(INT_RANDOM_MIN, INT_RANDOM_MAX));
        // Add ratio commits per devs
        repo.setRatioCommitPerDevs(this.randomIntegerBetweenRange(INT_RANDOM_MIN, INT_RANDOM_MAX));
        // Add number of open pull requests
        repo.setNumberOpenPullRequests(calcNumberOfOpenPullRequests(repo.getFullName()));
        return repo;
    }

    // Calculate average of commits per day
    private float calcNumberOfCommitsPerDay(String repoName, LocalDate created_at, LocalDate now) {
        GithubUrl url = new GithubUrl(String.format(GITHUB_URI_PREFIX + "repos/%s/contributors", repoName));
        url.setPerPage(PER_PAGE);
        int totalCommits = 0;

        try {
            HttpRequest request = requestFactory.buildGetRequest(url);
            request.setHeaders(this.headers);
            String json = request.execute().parseAsString();
            JsonArray contributors = JSON_PARSER.parse(json).getAsJsonArray();
            Iterator<JsonElement> iterator = contributors.iterator();

            while(iterator.hasNext()) {
                totalCommits += iterator.next().getAsJsonObject().get("contributions").getAsInt();
            }

            return totalCommits / this.calcCurrentDiff(created_at, now).getDays();
        } catch (IOException e) {
            LOGGER.error("Failed to calculate number of commits per day. Exception follows. ", e);
            return -1;
        }
    }

    // Calculate ratio of closed and open issues
    private float calcRatioClosedOpenIssues(String repoName) {
        try {
            // Get number of closed issues
            GithubUrl url = new GithubUrl(String.format(GITHUB_URI_PREFIX + "search/issues?q=repo:%s+type:issue+state:open", repoName));
            url.setPerPage(PER_PAGE);
            HttpResponse response = requestFactory.buildGetRequest(url).setHeaders(this.headers).execute();
            String openJson = response.parseAsString();
            response.disconnect();
            JsonObject openIssues = JSON_PARSER.parse(openJson).getAsJsonObject();
            int numOpenIssues = openIssues.get("total_count").getAsInt();

            // Get number of open issues
            url = new GithubUrl(String.format(GITHUB_URI_PREFIX + "search/issues?q=repo:%s+type:issue+state:closed", repoName));
            response = requestFactory.buildGetRequest(url).setHeaders(this.headers).execute();
            String closedJson = response.parseAsString();
            response.disconnect();
            JsonObject closedIssues = JSON_PARSER.parse(closedJson).getAsJsonObject();
            int numClosedIssues = closedIssues.get("total_count").getAsInt();

            return (float)numClosedIssues / (numClosedIssues + numOpenIssues);
        } catch (IOException e) {
            LOGGER.error("Failed to calculate ratio of closed to open issues. Exception follows. ", e);
            return -1.0F;
        }
    }

    // Calculate number of pull request
    private int calcNumberOfOpenPullRequests(String repoName) {
        GithubUrl url = new GithubUrl(String.format(GITHUB_URI_PREFIX + "search/issues?q=repo:%s+is:pr+is:open", repoName));
        url.setPerPage(PER_PAGE);

        try {
            HttpResponse response = requestFactory.buildGetRequest(url).setHeaders(this.headers).execute();
            String json = response.parseAsString();
            response.disconnect();
            JsonObject openPullRequests = JSON_PARSER.parse(json).getAsJsonObject();
            return openPullRequests.get("total_count").getAsInt();
        } catch (IOException e) {
            LOGGER.error("Failed to calculate the number of open pull request. Exception follows.", e);
            return -1;
        }
    }

    // Random Integer within a given range
    private int randomIntegerBetweenRange(int min, int max) {
        return (int)(Math.random()*((max-min)+1))+min;
    }

    // Random Double within a given range
    private double randomDoubleBetweenRange(double min, double max) {
        return (Math.random()*((max-min)+1))+min;
    }

    // Calculate the diff period between start and end
    private Period calcCurrentDiff(LocalDate start, LocalDate end) { return Period.between(start, end); }
}
