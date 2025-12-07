package com.esprit.persistence.entities.compositekey;

import java.io.Serializable;
import lombok.Data;

@Data
public class StgOrderId implements Serializable {
    private Long id;       // order_id
    private String batchId;
}
