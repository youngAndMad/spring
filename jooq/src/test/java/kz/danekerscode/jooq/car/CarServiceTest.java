package kz.danekerscode.jooq.car;

import kz.danekerscode.jooq.EntityNotFoundException;
import kz.danekerscode.jooq.TestContainersConfig;
import kz.danekerscode.jooq.tables.Car;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@JooqTest
@Import(TestContainersConfig.class)
class CarServiceTest {
    private CarService carService;
    @Autowired
    private DSLContext dsl;
    @Autowired
    private MySQLContainer<?> mySQLContainer;

    @BeforeEach
    void setUp() {
        carService = new CarService(dsl);
        dsl.deleteFrom(Car.CAR);
    }

    @Test
    @Order(1)
    void mySQLContainer_FieldInjection_isRunning() {
        assertTrue(mySQLContainer.isRunning());
    }

    @ParameterizedTest
    @MethodSource("createCarRequests")
    void create_NewRecordInserted(CarRequest carRequest) {
        var id = carService.create(carRequest);
        assertNotNull(id);
    }

    @Test
    void findById_RecordDoesNotExists_ThrownEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> carService.findById(1));
    }

    @Test
    void findById_RecordInserted_NoException() {
        var id = carService.create(new CarRequest("Toyota", "Corolla", 2021));
        assertDoesNotThrow(() -> carService.findById(id));
    }

    @Test
    void findById_RecordInserted_NonNullResult() {
        var id = carService.create(new CarRequest("Toyota", "Corolla", 2021));
        assertNotNull(carService.findById(id));
    }

    @Test
    void delete_RecordDoesNotExists_ReturnFalse() {
        assertFalse(carService.delete(1));
    }

    @Test
    void delete_RecordInserted_ReturnTrue() {
        var id = carService.create(new CarRequest("Toyota", "Corolla", 2021));
        assertTrue(carService.delete(id));
    }

    private static Stream<Arguments> createCarRequests() {
        return carRequests().stream().map(Arguments::of);
    }

    private static List<CarRequest> carRequests() {
        return List.of(
                new CarRequest("Toyota", "Corolla", 2021),
                new CarRequest(null, "Camry", 2022),
                new CarRequest("Toyota", null, 2023),
                new CarRequest("VAZ LADA", "2115", 2010),
                new CarRequest("VAZ LADA", "2114", 1999),
                new CarRequest("VAZ LADA", "2110", 2012),
                new CarRequest("NIVA", "4x4", 2016)
        );
    }
}