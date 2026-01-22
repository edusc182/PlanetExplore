public class AssetType {
    private final String typeName; // e.g., "texture", "model"
    private final String fileExtension; // e.g., ".png", ".obj"

    public AssetType(String typeName, String fileExtension) {
        this.typeName = typeName;
        this.fileExtension = fileExtension;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    @Override
    public String toString() {
        return "AssetType{" +
               "typeName='" + typeName + '\'' +
               ", fileExtension='" + fileExtension + '\'' +
               '}';
    }
}
