package com.geekhalo.ddd.lite.query.fetcher;


import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FetcherRunner<K, V> {
    private final Function<List<K>, List<V>> loader;
    private final Function<V, K> keyRecover;

    public FetcherRunner(Function<List<K>, List<V>> loader, Function<V, K> keyRecover) {
        this.loader = loader;
        this.keyRecover = keyRecover;
    }

    public void fetch(List<Fetcher<K, V>> fetchers){
        List<K> keys = fetchers.stream()
                        .filter(fetcher->fetcher!=null)
                        .map(fetcher->fetcher.getKey())
                        .filter(key -> key!=null)
                        .distinct()
                        .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(keys)){
            return;
        }

        Map<K, V> mapAsIdKey = this.loader.apply(keys).stream()
                                .collect(Collectors.toMap(a -> this.keyRecover.apply(a), a -> a));
        if (mapAsIdKey == null || mapAsIdKey.isEmpty()){
            return;
        }

        fetchers.forEach(fetcher->{
            V v = mapAsIdKey.get(fetcher.getKey());
            if (v != null){
                fetcher.whenFound().accept(v);
            }else {
                fetcher.whenNotFound().accept(fetcher.getKey());
            }
        });
    }
}
