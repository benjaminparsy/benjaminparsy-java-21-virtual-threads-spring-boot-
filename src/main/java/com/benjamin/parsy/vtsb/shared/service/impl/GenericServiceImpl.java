package com.benjamin.parsy.vtsb.shared.service.impl;

import com.benjamin.parsy.vtsb.shared.service.GenericService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public abstract class GenericServiceImpl<I> implements GenericService<I> {

    private final JpaRepository<I, Long> repository;

    protected GenericServiceImpl(JpaRepository<I, Long> repository) {
        this.repository = repository;
    }

    public I save(I obj) {
        return repository.save(obj);
    }

    public List<I> findAll() {
        return repository.findAll(Sort.by("id"));
    }

    public Optional<I> findById(Long id) {
        return repository.findById(id);
    }

    public List<I> findAllByIdIn(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public boolean deleteById(@NonNull long id) {
        repository.deleteById(id);
        return !repository.existsById(id);
    }

}
