package com.nvbac.beam;

import com.nvbac.beam.pipeline.GithubPipelineStarter;
import com.nvbac.beam.pipeline.PipelineStarter;

public class Application {

    public static void main(String[] args) {
        PipelineStarter starter = new GithubPipelineStarter();
        starter.initialize();
        starter.start();
        starter.stop();
    }
}
