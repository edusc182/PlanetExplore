import java.awt.Color;

/**
 * GameObject3D: Interface for 3D engine abstraction.
 * 
 * This class simulates a game object in a 3D graphics engine.
 * Can be adapted to work with real engines like Unity, JavaFX 3D, LibGDX, etc.
 * 
 * Each method represents a typical operation you'd perform on 3D models:
 * - setScale(): Resize the model
 * - setColor(): Tint the model
 * - setTexture(): Apply 2D texture mapping
 * - setSubMeshVisible(): Show/hide body parts
 * - setAnimation(): Play locomotion or action animations
 */
public class GameObject3D {
    
    private String name;
    private float[] scale = {1.0f, 1.0f, 1.0f};  // X, Y, Z
    private Color color = Color.WHITE;
    private String currentTexture = "texture_default.png";
    private String currentAnimation = "Idle";
    private boolean[] visibleParts = new boolean[10];  // Up to 10 named parts
    
    /**
     * Constructor: Create a new 3D game object with a given name.
     * @param name Identifier for this game object (e.g., "Creature_42")
     */
    public GameObject3D(String name) {
        this.name = name;
        // All parts visible by default
        for (int i = 0; i < visibleParts.length; i++) {
            visibleParts[i] = true;
        }
    }
    
    /**
     * Set the scale of the 3D model (proportions along X, Y, Z axes).
     * @param x Horizontal scale (girth/width)
     * @param y Vertical scale (height)
     * @param z Depth scale
     */
    public void setScale(float x, float y, float z) {
        this.scale[0] = x;
        this.scale[1] = y;
        this.scale[2] = z;
        System.out.println("     [3D Model] \"" + name + "\" scaled to: X=" + 
                          String.format("%.2f", x) + ", Y=" + String.format("%.2f", y) + 
                          ", Z=" + String.format("%.2f", z));
    }
    
    /**
     * Set the tint/color of the 3D model.
     * @param c The color to apply
     */
    public void setColor(Color c) {
        this.color = c;
        System.out.println("     [3D Model] \"" + name + "\" color set to RGB(" + 
                          c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")");
    }
    
    /**
     * Apply a texture file to the model's surface.
     * @param textureName Path or name of the texture file (e.g., "texture_scales_shiny.png")
     */
    public void setTexture(String textureName) {
        this.currentTexture = textureName;
        System.out.println("     [3D Model] \"" + name + "\" texture loaded: " + textureName);
    }
    
    /**
     * Show or hide a specific named body part (sub-mesh) on the model.
     * Examples: "Wings", "Fins", "Legs", "Tail", "Horns"
     * 
     * @param partName Name of the body part to control
     * @param visible true to show, false to hide
     */
    public void setSubMeshVisible(String partName, boolean visible) {
        if (visible) {
            System.out.println("     [3D Model] \"" + name + "\" enabling body part: " + partName);
        } else {
            System.out.println("     [3D Model] \"" + name + "\" disabling body part: " + partName);
        }
    }
    
    /**
     * Play an animation on this model.
     * Common animations: "Idle", "Walk_Bipedal", "Walk_Quadrupedal", "Swim_Loop", 
     *                   "Fly_Loop", "Crawl_Loop", "Attack", "Damaged"
     * 
     * @param animationName Name of the animation to play
     */
    public void setAnimation(String animationName) {
        this.currentAnimation = animationName;
        System.out.println("     [3D Model] \"" + name + "\" animation playing: " + animationName);
    }
    
    /**
     * Get the current scale of this model.
     * @return float array [X, Y, Z] scale factors
     */
    public float[] getScale() {
        return scale;
    }
    
    /**
     * Get the current color tint.
     * @return Color object
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Get the current texture filename.
     * @return Texture file name
     */
    public String getTexture() {
        return currentTexture;
    }
    
    /**
     * Get the currently playing animation name.
     * @return Animation name
     */
    public String getAnimation() {
        return currentAnimation;
    }
    
    /**
     * Get the name/identifier of this game object.
     * @return Object name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Summary of this game object's visual state.
     */
    @Override
    public String toString() {
        return "GameObject3D{" +
                "name='" + name + '\'' +
                ", scale=[" + String.format("%.2f", scale[0]) + ", " + 
                String.format("%.2f", scale[1]) + ", " + String.format("%.2f", scale[2]) + "]" +
                ", color=" + color +
                ", texture='" + currentTexture + '\'' +
                ", animation='" + currentAnimation + '\'' +
                '}';
    }
}
