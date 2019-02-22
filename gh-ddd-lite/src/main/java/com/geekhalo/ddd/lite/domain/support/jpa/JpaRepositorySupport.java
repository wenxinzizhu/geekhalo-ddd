package com.geekhalo.ddd.lite.domain.support.jpa;

import com.geekhalo.ddd.lite.domain.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


/**
 * Created by taoli on 17/11/17.
 */
@Component
public abstract class JpaRepositorySupport<A extends JpaAggregate> implements Repository<Long, A> {
    private final JpaRepository<A, Long> repository;
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaRepositorySupport.class);

    public JpaRepositorySupport(JpaRepository<A, Long> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<A> getById(Long aLong) {
        return repository.findById(aLong);
    }

    @Override
    public List<A> getByIds(List<Long> ids){
        return repository.findAllById(ids);
    }

    @Override
    public void save(A a) {
        repository.save(a);
    }

    @Override
    public void update(A a) {
        repository.save(a);
    }

    @Override
    public void delete(A a) {
        repository.delete(a);
    }
}
