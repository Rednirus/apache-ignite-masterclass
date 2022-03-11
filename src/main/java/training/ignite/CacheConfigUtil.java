package training.ignite;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.DeploymentMode;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.jetbrains.annotations.NotNull;
import training.ignite.cache.store.CacheStoreFactory;
import training.ignite.cache.store.DepartmentCacheStore;
import training.model.Department;
import training.model.Student;

import javax.cache.configuration.Factory;

public class CacheConfigUtil {
    public static final String DEPT_CACHE = "deptCache";
    public static final String STUDENT_CACHE = "studentCache";

    public static IgniteConfiguration getCfg() {
        DataStorageConfiguration storageCfg = new DataStorageConfiguration();
        storageCfg.getDefaultDataRegionConfiguration().setPersistenceEnabled(true);
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setDataStorageConfiguration(storageCfg);
        cfg.setDeploymentMode(DeploymentMode.CONTINUOUS);
        cfg.setPeerClassLoadingEnabled(true);
        return cfg;
    }

    @NotNull
    public static CacheConfiguration<Integer, Department> deptCacheConfig() {
        CacheConfiguration<Integer, Department> deptCacheConfig = new CacheConfiguration<>(DEPT_CACHE);
        deptCacheConfig.setCacheMode(CacheMode.PARTITIONED);
        deptCacheConfig.setBackups(1);

        //Enable cache store

        DepartmentCacheStore cacheStore = new DepartmentCacheStore();
        Factory cacheStoreFactory = new CacheStoreFactory(cacheStore);
        deptCacheConfig.setCacheStoreFactory(cacheStoreFactory);
        deptCacheConfig.setWriteThrough(true);

        //Enable Sql support
        deptCacheConfig.setIndexedTypes(Integer.class, Department.class);

        //set expiry policy
        //deptCacheConfig.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));

        //set eviction policy
        //deptCacheConfig.setOnheapCacheEnabled(true);
        //deptCacheConfig.setEvictionPolicyFactory(() -> new FifoEvictionPolicy<>(2));
        return deptCacheConfig;
    }


    @NotNull
    public static CacheConfiguration<AffinityKey<Integer>, Student> studentsCacheConfig() {

        //Integer inside Affinity is DeptId data type
        CacheConfiguration<AffinityKey<Integer>, Student> studentCacheCfg =
                new CacheConfiguration<>(STUDENT_CACHE);
        studentCacheCfg.setCacheMode(CacheMode.PARTITIONED); // Default.
        studentCacheCfg.setBackups(1);
        studentCacheCfg.setIndexedTypes(AffinityKey.class, Student.class);
        return studentCacheCfg;
    }


    public static void initializeCaches(IgniteCache<Integer, Department> deptCache,
                                         IgniteCache<AffinityKey<Integer>, Student> studentCache) {

        Department d1 = new Department(1, "CS");
        Department d2 = new Department(2, "ECE");
        Department d3 = new Department(3, "CIVIL");

        if(deptCache.size(CachePeekMode.ALL) == 0){
            System.out.println("Adding dept data to cache");
            deptCache.put(d1.getDeptId(), d1);
            deptCache.put(d2.getDeptId(), d2);
            deptCache.put(d3.getDeptId(), d3);
        }

        Student s1 = new Student(1, "Rohan", d1.getDeptId(), d1.getDeptName());
        Student s2 = new Student(2, "Mohan", d2.getDeptId(), d2.getDeptName());
        Student s3 = new Student(3, "Kartik", d3.getDeptId(), d3.getDeptName());
        Student s4 = new Student(4, "Sham", d2.getDeptId(), d2.getDeptName());
        Student s5 = new Student(5, "Ram", d1.getDeptId(), d1.getDeptName());

        if(studentCache.size(CachePeekMode.ALL) == 0){
            System.out.println("Adding students data to cache");
            studentCache.put(new AffinityKey<>(s1.getId(), s1.getDeptId()), s1);
            studentCache.put(new AffinityKey<>(s2.getId(), s2.getDeptId()), s2);
            studentCache.put(new AffinityKey<>(s3.getId(), s3.getDeptId()), s3);
            studentCache.put(new AffinityKey<>(s4.getId(), s4.getDeptId()), s4);
            studentCache.put(new AffinityKey<>(s5.getId(), s5.getDeptId()), s5);
        }
    }


}
