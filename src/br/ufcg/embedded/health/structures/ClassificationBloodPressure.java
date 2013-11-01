package br.ufcg.embedded.health.structures;

public enum ClassificationBloodPressure {
    NORMAL(0), LOW_PRESSURE(1), PRE_HYPERTENSION(2), HIGH_PRESSURE_1(3), HIGH_PRESSURE_2(
            4);

    private int value;

    ClassificationBloodPressure(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
