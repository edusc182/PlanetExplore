# ⚙️ **INTEGRACIÓN UNREAL ENGINE 5 - Guía Completa**

## 🚀 Parte 1: Setup Inicial

### Requisitos
- **Unreal Engine 5.3+** (descarga desde Epic Games Launcher)
- **Visual Studio 2022 Community** (C++ development tools)
- **Python 3.x** (bundled con Unreal)
- Archivos generados: `creature_0.obj`, `creature_1.json`

### 1.1 Crear Proyecto
```bash
# Opción A: Desde Epic Games Launcher
Epic Games Launcher → Unreal Engine → Create → 
  - Project Type: Games
  - Template: Third Person (C++)
  - Project Name: PlanetaExplorer
  - Location: C:\UnrealProjects\

# Opción B: Desde terminal
"C:\Program Files\Epic Games\UE_5.3\Engine\Build\BatchFiles\RunUAT.bat" 
  BuildProject -Project="C:\UnrealProjects\PlanetaExplorer\PlanetaExplorer.uproject" 
  -TargetPlatforms=Win64 -Build
```

### 1.2 Copiar Archivos
```
C:\UnrealProjects\PlanetaExplorer\
├── Content/
│   ├── Creatures/
│   │   ├── creature_0.obj        ← Copiar aquí
│   │   ├── creature_1.json       ← Copiar aquí
│   │   └── Meshes/               ← Importados
│   │       ├── Creature_0
│   │       ├── Creature_1
│   │       └── ...
│   └── Materials/
│       ├── M_Creature_Base.uasset
│       └── M_Creature_Procedural.uasset
├── Source/
│   ├── PlanetaExplorer/
│   │   ├── Creatures/
│   │   │   ├── PlanetaCreature.h      ← Pegable aquí
│   │   │   ├── PlanetaCreature.cpp
│   │   │   ├── CreatureManager.h
│   │   │   └── CreatureManager.cpp
│   │   └── PlanetaExplorer.Build.cs
│   └── PlanetaExplorerEditor.Target.cs
└── PlanetaExplorer.uproject
```

---

## 📥 Parte 2: Importar Mallas OBJ

### 2.1 Método Manual (UI)
```
1. Unreal Editor → Content Browser
2. Click derecho → Import
3. Selecciona creature_0.obj
4. Opciones de importación:
   ✓ Import Mesh: ON
   ✓ Create Physics Asset: ON
   ✓ Import Material: ON
   ✓ Normal Import Method: Import Normals and Tangents
5. Click "Import" o "Import All"
```

**Resultado:** Aparecerá en Content/Creatures/Meshes/Creature_0_0

### 2.2 Método Automático (Python Script en Unreal)
```python
# Script: Content/Python/import_creatures.py

import unreal
import os

def import_creature_mesh(obj_path: str, asset_name: str) -> None:
    """Importa malla OBJ con configuración automática"""
    
    # Configurar opciones de importación
    task = unreal.AssetImportTask()
    task.filename = obj_path
    task.destination_path = "/Game/Creatures/Meshes"
    task.destination_name = asset_name
    task.replace_existing = True
    task.automated = True
    
    # Opciones específicas para OBJ
    options = unreal.FbxImportUI()
    options.set_editor_property('create_physics_asset', True)
    options.set_editor_property('import_material', True)
    options.set_editor_property('import_normals', True)
    options.set_editor_property('import_tangents', True)
    
    task.options = options
    
    # Ejecutar importación
    unreal.AssetToolsHelpers.get_asset_tools().import_asset_tasks([task])
    
    print(f"✅ Importado: {asset_name}")

# Usar:
import_creature_mesh("C:/creature_0.obj", "Creature_0")
```

**Ejecutar en Unreal:**
```
1. Tools → Python Console
2. exec(open('C:/Path/to/import_creatures.py').read())
```

---

## 🎨 Parte 3: Crear Material Procedural

### 3.1 Material Base (Blueprint Material)
```
1. Content Browser → Crear → Material
2. Nombre: M_Creature_Base
3. En Material Editor:
   
   [Main Material Node]
   ├── Base Color: [Your Base Color]
   ├── Roughness: 0.6
   ├── Metallic: 0.1
   ├── Subsurface Weight: 0.3
   └── [Connect to Pixel Shader]

4. Crear parámetros escalares/vectoriales:
   - "SkinColor" (Vector3)
   - "Roughness" (Scalar 0.0-1.0)
   - "Metallic" (Scalar 0.0-1.0)

5. Guardar
```

### 3.2 Material Dinámico (C++ / Blueprint)

**Opción A: Blueprint (Más fácil)**
```
1. Content → Crear → Blueprint
2. Parent Class: Character
3. Nombre: BP_CreatureCharacter
4. En Viewport:
   ├── Mesh (Skeletal Mesh)
   ├── Agregar Component: Skeletal Mesh
   └── Set Mesh: Creature_0_Skeleton

5. En Construction Script:
   Crear Material Instance Dynamic
   └── Aplicar a Mesh

6. Guardar
```

**Opción B: C++ (Más control)**

