import java.util.Random;

public class EventEngine {
    /**
     * Trigger a PlanetCrisis: returns a 2-element array {newType, newAtmosphere}
     * based on the current environment. Uses a deterministic mapping with some
     * randomness to create dramatic changes.
     */
    public static String[] triggerCrisis(Random random, String currentType, String currentAtmosphere) {
        // pick a random event that changes the environment significantly
        String newType = currentType;
        String newAtmosphere = currentAtmosphere;

        int choice = random.nextInt(5);
        switch (choice) {
            case 0:
                newType = "Ice";
                newAtmosphere = "Methane";
                break;
            case 1:
                newType = "Lava";
                newAtmosphere = "Carbon monoxide";
                break;
            case 2:
                newType = "Desert";
                newAtmosphere = "Nitrogen";
                break;
            case 3:
                newType = "Ocean";
                newAtmosphere = "Oxygen-rich";
                break;
            case 4:
                newType = "Forest";
                newAtmosphere = "Oxygen-rich";
                break;
        }

        System.out.println("!! PLANET CRISIS: environment shifting to " + newType + " (" + newAtmosphere + ") !!");
        return new String[]{newType, newAtmosphere};
    }
}
