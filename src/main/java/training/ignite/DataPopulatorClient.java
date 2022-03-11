package training.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.IgniteConfiguration;
import training.model.Department;
import training.model.Student;

import static training.ignite.CacheConfigUtil.*;

public class DataPopulatorClient {
    public static void main(String[] args) {
        IgniteConfiguration cfg = new IgniteConfiguration();
        //IgniteConfiguration cfg = getCfg();
        cfg.setClientMode(true);
        try (Ignite ignite = Ignition.start(cfg)) {
            ignite.cluster().state(ClusterState.ACTIVE);
            IgniteCache<Integer, Department> deptCache = ignite.getOrCreateCache(deptCacheConfig());
            IgniteCache<AffinityKey<Integer>, Student> studentCache = ignite.getOrCreateCache(studentsCacheConfig());
            initializeCaches(deptCache, studentCache);
        }
    }
}