Ver archivo: `UnrealEngine_CreatureSystem.cpp` → `ApplyProceduralMaterial()`

---

## 🦴 Parte 4: Rigging & Esqueletos

### 4.1 Crear Esqueleto (Skeleton Asset)
```
1. Content → Creature_0 mesh
2. Click derecho → Create Skeleton
3. Nombre: Creature_0_Skeleton
4. Aceptar
```

### 4.2 Crear Rig Manual
```
1. Content → Crear → Skeletal Mesh
2. Nombre: Creature_0_Rigged
3. En Skeleton Editor:
   └── Agregar huesos según locomoción:
   
   BIPEDAL:
   ├── Root
   ├── Spine
   │  ├── Head
   │  ├── LeftArm
   │  └── RightArm
   ├── LeftLeg
   └── RightLeg
   
   SWIMMING:
   ├── Root
   └── Spine_Segments (1-5)
   
   FLYING:
   ├── Root
   ├── Body
   ├── LeftWing
   └── RightWing
```

---

## 🎬 Parte 5: Animaciones Procedurales

### 5.1 Crear Montaje de Animación
```
1. Content → Crear → Animation Montage
2. Nombre: AM_Creature_Idle
3. En Animation Editor:
   ├── Drag & drop Animation Sequence
   ├── Set Duration: 2.0s
   └── Loop: ON
```

### 5.2 Estado Machine (Locomotión)
```
1. Content → Crear → Animation Blueprint
2. Nombre: ABP_Creature
3. Parent Class: APlanetaCreature (o tu clase)

4. En Anim Graph:
   ┌─────────────┐
   │   Idle      │
   └──────┬──────┘
          │
   ┌──────▼──────┐
   │  Moving     │
   └──────┬──────┘
          │
   ┌──────▼──────────┐
   │  Attack/Special │
   └─────────────────┘

5. En Blueprint:
   Event Locomotion Changed:
   └── Switch on Locomotion Type
       ├── Bipedal: Play Montage ABP_Bipedal_Walk
       ├── Swimming: Play Montage ABP_Swim
       ├── Flying: Play Montage ABP_Fly
       └── ...
```

---

## ⚔️ Parte 6: Sistema de Combate

### 6.1 Gestión de Daño
```cpp
// En tu Character Class
void ACreatureCharacter::OnDamageReceived(float Damage, FVector HitLocation)
{
    APlanetaCreature* Creature = Cast<APlanetaCreature>(this);
    if (Creature)
    {
        Creature->TakeDamage(Damage, HitLocation);
        
        // Visualización
        if (Creature->GeneticCode.Health <= 50.0f)
        {
            PlayDamagedMaterial();
        }
    }
}

void ACreatureCharacter::PlayDamagedMaterial()
{
    DynamicMaterial->SetVectorParameterValue(
        FName("DamageColor"),
        FLinearColor::Red
    );
    // Transición suave a color normal
}
```

### 6.2 Animación de Defensa (Adaptación)
```cpp
void APlanetaCreature::PlayAdaptiveDefense()
{
    if (GeneticCode.AdaptiveCharges > 0)
    {
        // Play shield/dodge animation
        PlayCombatAnimation(TEXT("Dodge"));
        
        // Mostrar efecto visual
        SpawnAdaptationEffect(GetActorLocation());
        
        GeneticCode.AdaptiveCharges--;
    }
}
```

---

## 🌍 Parte 7: Gestión de Criaturas (CreatureManager)

### 7.1 Crear Manager
```cpp
// CreatureManager.h
UCLASS()
class PLANETA_API ACreatureManager : public AActor
{
    GENERATED_BODY()

public:
    UPROPERTY(BlueprintReadWrite)
    TArray<APlanetaCreature*> ActiveCreatures;

    UFUNCTION(BlueprintCallable)
    void SpawnCreatureFromJSON(const FString& JSONPath);

    UFUNCTION(BlueprintCallable)
    void UpdateAllCreatures(float DeltaTime);

    UFUNCTION(BlueprintCallable)
    void EvolvePrimaryCreature(const TArray<FString>& AvailableTraits);
};
```

### 7.2 Spawning desde JSON
```cpp
void ACreatureManager::SpawnCreatureFromJSON(const FString& JSONPath)
{
    // 1. Parsear JSON
    FString JsonContent;
    FFileHelper::LoadFileToString(JsonContent, *JSONPath);
    
    TSharedPtr<FJsonObject> JsonObject;
    TSharedRef<TJsonReader<>> Reader = TJsonReaderFactory<>::Create(JsonContent);
    FJsonSerializer::Deserialize(Reader, JsonObject);

    // 2. Extraer datos
    TSharedPtr<FJsonObject> CreatureData = JsonObject->GetObjectField(TEXT("creature"));
    FString GeneticCode = CreatureData->GetStringField(TEXT("geneticCode"));
    float Weight = CreatureData->GetObjectField(TEXT("physical"))->GetNumberField(TEXT("weight"));
    
    // 3. Crear criatura
    FActorSpawnParameters SpawnParams;
    APlanetaCreature* NewCreature = GetWorld()->SpawnActor<APlanetaCreature>(
        APlanetaCreature::StaticClass(),
        FVector(0, 0, 100),
        FRotator::ZeroRotator,
        SpawnParams
    );

    // 4. Configurar propiedades
    NewCreature->GeneticCode = ParseGeneticCode(GeneticCode);
    NewCreature->PhysicalProperties.Weight = Weight;
    
    ActiveCreatures.Add(NewCreature);
}
```

