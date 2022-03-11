package training.ignite.compute;

import org.apache.ignite.Ignite;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteCallable;
import training.model.Student;

import javax.cache.Cache;
import java.util.List;
import java.util.stream.Collectors;

import static training.ignite.CacheConfigUtil.STUDENT_CACHE;

public class LocateStudentsTask implements IgniteCallable<List<Student>> {
    private Ignite ignite;

    public LocateStudentsTask(Ignite ignite) {
        this.ignite = ignite;
    }

    @Override
    public List<Student> call() throws Exception {
        ClusterNode localNode = ignite.cluster().localNode();
        int localSize = ignite.cache(STUDENT_CACHE).localSize(CachePeekMode.PRIMARY);
        System.out.println("Executing query on " + localNode.id() + ", data size = " + localSize);

        return ignite
                .cache(STUDENT_CACHE)
                .query(new ScanQuery<AffinityKey<Integer>, Student>().setLocal(true))
                .getAll()
                .stream()
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList());
    }
}
