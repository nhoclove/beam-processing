package com.nvbac.beam.pipeline.source.github;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class GithubUrl extends GenericUrl {

    public GithubUrl(String encodedUrl) {
        super(encodedUrl);
    }

    @Key("per_page")
    private int perPage;

    @Key
    private int page;

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}