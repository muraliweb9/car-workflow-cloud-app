package com.interview.carworkflowcloud.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable {
    @Id
    private String id;
    private String locationName;
}
