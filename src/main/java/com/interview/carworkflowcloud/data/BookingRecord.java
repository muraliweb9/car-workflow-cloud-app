package com.interview.carworkflowcloud.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRecord {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "customerdetails_id")
    private CustomerDetails customerDetails;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    private boolean allChecksDone;

}
