# 🎯 **QUICK START - Blender + Unreal en 30 Minutos**

## 📋 Pre-requisitos (5 min)

```bash
✓ Blender 4.0+ (descarga desde blender.org)
✓ Unreal Engine 5.3+ (descarga desde Epic Games Launcher)
✓ Archivos generados: creature_0.obj, creature_1.json
```

---

## 🎨 **OPCIÓN 1: Blender (15 minutos)**

### Paso 1: Abre Blender
```
1. Blender → Start Project → General
2. Espera a que cargue
```

### Paso 2: Importa la Criatura
```
1. File → Import → Wavefront (.obj)
2. Selecciona: creature_0.obj
3. Click "Import OBJ"
```

### Paso 3: Aplica Material
```
1. Tab (entra en Edit Mode)
2. Tab (vuelve a Object Mode)
3. En Material Properties (derecha):
   ├── Nuevo Material → "Creature"
   ├── Base Color → Verde (0.2, 0.6, 0.3)
   ├── Roughness → 0.6
   └── Metallic → 0.1
```

### Paso 4: Renderiza
```
1. Viewport → Render Preview (Z + select Rendered)
2. Verifica: Ves la criatura con iluminación 3D
3. F12 → Renderiza imagen final
```

### Resultado
```
✅ Criatura 3D visualizada en Blender
✅ Listo para editar mesh/animaciones
✅ Puedes exportar a: FBX, glTF, USDZ, etc.
```

---

## 🎮 **OPCIÓN 2: Unreal Engine (20 minutos)**

### Paso 1: Crea Proyecto
```
1. Epic Games Launcher → Launch Engine
2. New Project → Third Person (C++)
3. Nombre: PlanetaExplorer
4. Create Project
5. Espera compilación (~5 min)
```

### Paso 2: Importa Malla
```
1. En Unreal, Content Browser (derecha abajo)
2. Click derecho → Import
3. Selecciona: creature_0.obj
4. Opciones:
   ✓ Import Mesh: ON
   ✓ Create Physics Asset: ON
5. Click "Import"
```

### Paso 3: Crea Personaje
```
1. Content → Right click → Blueprint
2. Parent Class: Character
3. Nombre: BP_Creature
4. Abre Blueprint

5. En Viewport:
   └── Add Component → Skeletal Mesh
   └── Set Mesh: Creature_0
   └── Compile

6. Save Blueprint
```

### Paso 4: Spawn en Nivel
```
1. Place → BP_Creature (arrastra a nivel)
2. Play (Alt + P)
3. Verifica: Ves la criatura en el mundo
```

### Resultado
```
✅ Criatura 3D interactiva en Unreal
✅ Movimiento con WASD
✅ Listo para agregar combate/IA
```

---

## 🔄 **Loop Completo: Genera → Visualiza → Distribuye**

```
┌──────────────────────────────────────────────────────────────┐
│  1. GENERAR (Java)                                           │
│  ─────────────────────────────────────────────────────────── │
│  $ cd PLantet                                                │
│  $ java Creature3DDemo                                       │
│  ✓ Genera: creature_0.obj, creature_1.json, .csv           │
└────────────────┬─────────────────────────────────────────────┘
                 │
         ┌───────▼────────┐
         │ VISUALIZAR     │
         └────┬──────┬────┘
             │      │
      ┌──────▼─┐  ┌─▼──────┐
      │ Blender│  │ Unreal │
      └──────┬─┘  └─┬──────┘
             │      │
      ┌──────▼─┐  ┌─▼──────┐
      │ Export │  │ Package│
      │  FBX   │  │  .exe  │
      └──────┬─┘  └─┬──────┘
             │      │
         ┌───▼──────▼───┐
         │ DISTRIBUIR   │
         ├──────────────┤
         │ • GitHub     │
         │ • Itch.io    │
         │ • Servidor   │
         └──────────────┘
```

---

## 🔗 **Integración Java → Blender**

Para automatizar el flujo completo:

```bash
#!/bin/bash
# ARCHIVO: build_creatures.sh

echo "🔄 Generando criaturas..."
cd /path/to/PLantet
java Creature3DDemo

echo "🎨 Abriendo en Blender..."
blender --python blender_creature_importer.py

echo "✅ Listo para renderizar"
```

**Ejecutar:**
```bash
chmod +x build_creatures.sh
./build_creatures.sh
```

---

## 🔗 **Integración Java → Unreal**

Para pipeline automático:

```python
# ARCHIVO: Content/Python/sync_creatures.py

import unreal
import json
import shutil

def sync_creatures_from_java(java_folder: str, unreal_content: str) -> None:
    """Sincroniza criaturas desde carpeta Java a Unreal"""
    
    # 1. Copiar archivos
    for obj_file in os.listdir(java_folder):
        if obj_file.endswith('.obj'):
            src = os.path.join(java_folder, obj_file)
            dst = os.path.join(unreal_content, 'Creatures', 'Meshes', obj_file)
            shutil.copy2(src, dst)
    
    # 2. Importar en Unreal
    import_task = unreal.AssetImportTask()
    import_task.filename = dst
    import_task.destination_path = "/Game/Creatures/Meshes"
    # ... más configuración
    
    # 3. Crear Blueprint automáticamente
    # ...

# Usar en Unreal Console:
# exec("C:/path/to/sync_creatures.py")
```

---

## 📊 **Comparativa: Blender vs Unreal**

| Aspecto | Blender | Unreal |
|--------|---------|--------|
| **Setup** | 2 min | 10 min |
| **Importar OBJ** | 1 click | 1 click |
| **Render** | CPU/GPU | Real-time |
| **Edición** | Full modeling | Limited |
| **Distribución** | Exportar FBX | Package .exe |
| **Mejor para** | Artístico | Juegos |

**Recomendación:** Usa **Blender para arte**, **Unreal para juegos interactivos**

---

## 🎬 **Próximos Pasos Avanzados**

### 1. Agregar Animaciones
```
Blender:
1. Add → Armature
2. Rigging → Crear huesos
3. Animate → Motion tracking

Unreal:
1. Create Animation Montage
2. Blueprint → State Machine
3. Link a movimiento/combate
```

### 2. Combate Procedural
```cpp
// En Unreal
if (TakeDamage(40)) {
    PlayAdaptiveDefense();  // Mitigación 50%
    ApplyMutation();        // Evoluciona
}
```

### 3. Ecosistema Completo
```
- Múltiples criaturas interactuando
- Reproducción genética
- Selección natural visual
- Árbol filogenético 3D
```

---

## 🐛 **Troubleshooting Rápido**

| Problema | Solución |
|----------|----------|
| **OBJ no importa en Blender** | Usa importador Wavefront; verifica normals |
| **Unreal no encuentra mesh** | Verifica path; asegura content folder existe |
| **Material se ve gris** | Activa viewport shading (Z → rendered) |
| **Criatura no se mueve en Unreal** | Agrega CharacterMovement component |
| **Archivo JSON no parse** | Verifica formato UTF-8; no BOM |

---

## 🎉 **¡Listo!**

Has completado:
- ✅ Generación procedural de criaturas (Java)
- ✅ Visualización 3D (Blender)
- ✅ Motor de juego (Unreal)
- ✅ Integración completa

**Próximo:** ¿Quieres combate, multiplayer, o más evolución?
