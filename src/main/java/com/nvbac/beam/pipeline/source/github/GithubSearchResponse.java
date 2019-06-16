package com.nvbac.beam.pipeline.source.github;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import java.util.List;

public class GithubSearchResponse extends GenericJson {

    @Key
    private long total_count;

    @Key
    private boolean incomplete_results;

    @Key
    private List<GithubRepo> items;

    public long getTotal_count() {
        return total_count;
    }

    public void setTotal_count(long total_count) {
        this.total_count = total_count;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public List<GithubRepo> getItems() {
        return items;
    }

    public void setItems(List<GithubRepo> items) {
        this.items = items;
    }
}

