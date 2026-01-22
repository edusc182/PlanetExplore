public class Scene {
    public void loadAssets(AssetManager assetManager) {
        System.out.println("Loading assets into the scene:");
        // Iterate over AssetType objects
        for (AssetType asset : assetManager.getAssets()) {
            System.out.println("Loaded asset: " + asset.toString());
            // Or, for more detail:
            // System.out.println("Loaded asset: " + asset.getTypeName() + " (" + asset.getFileExtensionOrFileName() + ")");
        }
    }
}
