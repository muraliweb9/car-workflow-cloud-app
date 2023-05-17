package com.interview.carworkflowcloud.data;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Builder
@Data
@Setter(AccessLevel.NONE)
public class VehicleHandoverDetails {
    private boolean isClean;
    private boolean hasFuel;
    private boolean hasExteriorBeenChecked;

    public boolean allChecksDone() {
        return isClean() && isHasFuel() && isHasExteriorBeenChecked();
    }
}
