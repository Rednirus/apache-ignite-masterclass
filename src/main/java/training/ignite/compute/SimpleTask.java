package training.ignite.compute;

import org.apache.ignite.lang.IgniteCallable;

public class SimpleTask implements IgniteCallable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("Simple task deployed");
        return 1;
    }
}
