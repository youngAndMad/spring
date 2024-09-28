package kz.danekerscode.jooq.car;

public record CarEntity(
        Long id,
        String brand,
        String model,
        Long madeYear
) {
}