---

## 📊 Parte 8: UI de Estadísticas

### 8.1 Widget Genética
```cpp
// CreatureStatsWidget.h
UCLASS()
class PLANETA_API UCreatureStatsWidget : public UUserWidget
{
    GENERATED_BODY()

protected:
    UPROPERTY(BlueprintReadWrite, meta = (BindWidget))
    class UTextBlock* GeneticCodeText;
    
    UPROPERTY(BlueprintReadWrite, meta = (BindWidget))
    class UProgressBar* HealthBar;
    
    UPROPERTY(BlueprintReadWrite, meta = (BindWidget))
    class UTextBlock* TraitsText;

    UPROPERTY()
    APlanetaCreature* TargetCreature;

    virtual void NativeConstruct() override;
    virtual void NativeTick(const FGeometry& MyGeometry, float InDeltaTime) override;

public:
    UFUNCTION(BlueprintCallable)
    void SetTargetCreature(APlanetaCreature* Creature);

private:
    void UpdateUI();
};
```

### 8.2 Display en Blueprint
```
1. Content → Crear → Widget Blueprint
2. Nombre: WBP_CreatureHUD

3. Canvas Panel
├── Genetic Code (TextBlock)
├── Health Bar (ProgressBar)
├── Traits List (VerticalBox)
└── Adaptation Status (Image + Text)

4. Graph:
   Event Tick:
   └── Call UpdateCreatureStats()
```

---

## 🔄 Parte 9: Loop de Simulación

### 9.1 Game Mode
```cpp
// PlanetaGameMode.cpp
void APlanetaGameMode::Tick(float DeltaTime)
{
    Super::Tick(DeltaTime);
    
    if (CreatureManager)
    {
        // Actualizar todas las criaturas
        CreatureManager->UpdateAllCreatures(DeltaTime);
        
        // Cada 5 segundos, evaluar mutaciones
        EvolutionTimer += DeltaTime;
        if (EvolutionTimer >= 5.0f)
        {
            CreatureManager->EvolvePrimaryCreature(AvailableTraits);
            EvolutionTimer = 0.0f;
        }
    }
}
```

---

## 🚀 Parte 10: Testing

### 10.1 Prueba Rápida
```
1. Abre PlanetaExplorer en Unreal Editor
2. Content Browser → Creatures → BP_CreatureCharacter
3. Arrastra a Viewport
4. Play (Alt + P)
5. Verifica:
   ✓ Criatura se mueve
   ✓ Animaciones se reproducen
   ✓ UI muestra estadísticas
   ✓ Al presionar F, toma daño
```

### 10.2 Debugging
```cpp
// Agregar esto al BeginPlay
if (UWorld* World = GetWorld())
{
    World->GetFirstPlayerController()->ClientMessage(
        FString::Printf(TEXT("Criatura: %s | Salud: %.0f"), 
            *GetGeneticCodeString(), 
            GeneticCode.Health)
    );
}
```

---

## 📦 Parte 11: Packaging Final

### 11.1 Build Production
```
1. File → Package Project → Windows (64-bit)
2. Selecciona destino: C:\PlanetaExplorer\Build\
3. Espera a que compile (5-10 minutos)
4. Resultado: PlanetaExplorer.exe
```

### 11.2 Distribuir
```
Carpetas necesarias:
├── PlanetaExplorer.exe
├── Binaries/
├── Content/
│   ├── Creatures/
│   │   └── Meshes/
│   │       └── Creature_*.uasset
│   └── Materials/
│       └── M_Creature_*.uasset
└── Plugins/
    └── [Any required plugins]
```

---

## 💡 Tips Avanzados

### Procedural Mesh Generation
```cpp
void APlanetaCreature::GenerateProceduralMesh()
{
    // Usar Procedural Mesh Component
    ProceduralMesh->CreateMeshSection_LinearColor(
        0,
        Vertices,
        Triangles,
        Normals,
        UV0,
        VertexColors,
        Tangents,
        true
    );
}
```

### Real-time Evolution
```cpp
void ACreatureManager::StartEvolutionSimulation(int32 GenerationCount)
{
    for (int32 Gen = 0; Gen < GenerationCount; Gen++)
    {
        for (APlanetaCreature* Creature : ActiveCreatures)
        {
            // Aplicar presión ambiental
            Creature->ApplyMutation(EnvironmentalTraits);
            
            // Guardar snapshot
            SaveCreatureSnapshot(Creature, Gen);
        }
        
        // Esperar frame
        GetWorld()->GetTimerManager().SetTimerForNextTick(
            [this]() { /* continue */ }
        );
    }
}
```

---

**¡Unreal Engine está listo para tus criaturas!** 🎮✨
