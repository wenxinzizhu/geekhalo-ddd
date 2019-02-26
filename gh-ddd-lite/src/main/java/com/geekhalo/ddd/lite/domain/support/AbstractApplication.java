package com.geekhalo.ddd.lite.domain.support;

import com.geekhalo.ddd.lite.domain.*;
import com.google.common.base.Preconditions;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by taoli on 17/11/17.
 */
public abstract class AbstractApplication implements Application {
    private final Logger logger;

    protected Logger logger(){
        return logger;
    }

    protected AbstractApplication(Logger logger) {
        this.logger = logger;
    }

    protected AbstractApplication(){
        this.logger = LoggerFactory.getLogger(getClass());
    }

    protected <ID, A extends Aggregate<ID>> Creator<ID, A> creatorFor(AggregateRepository<ID, A> aggregateRepository){
        return new Creator<ID, A>(aggregateRepository);
    }

    protected <ID, A extends Aggregate<ID>> Updater<ID, A> updaterFor(AggregateRepository<ID, A> aggregateRepository){
        return new Updater<ID, A>(aggregateRepository);
    }

    protected <ID, A extends Aggregate<ID>> Syncer<ID, A> syncerFor(AggregateRepository<ID, A> aggregateRepository){
        return new Syncer<ID, A>(aggregateRepository);
    }


    protected class Creator<ID, A extends Aggregate<ID>>{
        private final AggregateRepository<ID, A> aggregateRepository;
        private Supplier<A> instanceFun;
        private Consumer<A> updater = a->{
            if(a instanceof AbstractEntity){
                ((AbstractEntity)a).prePersist();
            }
        };
        private ValidationHandler validationHandler = new ExceptionBasedValidationHandler();
        private DomainEventPublisher eventPublisher;
        private Consumer<A> successFun = a -> logger.info("success to save {}", a);
        private BiConsumer<A, Exception> errorFun = (a, e) -> {
            logger.error("failed to save {}.", a, e);
            if (e instanceof RuntimeException){
                throw (RuntimeException) e;
            }else {
                throw new BusinessException(500, e.toString(), e);
            }
        };

        Creator(AggregateRepository<ID, A> aggregateRepository) {
            Preconditions.checkArgument(aggregateRepository != null);
            this.aggregateRepository = aggregateRepository;
        }

        public Creator<ID, A> instance(Supplier<A> instanceFun){
            Preconditions.checkArgument(instanceFun != null);
            this.instanceFun = instanceFun;
            return this;
        }

        public Creator<ID, A> update(Consumer<A> updater){
            Preconditions.checkArgument(updater != null);
            this.updater = this.updater.andThen(updater);
            return this;
        }

        public Creator<ID, A> validate(ValidationHandler handler){
            Preconditions.checkArgument(handler != null);
            this.validationHandler = handler;
            return this;
        }

        public Creator<ID, A> publishBy(DomainEventPublisher publisher){
            Preconditions.checkArgument(publisher != null);
            this.eventPublisher = publisher;
            return this;
        }

        public Creator<ID, A> onSuccess(Consumer<A> onSuccessFun){
            Preconditions.checkArgument(onSuccessFun != null);
            this.successFun = onSuccessFun.andThen(this.successFun);
            return this;
        }

        public Creator<ID, A> onError(BiConsumer<A, Exception> errorFun){
            Preconditions.checkArgument(errorFun != null);
            this.errorFun = errorFun.andThen(this.errorFun);
            return this;
        }

        public A call(){
            Preconditions.checkArgument(this.instanceFun != null, "instance fun can not be null");
            Preconditions.checkArgument(this.aggregateRepository != null, "aggregateRepository can not be null");
            A a = null;
            try{
                a = this.instanceFun.get();

                this.updater.accept(a);

                a.validate(validationHandler);
                if (validationHandler instanceof ValidatrionChecker){
                    ((ValidatrionChecker)validationHandler).check();
                }

                this.aggregateRepository.save(a);

                if (this.eventPublisher != null){
                    this.eventPublisher.publishAll(a.getEvents());
                }

                this.successFun.accept(a);
            }catch (Exception e){
                this.errorFun.accept(a, e);
            }
            return a;
        }
    }

