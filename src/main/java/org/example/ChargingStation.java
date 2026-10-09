package org.example;

import java.util.concurrent.Semaphore;

public class ChargingStation {

    private final Semaphore chargers = new Semaphore(4);
    private final StationStats stats;

    public ChargingStation(StationStats stats) {
        this.stats = stats;
    }

    public ChargeResult charge(Vehicle vehicle) {
        boolean acquired = false;

        try {
            chargers.acquire();
            acquired = true;

            System.out.println(vehicle.plate() + " cargando...");

            Thread.sleep(vehicle.kwh() * 20L);

            stats.register(vehicle.kwh());

            ChargeResult result = new ChargeResult(vehicle.plate(), vehicle.kwh(), vehicle.kwh() * 45L);

            System.out.println(vehicle.plate() + " cargado y lo libera");

            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);

        } finally {
            if (acquired) {
                chargers.release();
            }
        }
    }
}
