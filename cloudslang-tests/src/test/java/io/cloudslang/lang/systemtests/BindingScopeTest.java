/**
 * ****************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 * <p/>
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * *****************************************************************************
 */
package io.cloudslang.lang.systemtests;

import com.google.common.collect.Sets;
import io.cloudslang.lang.compiler.SlangSource;
import io.cloudslang.lang.compiler.SlangTextualKeys;
import io.cloudslang.lang.compiler.modeller.model.Executable;
import io.cloudslang.lang.compiler.modeller.model.Flow;
import io.cloudslang.lang.entities.CompilationArtifact;
import io.cloudslang.lang.entities.SystemProperty;
import io.cloudslang.lang.entities.bindings.Output;
import io.cloudslang.lang.entities.utils.ExpressionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * @author Bonczidai Levente
 * @since 3/18/2016
 */
public class BindingScopeTest extends SystemsTestsParent {

    @Test
    public void testTaskPublishValues() throws Exception {
        URL resource = getClass().getResource("/yaml/binding_scope_flow.sl");
        URI operation = getClass().getResource("/yaml/binding_scope_op.sl").toURI();
        Set<SlangSource> path = Sets.newHashSet(SlangSource.fromFile(operation));
        CompilationArtifact compilationArtifact = slang.compile(SlangSource.fromFile(resource.toURI()), path);

        Map<String, Serializable> userInputs = Collections.emptyMap();
        Set<SystemProperty> systemProperties = Collections.emptySet();

        // trigger ExecutionPlan
        RuntimeInformation runtimeInformation = triggerWithData(compilationArtifact, userInputs, systemProperties);

        Map<String, StepData> executionData = runtimeInformation.getTasks();

        StepData taskData = executionData.get(FIRST_STEP_PATH);
        Assert.assertNotNull("task data is null", taskData);

        verifyTaskPublishValues(taskData);
    }

    private void verifyTaskPublishValues(StepData taskData) {
        Map<String, Serializable> expectedPublishValues = new LinkedHashMap<>();
        expectedPublishValues.put("task1_publish_1", "op_output_1_value op_input_1_task task_arg_1_value");
        expectedPublishValues.put("task1_publish_2_conflict", "op_output_2_value");
        Map<String, Serializable> actualPublishValues = taskData.getOutputs();
        Assert.assertEquals("task publish values not as expected", expectedPublishValues, actualPublishValues);
    }

    @Test
    public void testFlowContextInTaskPublishSection() throws Exception {
        URL resource = getClass().getResource("/yaml/binding_scope_flow_context_in_task_publish.sl");
        URI operation = getClass().getResource("/yaml/binding_scope_op.sl").toURI();
        Set<SlangSource> path = Sets.newHashSet(SlangSource.fromFile(operation));

        // pre-validation - task expression uses flow var name
        SlangSource flowSource = SlangSource.fromFile(resource.toURI());
        Executable flowExecutable = slangCompiler.preCompile(flowSource);
        String flowVarName = "flow_var";
        Assert.assertEquals(
                "Input name should be: " + flowVarName,
                flowVarName,
                flowExecutable.getInputs().get(0).getName()
        );
        @SuppressWarnings("unchecked")
        List<Output> taskPublishValues = (List<Output>) ((Flow) flowExecutable)
                .getWorkflow()
                .getTasks()
                .getFirst()
                .getPostTaskActionData()
                .get(SlangTextualKeys.PUBLISH_KEY);
        Assert.assertEquals(
                "Task expression should contain: " + flowVarName,
                flowVarName,
                StringUtils.trim(ExpressionUtils.extractExpression(taskPublishValues.get(0).getValue()))
        );

        CompilationArtifact compilationArtifact = slang.compile(flowSource, path);

        Map<String, Serializable> userInputs = Collections.emptyMap();
        Set<SystemProperty> systemProperties = Collections.emptySet();

        exception.expect(RuntimeException.class);
        exception.expectMessage("flow_var");
        exception.expectMessage("not defined");

        // trigger ExecutionPlan
        triggerWithData(compilationArtifact, userInputs, systemProperties);
    }

}
