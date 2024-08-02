package org.telytenko.carShop.services;

import org.telytenko.carShop.models.Car;

import java.util.List;

public interface CarService {
    void addCar(Car car);
    Car getCar(String brand, String model, int year);
    void updateCar(Car car);
    void deleteCar(String brand, String model, int year);
    List<Car> getAllCars();
    void beginTransaction();
    void commitTransaction();
    void rollbackTransaction();
}
