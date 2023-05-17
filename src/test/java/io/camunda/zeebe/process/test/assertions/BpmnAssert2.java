package io.camunda.zeebe.process.test.assertions;

import io.camunda.zeebe.process.test.inspections.model.InspectedProcessInstance;

public class BpmnAssert2 extends BpmnAssert {

    public static ProcessInstanceAssert2 assertThat(final InspectedProcessInstance inspectedProcessInstance) {
        return new ProcessInstanceAssert2(inspectedProcessInstance.getProcessInstanceKey(), getRecordStream());
    }
}
