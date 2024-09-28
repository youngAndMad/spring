package kz.danekerscode.jooq.car;

public record CarSearchCriteria(
        String brand,
        String model,
        Integer madeYearFrom,
        Integer madeYearTo
) {
}
