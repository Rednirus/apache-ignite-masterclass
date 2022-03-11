package training.ignite.compute;

import org.apache.ignite.Ignite;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteCallable;
import training.model.Department;

import javax.cache.Cache;
import java.util.List;
import java.util.stream.Collectors;

import static training.ignite.CacheConfigUtil.DEPT_CACHE;

public class LocateDepartmentsTask implements IgniteCallable<List<Department>> {
    private Ignite ignite;

    public LocateDepartmentsTask(Ignite ignite) {
        this.ignite = ignite;
    }

    @Override
    public List<Department> call() throws Exception {
        ClusterNode localNode = ignite.cluster().localNode();
        int localSize = ignite.cache(DEPT_CACHE).localSize(CachePeekMode.PRIMARY);
        System.out.println("Executing query on " + localNode.id() + ", data size = " + localSize);

        return ignite
                .cache(DEPT_CACHE)
                .query(new ScanQuery<Integer, Department>().setLocal(true))
                .getAll()
                .stream()
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList());
    }
}
