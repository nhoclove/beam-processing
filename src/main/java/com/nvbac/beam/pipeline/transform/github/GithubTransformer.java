package com.nvbac.beam.pipeline.transform.github;

import com.nvbac.beam.pipeline.source.github.GithubRepo;
import com.nvbac.beam.pipeline.transform.ITransformer;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;

import java.util.List;

public class GithubTransformer implements ITransformer<PCollection<GithubRepo>, PCollection<List<GithubRepo>>> {

    private CombineTransformer combineTransformer;

    public GithubTransformer() {
        this.combineTransformer = new CombineTransformer();
    }

    @Override
    public PTransform<PCollection<GithubRepo>, PCollection<List<GithubRepo>>> transform() {
        return this.combineTransformer;
    }
}