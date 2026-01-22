import java.awt.Color;

/**
 * MeshGenerator: Creates 3D procedural mesh geometry for creatures.
 * 
 * Generates basic geometric shapes that can be combined to form creature models:
 * - Spheres (bodies, heads)
 * - Cylinders (limbs, necks)
 * - Cones (tails, beaks)
 * - Custom shapes based on creature locomotion
 */
public class MeshGenerator {

    /**
     * Generate a simple sphere mesh (for creature bodies).
     * 
     * @param radius Radius of the sphere
     * @param segments Number of segments (higher = smoother)
     * @return Vertices as float array [x1,y1,z1,x2,y2,z2,...]
     */
    public static float[] generateSphereMesh(float radius, int segments) {
        float[] vertices = new float[segments * segments * 3];
        int index = 0;

        for (int i = 0; i < segments; i++) {
            float lat0 = (float) Math.PI * (i / (float) segments - 0.5f);
            float lat1 = (float) Math.PI * ((i + 1) / (float) segments - 0.5f);

            float cosLat0 = (float) Math.cos(lat0);
            float sinLat0 = (float) Math.sin(lat0);
            float cosLat1 = (float) Math.cos(lat1);
            float sinLat1 = (float) Math.sin(lat1);

            for (int j = 0; j < segments; j++) {
                float lng0 = 2 * (float) Math.PI * (j / (float) segments);
                float lng1 = 2 * (float) Math.PI * ((j + 1) / (float) segments);

                float cosLng0 = (float) Math.cos(lng0);
                float sinLng0 = (float) Math.sin(lng0);
                float cosLng1 = (float) Math.cos(lng1);
                float sinLng1 = (float) Math.sin(lng1);

                // First triangle vertex
                if (index + 2 < vertices.length) {
                    vertices[index++] = radius * cosLat0 * cosLng0;
                    vertices[index++] = radius * sinLat0;
                    vertices[index++] = radius * cosLat0 * sinLng0;
                }
            }
        }

        return vertices;
    }

    /**
     * Generate a cylinder mesh (for limbs, necks).
     * 
     * @param radius Radius of the cylinder
     * @param height Height of the cylinder
     * @param segments Number of segments around the cylinder
     * @return Vertices as float array
     */
    public static float[] generateCylinderMesh(float radius, float height, int segments) {
        float[] vertices = new float[(segments + 1) * 6 * 3];
        int index = 0;

        // Top and bottom rings
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float x = radius * (float) Math.cos(angle);
            float z = radius * (float) Math.sin(angle);

            // Top vertex
            vertices[index++] = x;
            vertices[index++] = height / 2;
            vertices[index++] = z;

            // Bottom vertex
            vertices[index++] = x;
            vertices[index++] = -height / 2;
            vertices[index++] = z;
        }

        return vertices;
    }

    /**
     * Generate a cone mesh (for tails, beaks, spines).
     * 
     * @param baseRadius Base radius of the cone
     * @param height Height of the cone
     * @param segments Number of segments around the base
     * @return Vertices as float array
     */
    public static float[] generateConeMesh(float baseRadius, float height, int segments) {
        float[] vertices = new float[(segments + 2) * 3];
        int index = 0;

        // Apex (tip) of the cone
        vertices[index++] = 0;
        vertices[index++] = height;
        vertices[index++] = 0;

        // Base ring
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            vertices[index++] = baseRadius * (float) Math.cos(angle);
            vertices[index++] = 0;
            vertices[index++] = baseRadius * (float) Math.sin(angle);
        }

        return vertices;
    }

    /**
     * Generate mesh data based on creature locomotion type.
     * This creates a basic body shape appropriate for the movement style.
     */
    public static MeshData generateCreatureMesh(AICreature.Locomotion locomotion, 
                                                 double weight, double height) {
        MeshData mesh = new MeshData();
        
        // Scale factors based on creature size
        float bodyScale = (float) (height / 1.0);
        float girthScale = (float) (weight / 50.0);

        switch (locomotion) {
            case SWIMMING:
                // Streamlined torpedo shape
                mesh.vertices = generateSphereMesh(bodyScale * girthScale * 0.3f, 12);
                mesh.color = new Color(0, 150, 200);
                mesh.shapeType = "Streamlined";
                break;

            case BIPEDAL:
                // Upright humanoid (sphere body + cylinders for limbs)
                float bodyRadius = bodyScale * girthScale * 0.4f;
                mesh.vertices = generateSphereMesh(bodyRadius, 12);
                mesh.color = new Color(139, 69, 19);
                mesh.shapeType = "Bipedal";
                break;

            case QUADRUPEDAL:
                // Four-legged beast (elongated sphere)
                mesh.vertices = generateCylinderMesh(bodyScale * girthScale * 0.5f, bodyScale, 16);
                mesh.color = new Color(100, 100, 100);
                mesh.shapeType = "Quadrupedal";
                break;

            case FLYING:
                // Lightweight with wings
                mesh.vertices = generateSphereMesh(bodyScale * girthScale * 0.25f, 12);
                mesh.color = new Color(255, 100, 0);
                mesh.shapeType = "Flying";
                break;

            case CRAWLING:
                // Flat, elongated body
                float flatRadius = bodyScale * girthScale * 0.35f;
                mesh.vertices = generateCylinderMesh(flatRadius, bodyScale * 0.7f, 12);
                mesh.color = new Color(80, 80, 80);
                mesh.shapeType = "Crawling";
                break;

            default:
                mesh.vertices = generateSphereMesh(bodyScale, 12);
                mesh.color = Color.GRAY;
                mesh.shapeType = "Generic";
        }

        return mesh;
    }

    /**
     * Container for mesh data.
     */
    public static class MeshData {
        public float[] vertices;
        public Color color;
        public String shapeType;

        @Override
        public String toString() {
            return "MeshData{" +
                    "vertices.length=" + (vertices != null ? vertices.length : 0) +
                    ", color=" + color +
                    ", shapeType='" + shapeType + '\'' +
                    '}';
        }
    }
}
