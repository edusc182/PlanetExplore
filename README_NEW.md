# 🌍 PlanetExplore - AI Creature Evolution System

**Procedural generation meets 3D visualization. Create unique alien creatures and watch them evolve in real-time.**

## ✨ Features

- **🧬 Genetic Evolution**: Creatures mutate and adapt based on planetary environments
- **🎨 Multi-Engine Visualization**: Export to Blender, Unreal Engine, Three.js, or any OBJ-compatible tool
- **⚔️ Combat System**: Adaptive damage mitigation and real-time evolution under stress
- **📊 Procedural Generation**: 5 planet types × 4 atmospheres = infinite creature combinations
- **🚀 Production-Ready**: Fully documented, tested, and ready for integration

## 🚀 Quick Start (30 minutes)

### 1. Generate Creatures
```bash
cd PLantet
javac Creature3DDemo.java
java Creature3DDemo
```

### 2. Visualize in Blender (15 min)
```bash
blender --python blender_creature_importer.py
# Opens Blender with creature_0.obj automatically imported
```

### 3. Or in Unreal Engine (20 min)
```
1. Create new C++ project (Third Person)
2. Content Browser → Import → creature_0.obj
3. Copy UnrealEngine_CreatureSystem.cpp to Source/
4. Compile and Play
```

## 📚 Documentation

| Document | Purpose | Time |
|----------|---------|------|
| **QUICK_START.md** | Get running immediately | 30 min |
| **VISUAL_INTEGRATION_GUIDE.md** | Integrate 4 different engines | 2 hours |
| **UNREAL_ENGINE_SETUP.md** | Complete Unreal guide with C++ code | 3-6 hours |
| **VISUAL_PIPELINE.md** | Architecture diagrams and flow | Reference |
| **COMPLETION_SUMMARY.md** | Project overview | 10 min |

## 🎮 Supported Engines

- ✅ **Blender 4.0+** - Full 3D modeling with automatic rigging (5 locomotion types)
- ✅ **Unreal Engine 5.3+** - Complete C++ integration with combat system
- ✅ **Three.js** - Web-based visualization with JSON parameters
- ✅ **Unity** - Via FBX export from Blender
- ✅ **Any OBJ-compatible tool** - Wavefront mesh format universal support

## 📦 Export Formats

- **OBJ** (Wavefront) - Universal 3D mesh with metadata
- **JSON** - Three.js parameters with full genetic data
- **CSV** - Tabular format for data analysis
- **TXT** - Reference manifests

## 🏗️ Architecture

```
Java Generation  →  Mesh Export  →  Engine Integration  →  Distribution
     ↓                  ↓                    ↓                  ↓
PlanetGenerator   MeshGenerator      Blender/Unreal      GitHub/Steam/Web
```

## 🔬 Technical Details

### Genetic Code Format
```
G1-A0-M0-D0-H100-AC2-GILLS-SWIMMI-SALTTO
├─ Generation, Adaptability, Mutations
├─ Health, Adaptive Charges
└─ Traits (Gills, Swimming capability, Salt tolerance)
```

### Visual Mapping
- **Weight** (kg) → **Girth Scale** (X/Z)
- **Height** (m) → **Vertical Scale** (Y)
- **Skin Type** → **Color & Texture**
- **Gender** → **Sexual Dimorphism**
- **Locomotion** → **Skeleton Type** (5 rigs)

## 📊 Project Status

| Component | Status |
|-----------|--------|
| Procedural Generation | ✅ Complete |
| Evolution System | ✅ Complete |
| Combat System | ✅ Complete |
| 3D Export Pipeline | ✅ Complete |
| Blender Integration | ✅ Complete |
| Unreal Integration | ✅ Complete |
| Documentation | ✅ Complete |

## 🎯 Latest: Blender + Unreal Integration (Jan 22, 2026)

- ✅ Blender Python script with automatic rigging (5 locomotion types)
- ✅ Unreal Engine 5 complete C++ system
- ✅ 11-part comprehensive setup guide
- ✅ 30-minute quick start guide
- ✅ Multi-format export (OBJ, JSON, CSV)
- ✅ All pushed to GitHub

## 📝 License

MIT - Open source and free to use

---

**Version:** 3.0 - Multi-Engine Integration | **Status:** Production Ready ✨
