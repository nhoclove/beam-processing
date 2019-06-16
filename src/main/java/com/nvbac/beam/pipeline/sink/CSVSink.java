package com.nvbac.beam.pipeline.sink;

import com.google.common.base.Joiner;
import com.nvbac.beam.pipeline.source.github.GithubRepo;
import org.apache.beam.sdk.io.FileIO.Sink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVSink implements Sink<List<GithubRepo>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVSink.class);

    private PrintWriter writer;

    @Override
    public void open(WritableByteChannel channel) throws IOException {
        this.writer = new PrintWriter(Channels.newOutputStream(channel));
    }

    @Override
    public void write(List<GithubRepo> element) throws IOException {
        String header = Joiner.on(",").join(this.getGithubRepoFields());
        LOGGER.debug("Writing to file with headers: {}", header);
        this.writer.println(header);
        element.forEach(repo -> {
            writer.println(Joiner.on(",").join(Arrays.asList(repo.getId(), repo.getName(), repo.getHealthScore(),
                    repo.getStars(), repo.getForks(), repo.getCreatedAt(), repo.getAvgCommitPerDay(),
                    repo.getAvgTimeFirstResponseToIssues(), repo.getAvgTimeOpenedIssues(), repo.getNumMaintainers(),
                    repo.getAvgTimeMergedPullRequests(), repo.getRatioClosedOpenIssues(), repo.getNumPeopleOpeningIssues(),
                    repo.getRatioCommitPerDevs())));
        });
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    /**
     * Should use java reflection for getting declared fields.
     * But now we need to write to file in defined order so hardcode headers here is acceptable.
     *
     * @return List of GithubRepo field names in defined order.
     */
    private List<String> getGithubRepoFields() {
        return Arrays.asList("repo_id", "repo_name", "health_score", "num_stars", "num_folks", "created_at",
                "avg_commits_per_day", "avg_time_first_response_to_issues", "avg_time_opened_issues", "num_maintainers",
                "avg_time_merged_pull_request", "ratio_closed_open_issues", "num_people_open_issues", "ratio_commit_per_devs");
    }

    /**
     * Take the advantage of java reflection to get all fields of a clazz.
     *
     * @param clazz The clazz.
     * @return A set of clazz's field name.
     */
    private Iterable<String> getFields(Class<?> clazz) {
        Map<String, Field> fields = new HashMap<>();
        while(clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!fields.containsKey(field.getName())) {
                    fields.put(field.getName(), field);
                }
            }

            clazz = clazz.getSuperclass();
        }
        LOGGER.debug("All fields of class {}: {}", clazz, fields);
        return fields.keySet();
    }
}
