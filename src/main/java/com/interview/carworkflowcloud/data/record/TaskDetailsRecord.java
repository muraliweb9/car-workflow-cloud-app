package com.interview.carworkflowcloud.data.record;

import jakarta.persistence.Id;

public record TaskDetailsRecord(@Id String id, String name) {}
