package com.esprit.persistence.entities.compositekey;

import java.io.Serializable;
import lombok.Data;

@Data
public class StgCustomerId implements Serializable {
    private Long id;       // order_id
    private String batchId;
}
