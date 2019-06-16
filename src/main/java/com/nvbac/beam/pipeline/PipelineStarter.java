package com.nvbac.beam.pipeline;

public interface PipelineStarter {

    /**
     * Initialize all resources for this pipeline.
     * Ex: Initialize sources, transformers, sinks
     */
    void initialize();

    /**
     * Start this pipeline
     */
    void start();

    /**
     * Perform any cleanup to stop this pipeline.
     * Ex: Stop sources, transformers, sinks.
     */
    void stop();
}
