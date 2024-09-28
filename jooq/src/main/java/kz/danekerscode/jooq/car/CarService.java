package kz.danekerscode.jooq.car;

import kz.danekerscode.jooq.tables.Car;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarService {

    private final DSLContext dsl;

    public Long create(CarRequest carRequest) {
        var carRecord = dsl.insertInto(Car.CAR)
                .set(Car.CAR.BRAND, carRequest.brand())
                .set(Car.CAR.MADE_YEAR, carRequest.madeYear())
                .set(Car.CAR.MODEL, carRequest.model())
                .returning(Car.CAR.ID)
                .fetchOne();

        Assert.notNull(carRequest, "Null received from insert query");

        return carRecord
                .getValue(Car.CAR.ID).longValue();
    }

    public void delete(Integer id) {
        dsl.deleteFrom(Car.CAR)
                .where(Car.CAR.ID.eq(ULong.valueOf(id)))
                .execute();
    }

    public List<CarEntity> filter(CarSearchCriteria criteria) {
        var carRecords = dsl.selectFrom(Car.CAR)
                .where("1=1"); // always true condition

        if (criteria.madeYearFrom() != null) {
            carRecords = carRecords.and(Car.CAR.MADE_YEAR.ge(criteria.madeYearFrom()));
        }

        if (criteria.madeYearTo() != null) {
            carRecords = carRecords.and(Car.CAR.MADE_YEAR.le(criteria.madeYearTo()));
        }

        if (criteria.brand() != null) {
            carRecords = carRecords.and(Car.CAR.BRAND.like(criteria.brand()));
        }

        if (criteria.model() != null) {
            carRecords = carRecords.and(Car.CAR.MODEL.like(criteria.model()));
        }

        return carRecords.fetchInto(CarEntity.class);
    }

    public void update(Integer id, CarRequest carRequest) {
        dsl.update(Car.CAR)
                .set(Car.CAR.BRAND, carRequest.brand())
                .set(Car.CAR.MADE_YEAR, carRequest.madeYear())
                .set(Car.CAR.MODEL, carRequest.model())
                .where(Car.CAR.ID.eq(ULong.valueOf(id)))
                .execute();
    }
}
