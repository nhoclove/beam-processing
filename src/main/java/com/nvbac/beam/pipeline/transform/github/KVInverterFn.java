package com.nvbac.beam.pipeline.transform.github;

import com.nvbac.beam.pipeline.source.github.GithubRepo;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

public class KVInverterFn extends DoFn<GithubRepo, KV<String, GithubRepo>> {

    @ProcessElement
    public void processElement(ProcessContext context) {
        GithubRepo repo = context.element();
        context.output(KV.of(GithubRepo.class.getName(), repo));
    }
}
