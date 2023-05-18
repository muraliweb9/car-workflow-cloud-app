package io.camunda.zeebe.process.test.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import io.camunda.zeebe.process.test.filters.RecordStream;
import io.camunda.zeebe.process.test.filters.StreamFilter;
import io.camunda.zeebe.protocol.record.intent.JobIntent;

public class ProcessInstanceAssert2 extends ProcessInstanceAssert {

    private final RecordStream recordStream;

    public ProcessInstanceAssert2(final long actual, final RecordStream recordStream) {
        super(actual, recordStream);
        this.recordStream = recordStream;
    }

    public ProcessInstanceAssert2 hasProcessInstanceThrownError(final String elementId) {

        final long count =
                StreamFilter.jobRecords(recordStream)
                        .withIntent(JobIntent.ERROR_THROWN)
                        .withElementId(elementId)
                        .stream()
                        .filter(r -> r.getValue().getProcessInstanceKey() == actual)
                        .count();

        assertThat(count)
                .withFailMessage(
                        "Expected element with id %s to have thrown %s error(s), but was %s", elementId, 1, count)
                .isEqualTo(1);

        return this;
    }
}
