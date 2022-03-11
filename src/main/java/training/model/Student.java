package training.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Student {
    @QuerySqlField(index = true)
    private final int id;
    @QuerySqlField
    private final String name;
    @QuerySqlField
    private final int deptId;
    @QuerySqlField
    private final String deptName;


    public Student(int id, String name, int deptId, String deptName) {
        this.id = id;
        this.name = name;
        this.deptId = deptId;
        this.deptName = deptName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDeptId() {
        return deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                '}';
    }
}
