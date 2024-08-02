package org.telytenko.carShop.servicesImpl;

import org.telytenko.carShop.models.Car;
import org.telytenko.carShop.services.CarService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarServiceImpl implements CarService {
    private List<Car> cars = new ArrayList<>();
    public boolean transactionActive = false;

    @Override
    public void addCar(Car car) {
        if (getCar(car.getBrand(), car.getModel(), car.getYear()) == null) {
            cars.add(car);
        } else {
            throw new IllegalArgumentException("Автомобиль с маркой " + car.getBrand() + ", моделью " + car.getModel() + ", и годом выпуска " + car.getYear() + " уже существует.");
        }
    }

    @Override
    public Car getCar(String brand, String model, int year) {
        Optional<Car> car = cars.stream()
                .filter(c -> c.getBrand().equals(brand) && c.getModel().equals(model) && c.getYear() == year)
                .findFirst();
        return car.orElse(null);
    }

    @Override
    public void updateCar(Car car) {
        Car existingCar = getCar(car.getBrand(), car.getModel(), car.getYear());
        if (existingCar != null) {
            existingCar.setPrice(car.getPrice());
            existingCar.setCondition(car.getCondition());
            existingCar.setAvailable(car.isAvailable());
        } else {
            throw new IllegalArgumentException("Автомобиль с маркой " + car.getBrand() + ", моделью " + car.getModel() + ", и годом выпуска " + car.getYear() + " не найден.");
        }
    }

    @Override
    public void deleteCar(String brand, String model, int year) {
        Car car = getCar(brand, model, year);
        if (car != null) {
            cars.remove(car);
        } else {
            throw new IllegalArgumentException("Автомобиль с маркой " + brand + ", моделью " + model + ", и годом выпуска " + year + " не найден.");
        }
    }

    @Override
    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }

    @Override
    public void beginTransaction() {
        if (transactionActive) {
            throw new IllegalStateException("Транзакция уже активна.");
        }
        transactionActive = true;
    }

    @Override
    public void commitTransaction() {
        if (!transactionActive) {
            throw new IllegalStateException("Нет активной транзакции для подтверждения.");
        }
        transactionActive = false;
    }

    @Override
    public void rollbackTransaction() {
        if (!transactionActive) {
            throw new IllegalStateException("Нет активной транзакции для отката.");
        }
        transactionActive = false;
    }
}
