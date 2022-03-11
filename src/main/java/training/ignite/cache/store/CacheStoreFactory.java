package training.ignite.cache.store;

import org.apache.ignite.cache.store.CacheStore;

import javax.cache.configuration.Factory;

public class CacheStoreFactory implements Factory<CacheStore> {
    private transient final CacheStore cacheStore;

    public CacheStoreFactory(CacheStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    @Override
    public CacheStore create() {
        return cacheStore != null ? cacheStore :  new DepartmentCacheStore();
    }
}
