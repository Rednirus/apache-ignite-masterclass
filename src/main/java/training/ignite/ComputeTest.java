package training.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.DeploymentMode;
import org.apache.ignite.configuration.IgniteConfiguration;
import training.ignite.compute.LocateDepartmentsTask;
import training.ignite.compute.LocateStudentsTask;
import training.ignite.compute.SimpleTask;
import training.model.Department;
import training.model.Student;

import javax.cache.Cache;
import java.util.List;

import static training.ignite.CacheConfigUtil.DEPT_CACHE;
import static training.ignite.CacheConfigUtil.deptCacheConfig;


public class ComputeTest {
  public static void main(String[] args) {
    IgniteConfiguration cfg = new IgniteConfiguration();
    cfg.setClientMode(true);
    try (Ignite ignite = Ignition.start(cfg)) {

        ClusterGroup serversGrp = ignite.cluster().forServers();

        //run on any one node
        //ignite.compute(serversGrp).call(new SimpleTask());

        //run on all nodes
        //ignite.compute(serversGrp).broadcast(new SimpleTask());

       // printAllDepartments(ignite);

        printDepartmentsFromEachNode(ignite);
        //printStudentsFromEachNode(ignite);
    }
  }

    private static void printAllDepartments(Ignite ignite) {
        IgniteCache<Integer, Department> deptCache = ignite.getOrCreateCache(deptCacheConfig());
        List<Cache.Entry<Object, Object>> depts = deptCache.query(new ScanQuery<>()).getAll();
        System.out.println("All departs list:\n" +  deptCache.size(CachePeekMode.PRIMARY));
        depts.stream().forEach(entry -> System.out.println(entry.getValue()));
    }


    private static void printDepartmentsFromEachNode(Ignite ignite) {
        ClusterGroup servers = ignite.cluster().forServers();
        servers
                .nodes()
                .forEach(
                        node -> {
                            ClusterGroup clusterGroup = ignite.cluster().forNode(node);
                            List<Department> list = ignite.compute(clusterGroup).call(new LocateDepartmentsTask(ignite));
                            System.out.println("Node =" + node.id());
                            System.out.println("Dept list =" + list);
                        });
    }
    private static void printStudentsFromEachNode(Ignite ignite) {
        ClusterGroup servers = ignite.cluster().forServers();
        servers
            .nodes()
            .forEach(
                node -> {
                  ClusterGroup clusterGroup = ignite.cluster().forNode(node);
                  List<Student> list = ignite.compute(clusterGroup).call(new LocateStudentsTask(ignite));
                  System.out.println("Node =" + node.id());
                  System.out.println("students list= " + list);
                });
    }
}
