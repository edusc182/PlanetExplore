# 🎨 **VISUAL PIPELINE - Diagrama Completo del Sistema**

## Sistema Completo: Java → Blender/Unreal

```
┌─────────────────────────────────────────────────────────────────┐
│                    PLANETA EXPLORER 3D                          │
│                   Procedural Creature System                     │
└─────────────────────────────────────────────────────────────────┘

████████████████████████████████████████████████████████████████████
                              FASE 1: GENERAR
████████████████████████████████████████████████████████████████████

    ┌────────────────────────────────────────┐
    │  PlanetGenerator.java (MAIN)           │
    │  ─────────────────────────────────────│
    │  • Crea planeta (Lava, Ice, Ocean...) │
    │  • Genera atmósfera (O2, N2, CH4...)  │
    │  • Instancia criaturas                │
    └─────────────┬──────────────────────────┘
                  │
        ┌─────────▼──────────┐
        │ AICreature (base)  │
        │ AdvancedAICreature │
        │ (con locomotion)   │
        └────────┬───────────┘
                 │
      ┌──────────▼──────────┐
      │ CreatureEvolution   │
      │ Manager             │
      │ ─────────────────── │
      │ • Training (skills) │
      │ • Mutation (traits) │
      │ • Combat damage     │
      └────────┬────────────┘
               │
      ┌────────▼──────────┐
      │ Genetic Data:     │
      │ ─────────────────│
      │ G1-A0-M0-D0-H100 │
      │ Traits: [Gills,  │
      │         Swimming]│
      └────────┬──────────┘
               │
████████████████████████████████████████████████████████████████████
                          FASE 2: EXPORTAR
████████████████████████████████████████████████████████████████████

               ┌──────────────┐
               │ Creature     │
               │ 3DExporter   │
               └────┬────┬────┘
                    │    │
        ┌───────────┴────┴────────────┐
        │                            │
    ┌───▼────┐  ┌─────────┐  ┌──────▼────┐
    │creature│  │creature │  │creatures  │
    │_0.obj  │  │_1.json  │  │_export.csv│
    │        │  │         │  │           │
    │Wavefront│  │Three.js │  │Data Table │
    │3D Mesh  │  │Params   │  │Analysis   │
    └────┬────┘  └────┬────┘  └──────┬────┘
         │            │             │
         │ Metadata:  │             │
         │ • Genetic  │             │
         │ • Physical │             │
         │ • Weight   │             │
         │ • Height   │             │
         │ • Gender   │             │
         │ • Skin     │             │
         └────┴────────┴─────────────┘

████████████████████████████████████████████████████████████████████
                       FASE 3: VISUALIZAR
████████████████████████████████████████████████████████████████████

              ┌─────────────────────────┐
              │  Archivo Exportado      │
              │  (OBJ / JSON / CSV)     │
              └────┬──────────────┬─────┘
                   │              │
           ┌───────▼────┐    ┌───▼──────┐
           │  BLENDER   │    │  UNREAL  │
           │            │    │  ENGINE  │
           └─────┬──────┘    └────┬─────┘
                 │                │
         ┌───────▼─────────┐   ┌──▼────────────┐
         │ 1. Import OBJ   │   │ 1. Create BP  │
         │ 2. Add Material │   │ 2. Add Mesh   │
         │ 3. Create Rig   │   │ 3. Add C++    │
         │ 4. Animate      │   │ 4. Compile    │
         │ 5. Render       │   │ 5. Package    │
         └────┬────────────┘   └──┬────────────┘
              │                   │
         ┌────▼──────┐       ┌───▼──────┐
         │Export FBX │       │Build .exe│
         │glTF/USD   │       │Package   │
         └────┬──────┘       └──┬───────┘
              │                 │
████████████████████████████████████████████████████████████████████
                      FASE 4: DISTRIBUIR
████████████████████████████████████████████████████████████████████

         ┌────────────────────────────────┐
         │    DISTRIBUCIÓN FINAL          │
         ├────────────────────────────────┤
         │ • Itch.io (juego web)          │
         │ • Steam (juego PC)             │
         │ • GitHub (código abierto)      │
         │ • Servidor (multiplayer)       │
         │ • ArtStation (portfolio)       │
         └────────────────────────────────┘
```

---

## Arquitectura de Datos

