package com.nvbac.beam.pipeline.transforms.github;

import com.nvbac.beam.pipeline.source.github.GithubRepo;
import com.nvbac.beam.pipeline.transform.github.HealthCalculatorFn;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class HealthCalculatorFnTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testDoFn_calculateHealthScore() throws Exception {
        GithubRepo repo1 = new GithubRepo(1L, "name_1", "fullname_1", "htmlUrl_1", "",
                0.0F, 2, 10, 10.0F, 10.0F,
                10.0F, 10, 10.0F, 10,
                10, 10, 10);
        GithubRepo repo2 = new GithubRepo(2L, "name_2", "fullname_2", "htmlUrl_2", "",
                0.0F, 4, 10, 10.0F, 10.0F,
                10.0F, 10, 10.0F, 10,
                10, 10, 10);
        GithubRepo repo3 = new GithubRepo(3L, "name_3", "fullname_3", "htmlUrl_3", "",
                0.0F, 8, 10, 10.0F, 10.0F,
                10.0F, 10, 10.0F, 10,
                10, 10, 10);

        KV<String, Iterable<GithubRepo>> testInput = KV.of("string", Arrays.asList(repo1, repo2, repo3));
        final Double[] expected = new Double[] { 1.0, 0.5, 0.25 };

        HealthCalculatorFn healthCalculatorFn = new HealthCalculatorFn();
        DoFnTester<KV<String, Iterable<GithubRepo>>, List<GithubRepo>> fnTester = DoFnTester.of(healthCalculatorFn);

        List<List<GithubRepo>> testOutput = fnTester.processBundle(testInput);
        Double[] actual = testOutput.get(0).stream().map(GithubRepo::getHealthScore).toArray(Double[]::new);

        Assert.assertArrayEquals(actual, expected);
    }
}
