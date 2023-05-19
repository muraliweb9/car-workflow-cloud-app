package com.interview.carworkflowcloud.data;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Builder
@Data
@Setter(AccessLevel.NONE)
public class VehicleHandoverDetails {
    private boolean cleaned;
    private boolean fuelFull;
    private boolean exteriorGood;

    public boolean allChecksDone() {
        return isCleaned() && isFuelFull() && isExteriorGood();
    }
}