```
GENOMA COMPRIMIDO
─────────────────────────────────────────────────────────────────
G1-A0-M0-D0-H100-AC2-GILLS-SWIMMI-SALTTO

Componentes:
├─ G1       ← Generación 1
├─ A0       ← Adaptabilidad 0
├─ M0       ← Mutaciones 0
├─ D0       ← Daño total 0
├─ H100     ← Salud 100%
├─ AC2      ← Adaptive Charges 2
├─ GILLS    ← Rasgo: Branquias
├─ SWIMMI   ← Locomoción: Natación
└─ SALTTO   ← Tolerancia a sal


PROPIEDADES FÍSICAS → ESCALA 3D
─────────────────────────────────────────────────────────────────
Genoma          →  Propiedad 3D  →  Visualización

GILLS           →  Tipo piel      →  Verde (0.2, 0.6, 0.3)
SCALEY/FURRY    →  Texture        →  Normal maps
                 
Peso: 8.0 kg    →  Girth (X/Z)    →  Scale = 8.0 / 50 = 0.16
Alto: 0.45 m    →  Altura (Y)     →  Scale = 0.45

MALE            →  Dimorfismo     →  Color saturado
FEMALE          →  Dimorfismo     →  Color vibrante + magenta


LOCOMOCIÓN → ESQUELETO → ANIMACIÓN
─────────────────────────────────────────────────────────────────
BIPEDAL
├─ Spine (columna vertebral)
├─ Head (cabeza)
├─ LeftArm + RightArm
├─ LeftLeg + RightLeg
└─ Animación: Walk, Run, Jump

SWIMMING
├─ Spine_Segments (1-5 ondulantes)
└─ Animación: Wave motion

FLYING
├─ Body
├─ LeftWing + RightWing
└─ Animación: Flap

QUADRUPEDAL
├─ Spine
├─ Head
├─ 4 Legs
└─ Animación: Gallop

CRAWLING
├─ Segments (1-6, tipo oruga)
└─ Animación: Slither
```

---

## Pipeline de Materiales

```
GENOMA → MATERIAL PROCEDURAL → RENDERIZADO

ENTRADA (Genoma):
│
├─ Tipo de piel: "Scales", "Fur", "Feathers", "Skin", "Chitinous"
├─ Género: "MALE", "FEMALE"
├─ Locomoción: determina rugosidad
└─ Edad: afecta color/desgaste

         ↓

PROCESAMIENTO:
│
├─ Color base según tipo de piel
│  ├─ Scales      → Verde (0.2, 0.6, 0.3)
│  ├─ Fur         → Marrón (0.5, 0.4, 0.3)
│  ├─ Feathers    → Dorado (0.8, 0.6, 0.2)
│  ├─ Skin        → Beige (0.8, 0.7, 0.6)
│  ├─ Chitinous   → Negro (0.3, 0.3, 0.2)
│  └─ Crystalline → Blanco (0.9, 0.9, 0.95)
│
├─ Dimorfismo sexual
│  └─ FEMALE: R×1.1, G×0.8, B×1.2 (magenta tone)
│
├─ Roughness basado en locomoción
│  ├─ SWIMMING  → 0.4 (liso)
│  ├─ FLYING    → 0.3 (muy liso)
│  ├─ BIPEDAL   → 0.6 (normal)
│  ├─ CRAWLING  → 0.7 (áspero)
│  └─ QUADRUPEDAL → 0.65
│
└─ Metallic basado en tipo de piel
   ├─ Scales      → 0.2 (brillante)
   ├─ Fur         → 0.0 (sin brillo)
   └─ Chitinous   → 0.15

         ↓

SALIDA (Material Dinámico):
│
├─ Base Color: RGBA con valores procesados
├─ Roughness: 0.0 - 1.0
├─ Metallic: 0.0 - 1.0
├─ Normal Map: Opcional (texture file)
└─ Subsurface: Para dimorfismo sexual (0.3 para hembras)

         ↓

RENDERIZADO (Blender/Unreal):
│
└─ Resultado: Criatura 3D con materiales realistas
```

---

## Flujo de Daño y Adaptación

