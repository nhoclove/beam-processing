package com.nvbac.beam.pipeline.transform;

import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PInput;
import org.apache.beam.sdk.values.POutput;

public interface ITransformer<T extends PInput, R extends POutput> {

    /**
     * Apply the transformations to a pipeline.
     * Can be a chain of transformations.
     *
     * @return PTransform instance represents a chain of transformations.
     */
    PTransform<T, R> transform();
}
