package kz.danekerscode.basejparepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = DanekerscodeRepoImpl.class)
public class BaseJpaRepoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseJpaRepoApplication.class, args);
    }

}
