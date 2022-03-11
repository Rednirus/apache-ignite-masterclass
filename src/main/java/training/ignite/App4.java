package training.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class App4 {

    public static void main(String[] args) throws IgniteCheckedException {
        IgniteConfiguration cfg = new IgniteConfiguration();
        //IgniteConfiguration cfg = getCfg();
        try (Ignite ignite = Ignition.start(cfg)) {
            while (true);
        }
    }
}
