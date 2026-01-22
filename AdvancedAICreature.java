import java.util.Random;
import java.util.Arrays;

/**
 * Advanced AI Creature extending AICreature with physical attributes,
 * gender/locomotion traits, and sexual dimorphism.
 * Maintains full compatibility with existing AICreature API.
 */
public class AdvancedAICreature extends AICreature {
    
    // Gender enum for strict type definitions
    public enum Gender { MALE, FEMALE }

    // Physical attributes
    private Gender gender;
    // Note: locomotion field is inherited from AICreature
    private String skinType;        // Skin, scales, feathers, chitin
    private String color;
    private String shape;           // Body shape
    private double weight;          // kg
    private double height;          // meters
    private Random random;

    /**
     * Create an AdvancedAICreature with full physical attributes.
     * Applies sexual dimorphism rules.
     * 
     * @param startLocomotion Movement type
     * @param skinType Type of skin/covering
     * @param baseColor Base color
     * @param shape Body shape
     * @param baseWeight Base weight in kg
     * @param baseHeight Base height in meters
     * @param initialSkills Skill/trait array
     */
    public AdvancedAICreature(AICreature.Locomotion startLocomotion, String skinType, String baseColor, 
                              String shape, double baseWeight, double baseHeight, String[] initialSkills) {
        // Initialize parent with skills
        super(initialSkills);
        this.random = new Random();
        
        // Assign random gender
        this.gender = random.nextBoolean() ? Gender.MALE : Gender.FEMALE;
        
        // Apply sexual dimorphism logic
        if (this.gender == Gender.FEMALE) {
            this.weight = baseWeight * 0.80;      // Female 20% lighter
            this.height = baseHeight * 0.85;      // Female 15% smaller
            this.color = "Exotic " + baseColor;   // Exotic coloration
        } else {
            this.weight = baseWeight;
            this.height = baseHeight;
            this.color = baseColor;
        }

        this.locomotion = startLocomotion;
        this.skinType = skinType;
        this.shape = shape;
    }

    // Getters for physical attributes
    public Gender getGender() { return gender; }
    public String getSkinType() { return skinType; }
    public String getColor() { return color; }
    public String getShape() { return shape; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }

    // Setters for evolution/adaptation
    public void setGender(Gender gender) { this.gender = gender; }
    public void setSkinType(String skinType) { this.skinType = skinType; }
    public void setColor(String color) { this.color = color; }
    public void setShape(String shape) { this.shape = shape; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(double height) { this.height = height; }

    /**
     * Compute fitness bonus based on physical attributes aligned with environment.
     * Example: High weight + Quadrupedal + Lava terrain = better fitness.
     */
    public int computePhysicalFitness(String planetType) {
        int fitness = 0;

        // Locomotion bonus
        if (locomotion == Locomotion.SWIMMING && (planetType.equals("Ocean") || planetType.equals("Swamp"))) {
            fitness += 10;
        }
        if (locomotion == Locomotion.FLYING && planetType.equals("Mountain")) {
            fitness += 10;
        }
        if ((locomotion == Locomotion.QUADRUPEDAL || locomotion == Locomotion.BIPEDAL) && 
            (planetType.equals("Desert") || planetType.equals("Forest"))) {
            fitness += 5;
        }

        // Skin type bonus
        if (skinType.contains("scale") && planetType.equals("Lava")) {
            fitness += 5;
        }
        if (skinType.contains("fur") && planetType.equals("Ice")) {
            fitness += 5;
        }

        // Weight/height balance
        if (weight > 50 && planetType.equals("Lava")) {
            fitness += 3;  // Heavier creatures better on hot terrain
        }
        if (height > 2.0 && planetType.equals("Forest")) {
            fitness += 3;  // Taller reaches more canopy
        }

        return fitness;
    }

    /**
     * Compact representation showing gender, locomotion, physical traits.
     */
    public String getPhysicalCode() {
        String genderCode = gender == Gender.MALE ? "M" : "F";
        String locomotionCode = locomotion.toString().substring(0, 3).toUpperCase();
        String skinCode = skinType.substring(0, 3).toUpperCase();
        return String.format("%s-%s-%s-%.1f-%.2f", genderCode, locomotionCode, skinCode, weight, height);
    }

    @Override
    public String toString() {
        String genderStr = gender.toString().substring(0, 1);
        String skillsStr = String.join(",", Arrays.copyOf(getAttributes(), Math.min(3, getAttributes().length)));
        return String.format("[%s-G%d] %s | Mov:%s | Skin:%s | Color:%s | %.1fkg/%.2fm | Skills:{%s}",
                genderStr, getLineageId(), getAge(), 
                locomotion, skinType, color, weight, height, skillsStr);
    }
}
