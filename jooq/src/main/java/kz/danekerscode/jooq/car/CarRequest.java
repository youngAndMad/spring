package kz.danekerscode.jooq.car;

public record CarRequest(
        String brand,
        String model,
        Integer madeYear
) {
}
