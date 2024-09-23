package com.esprit.ennum;

import com.esprit.ennum.common.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum implements BaseEnum<String> {
  TRUE("T"),
    FALSE("F"),
    NULL("");

  private final String libelle;
}
