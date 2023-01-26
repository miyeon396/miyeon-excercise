package effectivejava.item37;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Plant {
    enum LifeCycle { ANNUAL, PERNNIAL, BIENNIAL}

    final String name;
    final LifeCycle lifeCycle;

    public Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public String toString() {
        return name;
    }

    public static void main(String[] args) {
        Set<Plant>[] plantsByLifeCycle = (Set<Plant>[]) new Set[LifeCycle.values().length];

        for (int i = 0 ; i < plantsByLifeCycle.length ; i++) {
            plantsByLifeCycle[i] = new HashSet<>();
        }

        List<Plant> garden = new ArrayList<>();
        garden.add(new Plant("flower1", LifeCycle.ANNUAL));
        garden.add(new Plant("flower2", LifeCycle.PERNNIAL));
        garden.add(new Plant("flower3", LifeCycle.PERNNIAL));
        garden.add(new Plant("flower4", LifeCycle.ANNUAL));
        garden.add(new Plant("flower5", LifeCycle.ANNUAL));
        for (Plant plant : garden) {
            System.out.println(plant.lifeCycle.ordinal());
            plantsByLifeCycle[plant.lifeCycle.ordinal()].add(plant);
        }

        for (int i = 0 ; i < plantsByLifeCycle.length ; i++) {
            System.out.printf("%s : %s%n", LifeCycle.values()[i], plantsByLifeCycle[i]);
        }
    }
}
