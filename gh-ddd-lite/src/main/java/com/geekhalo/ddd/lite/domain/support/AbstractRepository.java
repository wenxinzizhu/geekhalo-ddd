package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.Aggregate;
import com.geekhalo.ddd.lite.domain.Repository;

/**
 * Created by taoli on 17/11/17.
 */
public abstract class AbstractRepository<ID, A extends Aggregate<ID>> implements Repository<ID, A> {
}
