package com.nvbac.beam.pipeline.transform.github;

import com.nvbac.beam.pipeline.source.github.GithubRepo;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import java.util.List;

public class CombineTransformer extends PTransform<PCollection<GithubRepo>, PCollection<List<GithubRepo>>> {

    @Override
    public PCollection<List<GithubRepo>> expand(PCollection<GithubRepo> input) {
        return input.apply(ParDo.of(new KVInverterFn()))
                .apply(GroupByKey.create())
                .apply(ParDo.of(new HealthCalculatorFn()));
    }
}
