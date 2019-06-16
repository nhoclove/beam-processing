package com.nvbac.beam.pipeline.transform.github;

import com.nvbac.beam.pipeline.source.github.GithubRepo;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class HealthCalculatorFn extends DoFn<KV<String, Iterable<GithubRepo>>, List<GithubRepo>> {

        @ProcessElement
        public void processElement(ProcessContext context) {
            Iterable<GithubRepo> repos = context.element().getValue();
            // Calculate health score
            Stream<GithubRepo> repoStream = processHealthScore(StreamSupport.stream(repos.spliterator(), true));

            // Sort by health score
            Stream<GithubRepo> sortedStream = repoStream
                    .sorted(Comparator.comparing(GithubRepo::getHealthScore).reversed());

            context.output(sortedStream.collect(Collectors.toList()));
        }

        /**
         * Currently the formula for health_score as follows:
         * heath_score = (num_stars/max_num_stars) * (num_folks/max_num_folks) *
         *               (commits_per_day/max_commits_per_day) * (num_opened_issues/max_num_opened_issues)
         *
         * @param repos A stream of GithubRepos.
         * @return A stream of GithubRepo with health_score field added.
         */
        private Stream<GithubRepo> processHealthScore(Stream<GithubRepo> repos) {
            int maxStar = Integer.MIN_VALUE;
            int maxFolk = Integer.MIN_VALUE;
            float maxCommitsPerDay = Float.MIN_VALUE;
            float maxTimeOpenedIssues = Float.MIN_VALUE;

            // It would be more efficient if finding 4 max values at once instead of finding each individually
            LinkedList<GithubRepo> listRepos = repos.collect(Collectors.toCollection(LinkedList::new));
            for (GithubRepo repo : listRepos) {
                if (repo.getStars() > maxStar) maxStar = repo.getStars();
                if (repo.getForks() > maxFolk) maxFolk = repo.getForks();
                if (repo.getAvgCommitPerDay() > maxCommitsPerDay) maxCommitsPerDay = repo.getAvgCommitPerDay();
                if (repo.getAvgTimeOpenedIssues() > maxTimeOpenedIssues) maxTimeOpenedIssues = repo.getAvgTimeOpenedIssues();
            }
            // Add healthScore field
            return addHealthScoreField(listRepos, maxStar, maxFolk, maxCommitsPerDay, maxTimeOpenedIssues);
        }

        private Stream<GithubRepo> addHealthScoreField(List<GithubRepo> repos, final int maxStar, final int maxFolk,
                                                  final float maxCommitsPerDay, final float maxTimeOpenedIssues) {
            return repos.parallelStream().map(repo -> {
                double healthScore = this.calculateHealthScore(repo, maxStar, maxFolk, maxCommitsPerDay, maxTimeOpenedIssues);
                GithubRepo nRepo = new GithubRepo(repo);
                nRepo.setHealthScore(healthScore);
                return nRepo;
            });
        }

        private double calculateHealthScore(GithubRepo githubRepo, int maxStar, int maxFolk,
                                     float maxCommitsPerDay, float maxTimeOpenedIssues) {
            return (githubRepo.getStars() / (double)maxStar) * (githubRepo.getForks() / (double)maxFolk) *
                    (githubRepo.getAvgCommitPerDay() / maxCommitsPerDay) * (githubRepo.getAvgTimeOpenedIssues() / maxTimeOpenedIssues);
        }
}
