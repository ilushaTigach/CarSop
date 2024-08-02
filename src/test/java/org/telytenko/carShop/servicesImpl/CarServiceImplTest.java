package test.java.org.telytenko.carShop.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telytenko.carShop.models.Car;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CarServiceImplTest {
    private CarServiceImpl carService;
    private Car car1;
    private Car car2;

    @BeforeEach
    public void setUp() {
        carService = new CarServiceImpl();
        car1 = new Car("Toyota", "Corolla", 2020, 20000, "Good");
        car2 = new Car("Honda", "Civic", 2019, 18000, "Excellent");
    }

    @Test
    public void testAddCar() {
        carService.addCar(car1);
        assertEquals(1, carService.getAllCars().size());
        assertEquals(car1, carService.getCar("Toyota", "Corolla", 2020));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.addCar(car1);
        });
        assertEquals("Автомобиль с маркой Toyota, моделью Corolla, и годом выпуска 2020 уже существует.", exception.getMessage());
    }

    @Test
    public void testGetCar() {
        assertNull(carService.getCar("Toyota", "Corolla", 2020));
        carService.addCar(car1);
        assertEquals(car1, carService.getCar("Toyota", "Corolla", 2020));
    }

    @Test
    public void testUpdateCar() {
        carService.addCar(car1);
        car1.setPrice(21000);
        car1.setCondition("Very Good");
        carService.updateCar(car1);
        Car updatedCar = carService.getCar("Toyota", "Corolla", 2020);
        assertEquals(21000, updatedCar.getPrice());
        assertEquals("Very Good", updatedCar.getCondition());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.updateCar(car2);
        });
        assertEquals("Автомобиль с маркой Honda, моделью Civic, и годом выпуска 2019 не найден.", exception.getMessage());
    }

    @Test
    public void testDeleteCar() {
        carService.addCar(car1);
        assertEquals(1, carService.getAllCars().size());
        carService.deleteCar("Toyota", "Corolla", 2020);
        assertEquals(0, carService.getAllCars().size());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.deleteCar("Toyota", "Corolla", 2020);
        });
        assertEquals("Автомобиль с маркой Toyota, моделью Corolla, и годом выпуска 2020 не найден.", exception.getMessage());
    }

    @Test
    public void testGetAllCars() {
        assertTrue(carService.getAllCars().isEmpty());
        carService.addCar(car1);
        carService.addCar(car2);
        List<Car> allCars = carService.getAllCars();
        assertEquals(2, allCars.size());
        assertTrue(allCars.contains(car1));
        assertTrue(allCars.contains(car2));
    }

    @Test
    public void testBeginTransaction() {
        carService.beginTransaction();
        assertTrue(carService.transactionActive);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            carService.beginTransaction();
        });
        assertEquals("Транзакция уже активна.", exception.getMessage());
    }

    @Test
    public void testCommitTransaction() {
        carService.beginTransaction();
        carService.commitTransaction();
        assertFalse(carService.transactionActive);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            carService.commitTransaction();
        });
        assertEquals("Нет активной транзакции для подтверждения.", exception.getMessage());
    }

    @Test
    public void testRollbackTransaction() {
        carService.beginTransaction();
        carService.rollbackTransaction();
        assertFalse(carService.transactionActive);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            carService.rollbackTransaction();
        });
        assertEquals("Нет активной транзакции для отката.", exception.getMessage());
    }
}