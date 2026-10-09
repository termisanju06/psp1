package org.cargadores;

import org.cargadores.records.StatsSnapshot;
import org.cargadores.records.Vehicle;
import org.cargadores.stations.ChargingStation;
import org.cargadores.stations.StationStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        List<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            String plate = String.format(
                    "%04d-%03d",
                    random.nextInt(10000),
                    random.nextInt(1000)
            );
            int kwh = random.nextInt(91) + 10;
            vehicles.add(new Vehicle(plate, kwh));
        }

        StationStats stats = new StationStats();
        ChargingStation station = new ChargingStation(stats);
        try (ExecutorService executor = Executors.newFixedThreadPool(8)) {
            for (Vehicle vehicle : vehicles) {
                executor.submit(() -> station.charge(vehicle));
            }
        }

        StatsSnapshot snapshot = stats.snapshot();
        System.out.println("Registro:");
        System.out.println("Kilovatios totales: " + snapshot.totalKwh());
        System.out.println("Centimos totales: " + snapshot.totalCents());

        long expectedKwh = vehicles.stream()
                .mapToLong(Vehicle::kwh)
                .sum();

        System.out.println("Camparar:");
        System.out.println("¿Coinciden los kWh? " + (expectedKwh == snapshot.totalKwh()));
        System.out.println("¿Coinciden los ingresos? " + (snapshot.totalCents() == snapshot.totalKwh() * 45L));
    }
}
