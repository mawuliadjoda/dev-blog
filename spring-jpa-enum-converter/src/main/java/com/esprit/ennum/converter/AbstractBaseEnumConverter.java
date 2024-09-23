package com.esprit.ennum.converter;

import com.esprit.ennum.common.BaseEnum;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;


public abstract class AbstractBaseEnumConverter<E extends Enum<E> & BaseEnum<T>, T> implements AttributeConverter<E, T> {

    /*
    private final Class<E> enumClass;

    protected AbstractBaseEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

     */

    protected abstract E[] getValueList();

    @Override
    public T convertToDatabaseColumn(E attribute) {
        return attribute != null ? attribute.getLibelle() : null;
    }

    @Override
    public E convertToEntityAttribute(T dbData) {
        if (dbData == null) {
            return null;
        }


        /*
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getLibelle().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);

         */
        return Arrays.stream(getValueList())
                .filter(e -> e.getLibelle().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }
}
