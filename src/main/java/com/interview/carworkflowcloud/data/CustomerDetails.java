package com.interview.carworkflowcloud.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetails {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String licenceNumber;
}
