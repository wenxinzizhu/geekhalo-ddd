package com.geekhalo.ddd.lite.query.fetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public interface Fetcher<K, V> {
    Logger LOGGER = LoggerFactory.getLogger(Fetcher.class);

    K getKey();

    Consumer<V> whenFound();

    default Consumer<K> whenNotFound(){
        return id->{
            LOGGER.warn("key {} not found", id);
        };
    }


    static <K, V> FetcherBuilder<K, V> buildFor(K key){
        return new FetcherBuilder(key);
    }

    class FetcherBuilder<K, V>{
        private final K key;
        private Consumer<V> whenFound = a->{

        };

        private Consumer<K> whenNotFound = KEY ->{
            LOGGER.warn("key {} not found", KEY);
        };

        private FetcherBuilder(K key){
            this.key = key;
        }

        public FetcherBuilder<K, V> whenFound(Consumer<V> consumer){
            this.whenFound = consumer.andThen(this.whenFound);
            return this;
        }

        public FetcherBuilder<K, V> whenNotFound(Consumer<K> consumer){
            this.whenNotFound = consumer.andThen(this.whenNotFound);
            return this;
        }

        public Fetcher<K, V> build(){
            if (this.key == null){
                return null;
            }

            return new Fetcher<K, V>() {

                @Override
                public K getKey() {
                    return key;
                }

                @Override
                public Consumer<V> whenFound() {
                    return whenFound;
                }

                @Override
                public Consumer<K> whenNotFound(){
                    return whenNotFound;
                }
            };
        }
    }
}
