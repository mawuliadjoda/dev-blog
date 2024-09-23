package com.esprit.ennum.converter;

import com.esprit.ennum.StatusEnum;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter extends AbstractBaseEnumConverter <StatusEnum, String> {

    protected StatusConverter() {
        super(StatusEnum.class);
    }
}
