package com.benjamin.parsy.vtsb.shared.service;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface GenericService<I> {

    I save(I obj);

    List<I> findAll();

    Optional<I> findById(Long id);

    List<I> findAllByIdIn(List<Long> ids);

    boolean deleteById(@NonNull long id);

}
