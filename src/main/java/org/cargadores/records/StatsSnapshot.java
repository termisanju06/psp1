package org.cargadores.records;

public record StatsSnapshot(long totalKwh, long totalCents) {
    public static class StationStats {
        private long totalKwh = 0;
        private long totalCents = 0;

        public synchronized void register(int kwh) {
            totalKwh += kwh;
            totalCents += kwh * 45L;
        }

        public synchronized StatsSnapshot snapshot() {
            return new StatsSnapshot(totalKwh, totalCents);
        }
    }
}
