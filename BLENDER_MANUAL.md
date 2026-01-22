# 🎨 **ABRIR BLENDER CON TU CRIATURA - Guía Manual**

## 3 Formas de Importar tu Criatura en Blender

---

## **OPCIÓN 1: Automática (Recomendada)** ⚡

### Paso 1: Abre Blender
```
Windows:
  Start Menu → Blender → Launch

Mac:
  Applications → Blender.app

Linux:
  $ blender &
```

### Paso 2: Abre la Consola Python
```
1. Blender → Scripting (arriba)
2. Ves panel con "New Text File"
3. Click "New Text File"
```

### Paso 3: Copia el Script
```
1. Abre: blender_creature_importer.py
2. Copy TODO el código (Ctrl+A → Ctrl+C)
3. En Blender, pega en el editor (Ctrl+V)
```

### Paso 4: Ejecuta
```
1. Click en el editor de texto
2. Alt+P (o click ▶ Run Script)
3. Verás en la consola:
   🔄 Importando criatura: creature_0.obj
   📊 Genoma: G1-A0-M0-D0-H100-AC2-GILLS-SWIMMI-SALTTO
   ✅ Criatura importada: Creature_G1-A0-M0
```

---

## **OPCIÓN 2: Desde Terminal (Para expertos)**

### Windows PowerShell
```powershell
# Navega a la carpeta
cd "C:\Users\edusc\Desktop\Master Code\PLantet"

# Busca Blender
$blender = Get-Command blender -ErrorAction SilentlyContinue
if ($blender) {
    & blender --python blender_creature_importer.py
} else {
    echo "Blender no encontrado. Instálalo primero."
}
```

### macOS Terminal
```bash
cd /path/to/PLantet
/Applications/Blender.app/Contents/MacOS/Blender --python blender_creature_importer.py
```

### Linux Terminal
```bash
cd /path/to/PLantet
blender --python blender_creature_importer.py
```

---

## **OPCIÓN 3: Manual (Sin Script)**

### Paso 1: Abre Blender
### Paso 2: Import OBJ
```
1. File → Import → Wavefront (.obj)
2. Navega a: C:\Users\edusc\Desktop\Master Code\PLantet\creature_0.obj
3. Click "Import OBJ"
```

### Paso 3: Aplica Material
```
1. Selecciona el objeto (debe estar resaltado)
2. Material Properties (derecha) → Nuevo Material
3. Shader Editor (abajo):
   - Base Color → Verde (0.2, 0.6, 0.3)
   - Roughness → 0.6
   - Metallic → 0.1
```

### Paso 4: Visualiza
```
1. Press Z (pie menu)
2. Select "Rendered" (esfera blanca)
3. ¡Ves tu criatura con iluminación 3D!
```

---

## **DESPUÉS DE IMPORTAR - Tips**

### 🎬 Ver en Renderizado
```
Press Z → Select "Rendered"
```

### 🔍 Rotar Vista
```
Middle Mouse Drag (o Shift+Right Click)
```

### 🔎 Zoom
```
Scroll Wheel (o numpad +/-)
```

### 🏠 Encuadrar
```
Numpad . (período) o Home key
```

### 📷 Cambiar Viewport
```
Numpad 0 (Cámara)
Numpad 7 (Top)
Numpad 1 (Front)
Numpad 3 (Side)
```

---

## **RENDERIZADO FINAL**

### Quick Render
```
F12 (renderiza frame actual)
```

### Render Settings
```
1. Render Properties (derecha, icono cámara)
2. Engine: Cycles o Eevee
3. Samples: 128-256 (más = mejor pero lento)
4. Click Render
```

### Guardar Render
```
Image → Save As...
```

---

## **EDITAR CRIATURA**

### Agregar Luz
```
1. Shift+A (Add menu)
2. Light → Sun (o Point)
3. Mueve con G, escala con S
```

### Editar Mesh
```
1. Selecciona objeto
2. Tab (entra en Edit Mode)
3. Edit → Face/Edge/Vertex mode
4. Tab (vuelve a Object Mode)
```

### Agregar Materiales Extra
```
1. Principled BSDF:
   ├─ Base Color (color)
   ├─ Metallic (0.0-1.0)
   ├─ Roughness (0.0-1.0)
   ├─ IOR (índice refracción)
   └─ Alpha (transparencia)
```

---

## **EXPORTAR RESULTADO**

### Exportar a FBX (para Unreal/Unity)
```
1. File → Export → FBX (.fbx)
2. Nombre: creature_final.fbx
3. Opciones:
   ✓ Mesh
   ✓ Materials
   ✓ Modifiers
4. Export FBX
```

### Exportar a glTF (para web)
```
1. File → Export → glTF 2.0 (.glb/.gltf)
2. Nombre: creature_final.glb
3. Export glTF 2.0
```

### Exportar a STL (para imprenta 3D)
```
1. File → Export → STL (.stl)
2. Nombre: creature_final.stl
3. Export STL
```

---

## 🎯 **FLUJO COMPLETO**

```
creature_0.obj
      ↓
   IMPORT EN BLENDER
      ↓
 APLICAR MATERIALES
      ↓
    VISUALIZAR (Z→Rendered)
      ↓
   EDITAR SI QUIERES
      ↓
   RENDERIZAR (F12)
      ↓
   EXPORTAR (FBX/glTF)
      ↓
   USO EN UNREAL/UNITY/WEB
```

---

## ⚠️ **PROBLEMAS COMUNES**

| Problema | Solución |
|----------|----------|
| **OBJ no importa** | Verifica ruta, usa Import → Wavefront OBJ |
| **Material se ve gris** | Press Z → Select Rendered |
| **Criatura no visible** | Press Numpad . (home) para encuadrar |
| **Muy oscuro** | Agrega luz (Shift+A → Light) |
| **Esqueleto no aparece** | Tab en Edit Mode, verifica modifiers |

---

## 🚀 **PRÓXIMOS PASOS**

1. ✅ Importar en Blender
2. 🎨 Editar materiales/mesh
3. 📸 Renderizar imagen
4. 📤 Exportar a FBX
5. 🎮 Llevar a Unreal/Unity

---

**¡Tu criatura está lista para Blender!** 🎉
