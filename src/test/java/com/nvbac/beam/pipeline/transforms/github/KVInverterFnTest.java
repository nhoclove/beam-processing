package com.nvbac.beam.pipeline.transforms.github;

import com.nvbac.beam.pipeline.source.github.GithubRepo;
import com.nvbac.beam.pipeline.transform.github.KVInverterFn;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class KVInverterFnTest {

    @Rule
    public final transient TestPipeline pipeline = TestPipeline.create();

    @Test
    @SuppressWarnings("unchecked")
    public void testDoFn_convertToKV() throws Exception {
        GithubRepo repo1 = new GithubRepo(1L, "name_1", "fullname_1", "htmlUrl_1", "",
                0.1F, 10, 10, 10.0F, 10.0F,
                10.0F, 10, 10.0F, 10,
                10, 10, 10);
        GithubRepo repo2 = new GithubRepo(2L, "name_2", "fullname_2", "htmlUrl_2", "",
                0.5F, 10, 10, 10.0F, 10.0F,
                10.0F, 10, 10.0F, 10,
                10, 10, 10);

        final KV<String, GithubRepo>[] expected = new KV[] {
                KV.of(GithubRepo.class.getName(), repo1),
                KV.of(GithubRepo.class.getName(), repo2)
        };

        final List<GithubRepo> repos = Arrays.asList(repo1, repo2);

        PCollection<GithubRepo> input = pipeline.apply(Create.of(repos))
                .setCoder(AvroCoder.of(GithubRepo.class));

        PCollection<KV<String, GithubRepo>> output = input.apply(ParDo.of(new KVInverterFn()));

        PAssert.that(output).containsInAnyOrder(expected);

        pipeline.run();
    }
}
