package comarch.client.ishift.pl.tests;

import comarch.client.ishift.pl.data.DataBasesListSingleton;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class Bootstrap implements CommandLineRunner {

    private final DataBasesListSingleton dataBasesListSingleton;

    public Bootstrap() {
        this.dataBasesListSingleton = DataBasesListSingleton.getInstance(null);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(dataBasesListSingleton.getDatabasesList().size());
    }
}
