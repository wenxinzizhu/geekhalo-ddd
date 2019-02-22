package com.geekhalo.ddd.lite.domain;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Created by taoli on 17/11/16.
 */
@NoRepositoryBean
public interface Repository<ID, A extends Aggregate<ID>> {
    Optional<A> getById(ID id);

    List<A> getByIds(List<ID> ids);

    void save(A a);

    void update(A a);

    void delete(A a);

    default void deleteById(ID id){
        Optional<A> aOptional = getById(id);
        aOptional.ifPresent(a -> delete(a));
    }
}