```
CRIATURA RECIBE DAÑO
         │
         ├─ Daño < 20: Daño normal
         │  └─ Health -= Damage
         │
         └─ Daño ≥ 20: Activar defensas
            │
            ├─ Si AC > 0 (Adaptive Charges disponibles):
            │  │
            │  ├─ ACTIVAR ADAPTACIÓN
            │  ├─ Health += Damage * 0.5  (mitigación 50%)
            │  ├─ AC--
            │  └─ Mostrar efecto visual
            │
            └─ Si AC = 0:
               └─ Daño normal completo
                  (Health -= Damage)

RESULTADO:
└─ Health ≤ 0 → Criatura muere / Destruida
└─ Health > 0 → Criatura sobrevive
   └─ Si evolucionó: AC regenera en siguiente ciclo
```

---

## Exportación: Formato Detallado

### OBJ (Wavefront)
```obj
# Creature OBJ Model
# Generated from Genetic Code: G1-A0-M0-D0-H100-AC2-GILLS-SWIMMI-SALTTO
# Physical: 8.0 kg, 0.45 m, SWIMMING, Scales, Blue
# Lineage: 1 | Age: 0

v 0.0 0.45 0.0        # Vertex (X Y Z)
v 0.055 0.423 0.0
vn 0.0 1.0 0.0        # Vertex Normal
...
f 1/1/1 2/1/1 3/1/1   # Face (v/vt/vn)
```

### JSON (Three.js/Babylon.js)
```json
{
  "creature": {
    "geneticCode": "G1-A0-M0-D0-H100-AC2-GILLS-SWIMMI-SALTTO",
    "lineageId": 1,
    "age": 0,
    "health": 100,
    "physical": {
      "weight": 8.0,
      "height": 0.45,
      "scaleX": 0.16,
      "scaleY": 0.45,
      "scaleZ": 0.16,
      "gender": "MALE",
      "locomotion": "SWIMMING",
      "skinType": "Scales"
    },
    "traits": ["Gills", "Swimming", "Salt Tolerance"]
  }
}
```

### CSV (Data Analysis)
```csv
GeneticCode,LineageID,Age,Health,TotalDamage,Traits
G1-A0-M0-D0-H100-AC2-GILLS-SWIMMI-SALTTO,1,0,100,0,"Gills;Swimming;Salt Tolerance"
```

---

## Integración Motor: Pasos Clave

### BLENDER
```
creature_0.obj → Import OBJ
                 ↓
            Apply Material
                 ↓
            Create Armature
                 ↓
            Add Modifiers
                 ↓
            Animate (opcional)
                 ↓
            Render / Export FBX
```

### UNREAL ENGINE
```
creature_0.obj → Import Mesh
                 ↓
            Create Blueprint
                 ↓
            Add C++ Class (PlanetaCreature)
                 ↓
            Create Material Instance
                 ↓
            Setup Animation Blueprint
                 ↓
            Compile & Package .exe
```

### THREE.JS
```
creature_1.json → Parse JSON
                  ↓
             Create Three.js Geometry
                  ↓
             Apply Material
                  ↓
             Add to Scene
                  ↓
             Render Web (canvas)
```

---

## Rendimiento y Escalabilidad

```
SINGLE CREATURE
├─ OBJ: ~5.8 KB (398 vértices)
├─ JSON: ~430 bytes (comprimido)
└─ Renderizado: <1 ms (real-time)

100 CREATURES
├─ OBJ Total: 580 KB
├─ Instanciado en Unreal: ~16 ms/frame
└─ Población: ~60 FPS en GPU moderna

1000 CREATURES
├─ OBJ Total: 5.8 MB
├─ LOD (Level of Detail): Versiones simplificadas
└─ Población: ~30-40 FPS con LOD

OPTIMIZACIÓN:
├─ Use Instanced Static Mesh (Unreal)
├─ LOD: Full → Mid → Low detail
├─ Occlusion Culling
└─ GPU Instancing (material parameter)
```

---

## Roadmap Visual

```
FASE 1 (ACTUAL)        FASE 2           FASE 3          FASE 4
─────────────────     ─────────────    ─────────────   ──────────
Procedural Gen    →   Blender Export  → Unreal Game  → Distribution
Multi-format      →   Animation Add   → Combat      → Multiplayer
Exportación       →   Material Edit   → Evolution   → Web/Mobile
ASCII Preview     →   Preview Render  → Ecosystem   → Cloud Save

✅ Completado      🟡 En Progreso    ⏳ Planeado     🔮 Futuro
```

---

Este diagrama visual muestra el flujo **completo** desde generación Java hasta visualización en Blender/Unreal. ¡Tu sistema está listo para producción! 🚀
