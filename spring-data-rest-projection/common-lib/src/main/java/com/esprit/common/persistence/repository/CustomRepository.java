package com.esprit.common.persistence.repository;

import java.util.List;

public interface CustomRepository<T> {
    List<T> search(String input);
}