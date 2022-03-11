package training.model;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public class StudentAffinityKey {
    int studentId;
    @AffinityKeyMapped
    String deptName;

    public StudentAffinityKey(int studentId, String deptName) {
        this.studentId = studentId;
        this.deptName = deptName;
    }
}
