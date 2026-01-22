# 🎨 **CÓMO VER TU CRIATURA RENDERIZADA EN BLENDER**

## ✨ RESULTADO ESPERADO

Cuando abras el script en Blender, verás:

```
🎨 BLENDER CREATURE IMPORTER
============================================================

🔄 Importando criatura: C:\...\creature_0.obj
📊 Genoma: G1-A0-M0-D0-H100-AC2-GILLS-SWIMMI-SALTTO
📐 Peso: 8.0 kg | Alto: 0.45 m
🚴 Locomoción: SWIMMING

✅ Criatura importada: Creature_G1-A0-M0

💡 Próximos pasos:
   1. Tab → Edita pesos del esqueleto (weight painting)
   2. Agrega acciones (Actions) para animaciones
   3. Render → Configura iluminación y cámara
   4. Exporta a formato final (FBX/glTF/Alembic)
```

---

## 🖥️ **INTERFAZ BLENDER - Qué Verás**

```
┌─────────────────────────────────────────────────────────┐
│ Blender 4.0+                                    ▬ ▢ ╳  │
├─────────────────────────────────────────────────────────┤
│ Layout | Modeling | Sculpting | UV Editing | Texture   │
├─────────────────────────────────────────────────────────┤
│  🐠                                                      │
│       ╱────────╲                                         │
│    ══╱    ◉      ╲══                                     │
│      ╲──────────╱                                        │
│         🐠                                               │ ← TU CRIATURA
│      (renderizada con                                    │
│       materials verdes)                                  │
│                                                         │
├─────────────────────────────────────────────────────────┤
│ Scripting                                               │
├─────────────────────────────────────────────────────────┤
│ 🟢 BLENDER CREATURE IMPORTER                            │
│ ============================================================│
│ 🔄 Importando criatura: creature_0.obj                  │
│ 📊 Genoma: G1-A0-M0-D0-H100-AC2-GILLS-SWIMMI-SALTTO   │
│ ✅ Criatura importada: Creature_G1-A0-M0               │
└─────────────────────────────────────────────────────────┘
```

---

## 🎯 **PASOS PARA VER RENDERIZADO**

### 1️⃣ **Abre Blender**
```
Windows: Start Menu → Blender
Mac: Applications → Blender.app
Linux: $ blender
```

### 2️⃣ **Ve a Scripting**
```
Click en "Scripting" (arriba de la interfaz)
```

### 3️⃣ **Abre el Script**
```
Text Editor (parte inferior) → 📁 Open
Selecciona: blender_creature_importer.py
```

### 4️⃣ **Ejecuta**
```
Alt+P (o click en ▶ Run Script)
Espera 2-3 segundos...
```

### 5️⃣ **Ve el Resultado**
```
Arriba en Viewport verás tu criatura importada
Abajo en Console verás los logs
```

### 6️⃣ **Visualiza Renderizada**
```
Press Z (se abre pie menu)
Select "Rendered" (última opción)
¡Ves la criatura con iluminación 3D y materiales!
```

---

## 🎨 **MATERIALES QUE SE APLICAN AUTOMÁTICAMENTE**

El script crea materiales basados en tu genoma:

| Tipo de Piel | Color | RGB |
|--------------|-------|-----|
| **Scales** (nadador) | 🟢 Verde | (0.2, 0.6, 0.3) |
| **Fur** | 🟤 Marrón | (0.5, 0.4, 0.3) |
| **Feathers** | 🟡 Dorado | (0.8, 0.6, 0.2) |
| **Skin** | 🟠 Beige | (0.8, 0.7, 0.6) |
| **Chitinous** | ⚫ Negro | (0.3, 0.3, 0.2) |
| **Crystalline** | ⚪ Blanco | (0.9, 0.9, 0.95) |

### Dimorfismo Sexual
- **MALE**: Color normal saturado
- **FEMALE**: Tintado con magenta (R×1.1, G×0.8, B×1.2)

