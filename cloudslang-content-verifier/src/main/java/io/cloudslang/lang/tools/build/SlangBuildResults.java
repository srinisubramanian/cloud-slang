/*
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.lang.tools.build;

import io.cloudslang.lang.tools.build.tester.RunTestsResults;

import java.util.Map;

/**
 * Created by stoneo on 4/1/2015.
 **/
public class SlangBuildResults {

    private int numberOfCompiledSources;
    private RunTestsResults runTestsResults;

    public SlangBuildResults(int numberOfCompiledSources, RunTestsResults runTestsResults) {
        this.numberOfCompiledSources = numberOfCompiledSources;
        this.runTestsResults = runTestsResults;
    }

    public int getNumberOfCompiledSources() {
        return numberOfCompiledSources;
    }

    public RunTestsResults getRunTestsResults() {
        return runTestsResults;
    }
}
