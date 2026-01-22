import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * JavaFX 3D Creature Visualizer
 * 
 * Renders AI creatures in a 3D environment using JavaFX.
 * Features:
 * - Real-time 3D rendering
 * - Interactive camera controls (mouse drag to rotate)
 * - Multiple creatures in the scene
 * - Dynamic lighting and materials
 * - Procedural mesh generation
 */
public class CreatureVisualizer3D extends Application {

    private double mouseX;
    private double mouseY;
    private double cameraRotX = 0;
    private double cameraRotY = 0;
    private Group sceneRoot;
    private PerspectiveCamera camera;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🌍 Creature Evolution - 3D Visualizer");

        // Create root group
        sceneRoot = new Group();

        // Create camera
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-50);

        // Create scene
        Scene scene = new Scene(sceneRoot, 1200, 800, true);
        scene.setCamera(camera);
        scene.setFill(Color.web("#001a4d")); // Dark blue space background

        // Add light
        addLighting(sceneRoot);

        // Create demo creatures and add to scene
        createDemoCreatures(sceneRoot);

        // Mouse controls for camera rotation
        scene.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;

            cameraRotY += deltaX * 0.5;
            cameraRotX += deltaY * 0.5;

            // Clamp X rotation to prevent flipping
            cameraRotX = Math.max(-85, Math.min(85, cameraRotX));

            updateCameraRotation();

            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        // Keyboard controls for camera movement
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    camera.setTranslateZ(camera.getTranslateZ() + 5);
                    break;
                case S:
                    camera.setTranslateZ(camera.getTranslateZ() - 5);
                    break;
                case A:
                    camera.setTranslateX(camera.getTranslateX() - 5);
                    break;
                case D:
                    camera.setTranslateX(camera.getTranslateX() + 5);
                    break;
                case SPACE:
                    camera.setTranslateY(camera.getTranslateY() - 5);
                    break;
                case SHIFT:
                    camera.setTranslateY(camera.getTranslateY() + 5);
                    break;
                case R:
                    // Reset camera
                    camera.setTranslateX(0);
                    camera.setTranslateY(0);
                    camera.setTranslateZ(-50);
                    cameraRotX = 0;
                    cameraRotY = 0;
                    updateCameraRotation();
                    break;
                default:
                    break;
            }
        });

        scene.getRoot().requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("🎮 3D Visualizer Controls:");
        System.out.println("  Mouse Drag: Rotate camera");
        System.out.println("  W/S: Move forward/backward");
        System.out.println("  A/D: Move left/right");
        System.out.println("  Space/Shift: Move up/down");
        System.out.println("  R: Reset camera");
    }

    /**
     * Create demo creatures using AdvancedAICreature and render them.
     */
    private void createDemoCreatures(Group root) {
        Random rand = new Random();
        List<AdvancedAICreature> creatures = new ArrayList<>();

        // Create diverse creatures
        creatures.add(new AdvancedAICreature(
            AICreature.Locomotion.SWIMMING,
            "Scales", "Blue", "Streamlined",
            8.0, 0.45,
            new String[]{"Gills", "Swimming", "Salt Tolerance"}
        ));

        creatures.add(new AdvancedAICreature(
            AICreature.Locomotion.BIPEDAL,
            "Fur", "Orange", "Upright",
            45.0, 1.6,
            new String[]{"Intelligence", "Tool Use"}
        ));

        creatures.add(new AdvancedAICreature(
            AICreature.Locomotion.FLYING,
            "Feathers", "Red", "Aerodynamic",
            2.5, 0.35,
            new String[]{"Beak", "Sharp Vision"}
        ));

        creatures.add(new AdvancedAICreature(
            AICreature.Locomotion.QUADRUPEDAL,
            "Fur", "Brown", "Robust",
            55.0, 1.3,
            new String[]{"Claws", "Pack Hunter"}
        ));

        creatures.add(new AdvancedAICreature(
            AICreature.Locomotion.CRAWLING,
            "Chitin", "Gray", "Elongated",
            12.0, 0.6,
            new String[]{"Armor Plates", "Burrowing"}
        ));

        // Position creatures in scene
        double spacing = 30;
        for (int i = 0; i < creatures.size(); i++) {
            AdvancedAICreature creature = creatures.get(i);
            Group creatureGroup = createCreature3D(creature);
            
            // Position in a circle
            double angle = (2 * Math.PI * i) / creatures.size();
            double x = spacing * Math.cos(angle);
            double z = spacing * Math.sin(angle);
            
            creatureGroup.setTranslateX(x);
            creatureGroup.setTranslateZ(z);
            
            root.getChildren().add(creatureGroup);
        }

        System.out.println("✓ Created " + creatures.size() + " creatures in 3D scene");
    }

    /**
     * Create a 3D visual representation of a creature.
     */
    private Group createCreature3D(AdvancedAICreature creature) {
        Group creatureGroup = new Group();

        // Generate mesh data
        MeshGenerator.MeshData meshData = MeshGenerator.generateCreatureMesh(
            creature.getLocomotion(),
            creature.getWeight(),
            creature.getHeight()
        );

        // Create main body
        Sphere body = new Sphere(5);
        body.setScaleX(creature.getWeight() / 50.0);
        body.setScaleY(creature.getHeight());
        body.setScaleZ(creature.getWeight() / 50.0);

        // Apply material based on skin type
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(javafxColorFromAWT(meshData.color));
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(10);

        // Apply sexual dimorphism coloring
        if (creature.getGender() == AdvancedAICreature.Gender.FEMALE) {
            material.setDiffuseColor(Color.web("#c03070")); // Exotic magenta
        }

        body.setMaterial(material);

        // Add body parts based on locomotion
        Group bodyParts = new Group();
        bodyParts.getChildren().add(body);

        // Add wings for flying creatures
        if (creature.getLocomotion() == AICreature.Locomotion.FLYING) {
            Cylinder wing1 = new Cylinder(3, 0.5);
            wing1.setTranslateX(-6);
            wing1.setRotate(30);
            wing1.setMaterial(material);

            Cylinder wing2 = new Cylinder(3, 0.5);
            wing2.setTranslateX(6);
            wing2.setRotate(-30);
            wing2.setMaterial(material);

            bodyParts.getChildren().addAll(wing1, wing2);
        }

        // Add tail for some creatures
        if (creature.getLocomotion() == AICreature.Locomotion.QUADRUPEDAL ||
            creature.getLocomotion() == AICreature.Locomotion.SWIMMING) {
            Cylinder tail = new Cylinder(1, 8);
            tail.setTranslateZ(-6);
            tail.setRotationAxis(new Point3D(1, 0, 0));
            tail.setRotate(45);
            tail.setMaterial(material);
            bodyParts.getChildren().add(tail);
        }

        creatureGroup.getChildren().add(bodyParts);

        // Add label below creature
        Sphere label = new Sphere(0.5);
        label.setTranslateY(12);
        PhongMaterial labelMat = new PhongMaterial();
        labelMat.setDiffuseColor(Color.WHITE);
        label.setMaterial(labelMat);
        creatureGroup.getChildren().add(label);

        // Add animation (gentle rotation)
        Rotate rotate = new Rotate(0, Rotate.Y_AXIS);
        creatureGroup.getTransforms().add(rotate);

        // Animate rotation
        javafx.animation.RotateTransition rt = new javafx.animation.RotateTransition(javafx.util.Duration.seconds(8), creatureGroup);
        rt.setByAngle(360);
        rt.setCycleCount(javafx.animation.Animation.INDEFINITE);
        rt.play();

        return creatureGroup;
    }

    /**
     * Convert AWT Color to JavaFX Color.
     */
    private Color javafxColorFromAWT(java.awt.Color awtColor) {
        return Color.color(
            awtColor.getRed() / 255.0,
            awtColor.getGreen() / 255.0,
            awtColor.getBlue() / 255.0
        );
    }

    /**
     * Add lighting to the scene.
     */
    private void addLighting(Group root) {
        javafx.scene.light.PointLight light1 = new javafx.scene.light.PointLight();
        light1.setColor(Color.WHITE);
        light1.setTranslateX(100);
        light1.setTranslateY(100);
        light1.setTranslateZ(-100);

        javafx.scene.light.PointLight light2 = new javafx.scene.light.PointLight();
        light2.setColor(Color.LIGHTBLUE);
        light2.setTranslateX(-100);
        light2.setTranslateY(-100);
        light2.setTranslateZ(100);

        javafx.scene.light.AmbientLight ambientLight = new javafx.scene.light.AmbientLight();
        ambientLight.setColor(Color.web("#404040"));

        root.getChildren().addAll(light1, light2, ambientLight);
    }

    /**
     * Update camera rotation based on mouse input.
     */
    private void updateCameraRotation() {
        camera.getTransforms().clear();
        camera.getTransforms().addAll(
            new Rotate(cameraRotX, Rotate.X_AXIS),
            new Rotate(cameraRotY, Rotate.Y_AXIS),
            new Translate(0, 0, -50)
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