    protected class Updater<ID, A extends Aggregate<ID>> {
        private final AggregateRepository<ID, A> aggregateRepository;
        private ID id;
        private Supplier<Optional<A>> loader;
        private Consumer<ID> onNotExistFun = id-> {throw new AggregateNotFountException(id);};
        private Consumer<A> updater = a->{
            if (a instanceof  AbstractEntity){
                ((AbstractEntity) a).preUpdate();
            }
        };
        private ValidationHandler validationHandler = new ExceptionBasedValidationHandler();
        private DomainEventPublisher domainEventPublisher;
        private Consumer<Data> successFun = a -> logger.info("success to update {}", a);
        private BiConsumer<Data, Exception> errorFun = (a, e) -> {
            logger.error("failed to update {}.", a, e);
            if (e instanceof RuntimeException){
                throw (RuntimeException) e;
            }else {
                throw new BusinessException(500, e.toString(), e);
            }
        };

        Updater(AggregateRepository<ID, A> aggregateRepository) {
            this.aggregateRepository = aggregateRepository;
        }

        public Updater<ID, A> id(ID id){
            Preconditions.checkArgument(id != null);
            this.id = id;
            return this;
        }

        public Updater<ID, A> loader(Supplier<Optional<A>> loader){
            Preconditions.checkArgument(loader != null);
            this.loader = loader;
            return this;
        }

        public Updater<ID, A> update(Consumer<A> updater){
            Preconditions.checkArgument(updater != null);
            this.updater = updater.andThen(this.updater);
            return this;
        }

        public Updater<ID, A> validate(ValidationHandler handler){
            Preconditions.checkArgument(handler != null);
            this.validationHandler = handler;
            return this;
        }

        public Updater<ID, A> publishBy(DomainEventPublisher publisher){
            Preconditions.checkArgument(publisher != null);
            this.domainEventPublisher = publisher;
            return this;
        }

        public Updater<ID, A> onSuccess(Consumer<Data> onSuccessFun){
            Preconditions.checkArgument(onSuccessFun != null);
            this.successFun = onSuccessFun.andThen(this.successFun);
            return this;
        }

        public Updater<ID, A> onError(BiConsumer<Data, Exception> errorFun){
            Preconditions.checkArgument(errorFun != null);
            this.errorFun = errorFun.andThen(this.errorFun);
            return this;
        }


        public Updater<ID, A> onNotExist(Consumer<ID> onNotExistFun){
            Preconditions.checkArgument(onNotExistFun != null);
            this.onNotExistFun = onNotExistFun.andThen(this.onNotExistFun);
            return this;
        }

        public A call(){
            Preconditions.checkArgument(this.aggregateRepository != null, "aggregateRepository can not be null");
            Preconditions.checkArgument((this.loader != null || this.id != null), "id and loader can not both be null");
            A a = null;
            try {
                if (id != null && loader != null){
                    throw new RuntimeException("id and loader can both set");
                }
                if (id != null){
                    this.loader = ()->this.aggregateRepository.getById(this.id);
                }
                Optional<A> aOptional = this.loader.get();

                if (!aOptional.isPresent()){
                    this.onNotExistFun.accept(id);
                }

                a = aOptional.get();
                updater.accept(a);

                a.validate(validationHandler);
                if (validationHandler instanceof ValidatrionChecker){
                    ((ValidatrionChecker)validationHandler).check();
                }

                this.aggregateRepository.update(a);

                if (domainEventPublisher != null){
                    domainEventPublisher.publishAll(a.getEvents());
                }

                this.successFun.accept(new Data(id, a));

            }catch (Exception e){
                this.errorFun.accept(new Data(id, a), e);
            }
            return a;
        }

        @Value
        class Data{
            private final ID id;
            private final A a;
        }
    }

