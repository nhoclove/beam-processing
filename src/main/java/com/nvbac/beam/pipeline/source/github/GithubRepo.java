package com.nvbac.beam.pipeline.source.github;

import com.google.api.client.util.Key;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
public class GithubRepo {

    @Key
    private long id;

    @Key
    private String name;

    @Key("full_name")
    private String fullName;

    @Key("html_url")
    private String htmlUrl;

    @Key("stargazers_count")
    private int stars;

    @Key("forks_count")
    private int forks;

    @Key("created_at")
    private String createdAt;

    private double healthScore;

    private float avgCommitPerDay;

    private float avgTimeFirstResponseToIssues;

    private float avgTimeOpenedIssues;

    private int numMaintainers;

    private float avgTimeMergedPullRequests;

    private float ratioClosedOpenIssues;

    private int numPeopleOpeningIssues;

    private int ratioCommitPerDevs;

    private int numberOpenPullRequests;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public int getStars() { return stars; }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(double healthScore) {
        this.healthScore = healthScore;
    }

    public float getAvgCommitPerDay() { return avgCommitPerDay; }

    public void setAvgCommitPerDay(float avgCommitPerDay) {
        this.avgCommitPerDay = avgCommitPerDay;
    }

    public float getAvgTimeFirstResponseToIssues() {
        return avgTimeFirstResponseToIssues;
    }

    public void setAvgTimeFirstResponseToIssues(float avgTimeFirstResponseToIssues) {
        this.avgTimeFirstResponseToIssues = avgTimeFirstResponseToIssues;
    }

    public float getAvgTimeOpenedIssues() {
        return avgTimeOpenedIssues;
    }

    public void setAvgTimeOpenedIssues(float avgTimeOpenedIssues) {
        this.avgTimeOpenedIssues = avgTimeOpenedIssues;
    }

    public int getNumMaintainers() {
        return numMaintainers;
    }

    public void setNumMaintainers(int numMaintainers) {
        this.numMaintainers = numMaintainers;
    }

    public float getAvgTimeMergedPullRequests() {
        return avgTimeMergedPullRequests;
    }

    public void setAvgTimeMergedPullRequests(float avgTimeMergedPullRequests) {
        this.avgTimeMergedPullRequests = avgTimeMergedPullRequests;
    }

    public float getRatioClosedOpenIssues() {
        return ratioClosedOpenIssues;
    }

    public void setRatioClosedOpenIssues(float ratioClosedOpenIssues) {
        this.ratioClosedOpenIssues = ratioClosedOpenIssues;
    }

    public int getNumPeopleOpeningIssues() {
        return numPeopleOpeningIssues;
    }

    public void setNumPeopleOpeningIssues(int numPeopleOpeningIssues) {
        this.numPeopleOpeningIssues = numPeopleOpeningIssues;
    }

    public int getRatioCommitPerDevs() {
        return ratioCommitPerDevs;
    }

    public void setRatioCommitPerDevs(int ratioCommitPerDevs) {
        this.ratioCommitPerDevs = ratioCommitPerDevs;
    }

    public int getNumberOpenPullRequests() {
        return numberOpenPullRequests;
    }

    public void setNumberOpenPullRequests(int numberOpenPullRequests) {
        this.numberOpenPullRequests = numberOpenPullRequests;
    }

    public GithubRepo() {}

    public GithubRepo(GithubRepo repo) {
        this.id = repo.getId();
        this.name = repo.getName();
        this.fullName = repo.getFullName();
        this.htmlUrl = repo.getHtmlUrl();
        this.createdAt = repo.getCreatedAt();
        this.healthScore = repo.getHealthScore();
        this.stars = repo.getStars();
        this.forks = repo.getForks();
        this.avgCommitPerDay = repo.getAvgCommitPerDay();
        this.avgTimeFirstResponseToIssues = repo.getAvgTimeFirstResponseToIssues();
        this.avgTimeOpenedIssues = repo.avgTimeOpenedIssues;
        this.numMaintainers = repo.getNumMaintainers();
        this.avgTimeMergedPullRequests = repo.avgTimeMergedPullRequests;
        this.numPeopleOpeningIssues = repo.getNumPeopleOpeningIssues();
        this.ratioClosedOpenIssues = repo.ratioClosedOpenIssues;
        this.ratioCommitPerDevs = repo.getRatioCommitPerDevs();
        this.numberOpenPullRequests = repo.getNumberOpenPullRequests();
    }

    public GithubRepo(long id, String name, String fullName, String htmlUrl, String createdAt, float heathScore, int stars,
                      int folks, float avgCommitPerDay, float avgTimeFirstResponseToIssues, float avgTimeOpenedIssues,
                      int numMaintainers, float avgTimeMergedPullRequests, int numPeopleOpeningIssues, float ratioClosedOpenIssues,
                      int ratioCommitPerDevs, int numberOpenPullRequests) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.htmlUrl = htmlUrl;
        this.createdAt = createdAt;
        this.healthScore = heathScore;
        this.stars = stars;
        this.forks = folks;
        this.avgCommitPerDay = avgCommitPerDay;
        this.avgTimeFirstResponseToIssues = avgTimeFirstResponseToIssues;
        this.avgTimeOpenedIssues = avgTimeOpenedIssues;
        this.numMaintainers = numMaintainers;
        this.avgTimeMergedPullRequests = avgTimeMergedPullRequests;
        this.numPeopleOpeningIssues = numPeopleOpeningIssues;
        this.ratioClosedOpenIssues = ratioClosedOpenIssues;
        this.ratioCommitPerDevs = ratioCommitPerDevs;
        this.numberOpenPullRequests = numberOpenPullRequests;
    }

    @Override
    public String toString() {
        return "GithubRepo {" + "id=" + id + ", name=" + name + ", fullName= " + fullName
                + ", htmlUrl=" + htmlUrl + ", createdAt=" + createdAt + ", healthScore=" + healthScore
                + ", stars=" + stars + ", forks=" + forks + ", avgCommitPerDay=" + avgCommitPerDay
                + ", avgTimeFirstResponseToIssue=" + avgTimeFirstResponseToIssues + ", avgTimeOpenedIssues=" + avgTimeOpenedIssues
                + ", numMaintainers=" + numMaintainers + ", avgTimeMergedPullRequests=" + avgTimeMergedPullRequests
                + ", numPeopleOpeningIssues=" + numPeopleOpeningIssues + ", ratioClosedOpenIssue="+ ratioClosedOpenIssues
                + ", ratioCommitPerDevs=" + ratioCommitPerDevs + ", openPullRequest=" + numberOpenPullRequests + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        GithubRepo repo = (GithubRepo) obj;
        return id == repo.id
                && (name != null && name.equals(repo.getName())
                || (fullName.equals(repo.getFullName()))
                && (healthScore == repo.getHealthScore()));
    }
}