---

## 🦴 **ESQUELETOS PROCEDURALES GENERADOS**

El script crea automáticamente esqueletos según locomoción:

### SWIMMING (Nadador) 🐠
```
5 segmentos de espina ondulante
├─ Spine_Segment_0
├─ Spine_Segment_1
├─ Spine_Segment_2
├─ Spine_Segment_3
└─ Spine_Segment_4
```

### BIPEDAL (Bípedo) 🚶
```
Esqueleto humanoide
├─ Spine
├─ Head
├─ Left_Arm + Right_Arm
└─ Left_Leg + Right_Leg
```

### FLYING (Volador) 🦅
```
Cuerpo + Alas
├─ Body
├─ Wing_-0.3
└─ Wing_0.3
```

### QUADRUPEDAL (Cuadrúpedo) 🐆
```
Columna + 4 Patas
├─ Spine
├─ Head
├─ Leg_Front_-0.2, Leg_Front_0.2
└─ Leg_Back_-0.2, Leg_Back_0.2
```

### CRAWLING (Reptil) 🐛
```
6 segmentos ondulantes (tipo oruga)
├─ Segment_0
├─ Segment_1
├─ Segment_2
├─ Segment_3
├─ Segment_4
└─ Segment_5
```

---

## 🎬 **MODIFICADORES APLICADOS**

| Modificador | Uso |
|------------|-----|
| **Smooth Shading** | Suaviza la superficie |
| **Subdivision Surface** | Agrega detalles (Levels: 2) |
| **Solidify** (si swimming/flying) | Añade profundidad (0.02 m) |
| **Armature** | Vincula esqueleto a mesh |

---

## 📷 **VIEWPORT SHORTCUTS**

```
Z             → Abrir pie menu (switch viewport shading)
Rendered      → Ver con iluminación completa
Material      → Ver solo materiales (sin shadows)
Solid         → Modo básico
Wireframe     → Ver estructura

Middle Mouse  → Rotar vista
Shift+RMB     → Pan (mover vista)
Scroll        → Zoom in/out
Numpad .      → Encuadrar objeto
Home          → Fit all in view
```

---

## 💾 **GUARDAR TU TRABAJO**

### Guardar Blender File
```
File → Save As... (Ctrl+Shift+S)
Nombre: creature_proyecto.blend
```

### Exportar para Unreal
```
File → Export → FBX (.fbx)
creature_exported.fbx
```

### Exportar para Web
```
File → Export → glTF 2.0 (.glb)
creature_web.glb
```

### Renderizar Imagen
```
F12 (renderiza viewport)
Image → Save As...
creature_render.png
```

---

## ✅ **CHECKLIST**

- [ ] Blender instalado
- [ ] creature_0.obj generado (java Creature3DDemo)
- [ ] blender_creature_importer.py abierto en Blender
- [ ] Script ejecutado (Alt+P)
- [ ] Criatura visible en viewport
- [ ] Press Z → "Rendered" para ver iluminación
- [ ] Ves materiales procedurales aplicados
- [ ] Esqueleto creado según locomoción

---

## 🚀 **PRÓXIMOS PASOS**

1. **Editar Mesh** (Tab en Edit Mode)
2. **Agregar Animaciones** (Action Editor)
3. **Renderizar** (F12)
4. **Exportar** (FBX/glTF)
5. **Llevar a Unreal/Unity/Web**

---

## 📞 **PROBLEMAS?**

| Problema | Solución |
|----------|----------|
| **Script no corre** | Verifica sintaxis, copia todo de nuevo |
| **No ve criatura** | Press Numpad . (home view) |
| **Material se ve gris** | Press Z → Rendered |
| **Muy oscuro** | Agrega luz (Shift+A → Light → Sun) |
| **Archivo no importa** | Verifica ruta en parse_creature_metadata() |

---

**¡Tus criaturas genéticas están listas en 3D!** 🎉
