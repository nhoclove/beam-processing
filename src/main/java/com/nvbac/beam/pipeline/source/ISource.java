package com.nvbac.beam.pipeline.source;

import java.util.List;

public interface ISource<T> {

    /**
     * Start the source.
     * Ex: Opening files or database connections.
     */
    void start();

    /**
     * Poll this source for new records.
     *
     * @return List of records.
     */
    List<T> poll();

    /**
     * Stop the source and clean any used resources.
     * Ex: closing open files or database connections.
     */
    void stop();
}
