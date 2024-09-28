package kz.danekerscode.jooq;

import kz.danekerscode.jooq.car.CarSearchCriteria;
import kz.danekerscode.jooq.car.CarService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JooqApplication {

    public static void main(String[] args) {
        SpringApplication.run(JooqApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(CarService carService) {
        return args -> {
            carService.filter(new CarSearchCriteria(null, null, 2022, null))
                    .forEach(System.out::println);

        };
    }

}
