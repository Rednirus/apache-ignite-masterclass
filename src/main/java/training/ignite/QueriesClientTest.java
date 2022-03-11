package training.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;
import training.model.Department;

import javax.cache.Cache;
import java.util.List;
import java.util.stream.Collectors;

import static training.ignite.CacheConfigUtil.*;

public class QueriesClientTest {
  public static void main(String[] args) throws IgniteCheckedException {
      IgniteConfiguration cfg = new IgniteConfiguration();
      //IgniteConfiguration cfg = getCfg();
        cfg.setClientMode(true);
      try (Ignite ignite = Ignition.start(cfg)) {
          ignite.cluster().state(ClusterState.ACTIVE);
          IgniteCache<Integer, Department> deptCache = ignite.getOrCreateCache(deptCacheConfig());

          //scanQuery(deptCache);

          //scanQueryWithPredicate(deptCache);

           //sqlQuery(deptCache);

           //sqlFieldQuery(deptCache);

          sqlAggregateQuery(deptCache);

          //affinityToNodes(ignite, deptCache);
      }
  }

    private static void affinityToNodes(Ignite ignite, IgniteCache<Integer, Department> deptCache) {
      deptCache.query(new ScanQuery<>()).getAll().stream().forEach(entry-> {
          Affinity<Object> cacheAffinity = ignite.affinity(deptCache.getName());
          int partition = cacheAffinity.partition(entry.getKey());
          ClusterNode clusterNode = cacheAffinity.mapPartitionToNode(partition);
          System.out.println("Key = "+entry.getKey() +", stored on partition= "+partition+", node= "+ clusterNode.id().toString());
      });
    }

    private static void scanQueryWithPredicate(IgniteCache<Integer, Department> deptCache) {
        System.out.println("\nPrint all department with id > 1");
        IgniteBiPredicate<Integer, Department> filter = (k, v) -> {
            System.out.println("Running scan query predicate");
            return k > 1;
        };
        List<Cache.Entry<Integer, Department>> entries = deptCache.query(new ScanQuery<>(filter)).getAll();
        entries.forEach(entry -> System.out.println("key: " +  entry.getKey() + ", value : "+ entry.getValue()));
    }

    private static void scanQuery(IgniteCache<Integer, Department> deptCache) {
        System.out.println("\nPrint all departments");
        List<Cache.Entry<Integer, Department>> entries = deptCache.query(new ScanQuery<Integer, Department>()).getAll();
        entries.forEach(entry -> System.out.println("key: " +  entry.getKey() + ", value : "+ entry.getValue()));
    }

    private static void sqlQuery(IgniteCache<Integer, Department> deptCache) {
        System.out.println("\nPrint all departments using sql");
        List<List<?>> entries = deptCache.query(new SqlFieldsQuery("select _val from Department")).getAll();
        List<Department> departmentList = entries.stream().map(entry -> (Department) entry.get(0)).collect(Collectors.toList());
        //System.out.println(entries);
        departmentList.forEach(System.out::println);
    }

    private static void sqlFieldQuery(IgniteCache<Integer, Department> deptCache) {
        System.out.println("\nPrint all departments names using sql with id > 1");
        String sql = "select deptName from Department where deptId > ?";
        List<List<?>> entries = deptCache.query(new SqlFieldsQuery(sql).setArgs("1")).getAll();
        List<String> departmentNameList = entries.stream().map(entry -> (String) entry.get(0)).collect(Collectors.toList());
        //System.out.println(entries);
        departmentNameList.forEach(System.out::println);
    }

    private static void sqlAggregateQuery(IgniteCache<Integer, Department> deptCache) {
        System.out.println("\nPrint departments counts using sql");
        List<List<?>> entries = deptCache.query(new SqlFieldsQuery("select count(*) from Department")).getAll();
        List<Long> departmentNameList = entries.stream().map(entry -> (Long) entry.get(0)).collect(Collectors.toList());
        //System.out.println(entries);
        departmentNameList.forEach(System.out::println);
    }
}