    protected class Syncer<ID, A extends Aggregate<ID>> {
        private final AggregateRepository<ID, A> aggregateRepository;
        private Supplier<A> instanceFun;
        private Supplier<Optional<A>> loadFun;
        private Consumer<A> updater = a->{
            if (a instanceof AbstractEntity){
                ((AbstractEntity) a).preUpdate();
            }
        };
        private Consumer<A> updaterWhenCreate = a->{
            if (a instanceof AbstractEntity){
                ((AbstractEntity)a).prePersist();
            }
        };
        private ValidationHandler validationHandler = new ExceptionBasedValidationHandler();
        private DomainEventPublisher eventPublisher;
        private Consumer<Data> successFun = a -> logger.info("success to sync {}", a);
        private BiConsumer<Data, Exception> errorFun = (a, e) -> {
            logger.error("failed to sync {}.", a, e);
            if (e instanceof RuntimeException){
                throw (RuntimeException) e;
            }else {
                throw new BusinessException(500, e.toString(), e);
            }
        };

        Syncer(AggregateRepository<ID, A> aggregateRepository) {
            this.aggregateRepository = aggregateRepository;
        }

        public Syncer<ID, A> instance(Supplier<A> instanceFun){
            Preconditions.checkArgument(instanceFun != null);
            this.instanceFun = instanceFun;
            return this;
        }

        public Syncer<ID, A> loadBy(Supplier<Optional<A>> loadFun){
            Preconditions.checkArgument(loadFun != null);
            this.loadFun = loadFun;
            return this;
        }

        public Syncer<ID, A> update(Consumer<A> updater){
            Preconditions.checkArgument(updater != null);
            this.updater = updater.andThen(this.updater);
            return this;
        }

        public Syncer<ID, A> updateWhenCreate(Consumer<A> updater){
            Preconditions.checkArgument(updater != null);
            this.updaterWhenCreate = updater.andThen(this.updaterWhenCreate);
            return this;
        }

        public Syncer<ID, A> validate(ValidationHandler handler){
            Preconditions.checkArgument(handler != null);
            this.validationHandler = handler;
            return this;
        }

        public Syncer<ID, A> publishBy(DomainEventPublisher publisher){
            Preconditions.checkArgument(publisher != null);
            this.eventPublisher = publisher;
            return this;
        }

        public Syncer<ID, A> onSuccess(Consumer<Data> onSuccessFun){
            Preconditions.checkArgument(onSuccessFun != null);
            this.successFun = onSuccessFun.andThen(this.successFun);
            return this;
        }

        public Syncer<ID, A> onError(BiConsumer<Data, Exception> errorFun){
            Preconditions.checkArgument(errorFun != null);
            this.errorFun = errorFun.andThen(this.errorFun);
            return this;
        }


        public A call(){
            Preconditions.checkArgument(this.aggregateRepository != null, "aggregateRepository can not be null");
            Preconditions.checkArgument(this.loadFun != null, "load fun can not be nul");
            Preconditions.checkArgument(this.instanceFun != null, "instance fun can noe be null");
            A a = null;
            try {
                Optional<A> aOptional = this.loadFun.get();
                if (aOptional.isPresent()){
                    a = aOptional.get();

                    updater.accept(a);

                    a.validate(validationHandler);
                    if (validationHandler instanceof ValidatrionChecker){
                        ((ValidatrionChecker)validationHandler).check();
                    }

                    this.aggregateRepository.update(a);

                    if (this.eventPublisher != null){
                        eventPublisher.publishAll(a.getEvents());
                    }

                    this.successFun.accept(new Data(a.getId(), Action.CREATE, a));
                }else {
                    a = this.instanceFun.get();

                    updaterWhenCreate.accept(a);

                    updater.accept(a);

                    a.validate(validationHandler);
                    if (validationHandler instanceof ValidatrionChecker){
                        ((ValidatrionChecker)validationHandler).check();
                    }

                    this.aggregateRepository.save(a);

                    if (this.eventPublisher != null){
                        eventPublisher.publishAll(a.getEvents());
                    }

                    this.successFun.accept(new Data(a.getId(), Action.UPDATE, a));
                }
            }catch (Exception e){
                this.errorFun.accept(new Data(null, null, a), e);
            }
            return a;
        }

        @Value
        class Data{
            private final ID id;
            private final Action action;
            private final A a;
        }


    }
    enum Action{
        CREATE, UPDATE
    }
}
