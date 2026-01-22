import java.util.List;

public class AssetManager {
    private final List<AssetType> assets;

    public AssetManager(List<AssetType> assets) {
        this.assets = assets;
    }

    public List<AssetType> getAssets() {
        return this.assets;
    }

    public void addAsset(AssetType asset) {
        this.assets.add(asset);
    }
}
