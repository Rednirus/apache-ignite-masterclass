package training.ignite.cache.store;

import org.apache.ignite.cache.store.CacheStoreAdapter;
import training.model.Department;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

public class DepartmentCacheStore extends CacheStoreAdapter<Integer, Department> {
    @Override
    public Department load(Integer key) throws CacheLoaderException {
        System.out.println("get received  : "+  key);
        return null;
    }

    @Override
    public void write(Cache.Entry<? extends Integer, ? extends Department> entry) throws CacheWriterException {
        System.out.println("write received : " +  entry);
    }

    @Override
    public void delete(Object key) throws CacheWriterException {

    }
}
