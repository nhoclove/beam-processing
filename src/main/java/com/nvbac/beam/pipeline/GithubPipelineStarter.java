package com.nvbac.beam.pipeline;

import com.nvbac.beam.conf.AppConf;
import com.nvbac.beam.conf.ConfigKeys;
import com.nvbac.beam.pipeline.sink.CSVSink;
import com.nvbac.beam.pipeline.source.GithubSource;
import com.nvbac.beam.pipeline.source.ISource;
import com.nvbac.beam.pipeline.source.github.GithubRepo;
import com.nvbac.beam.pipeline.transform.ITransformer;
import com.nvbac.beam.pipeline.transform.github.GithubTransformer;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class GithubPipelineStarter implements PipelineStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubPipelineStarter.class);

    private AppConf conf;
    private ISource<GithubRepo> source;
    private ITransformer<PCollection<GithubRepo>, PCollection<List<GithubRepo>>> transformer;
    private Pipeline pipeline;

    public void initialize() {
        LOGGER.info("Initializing beam-pipeline pipeline");
        this.conf = new AppConf();
        LOGGER.debug("Initializing beam-pipeline with configs: {}", conf);
        this.source = new GithubSource(this.conf);
        this.source.start();
        this.transformer = new GithubTransformer();

        PipelineOptions options = PipelineOptionsFactory.create();
        this.pipeline = Pipeline.create(options);
    }

    public void start() {
        LOGGER.debug("Starting beam-pipeline pipeline");
        this.pipeline.apply(Create.of(this.source.poll()).withCoder(AvroCoder.of(GithubRepo.class)))
                .apply(this.transformer.transform())
                .apply(FileIO.<List<GithubRepo>>write()
                    .via(new CSVSink())
                    .to(this.conf.get(ConfigKeys.OUTPUT_PATH, ".")).withNumShards(1)
                    .withPrefix(this.conf.get(ConfigKeys.OUTPUT_PREFIX, "data"))
                    .withSuffix(this.conf.get(ConfigKeys.OUTPUT_SUFFIX, ".csv")));
        pipeline.run().waitUntilFinish();
    }

    public void stop() {
        LOGGER.info("Stopping beam-pipeline pipeline");
        this.source.stop();
    }
}
