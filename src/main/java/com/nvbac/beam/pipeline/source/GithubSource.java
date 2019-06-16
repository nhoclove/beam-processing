package com.nvbac.beam.pipeline.source;

import com.nvbac.beam.conf.AppConf;
import com.nvbac.beam.pipeline.source.github.GithubClient;
import com.nvbac.beam.pipeline.source.github.GithubRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class GithubSource implements ISource<GithubRepo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GithubSource.class);

    private GithubClient githubClient;
    private AppConf appConf;

    public GithubSource(AppConf appConf) {
        this.appConf = appConf;
    }

    @Override
    public void start() {
        LOGGER.info("Starting Github source");
        githubClient = new GithubClient(this.appConf);
    }

    @Override
    public List<GithubRepo> poll() {
        LOGGER.info("Polling records from Github source");
        return this.githubClient.fetchJsRepoRankedByStars();
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping Github source");
        this.githubClient.close();
    }
}
