package com.interview.carworkflowcloud.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRecord {

    private CustomerDetails customerDetails;

    private Car car;

    private boolean allChecksDone;
}
