#!/usr/bin/env python3
"""
🎨 Blender Creature Importer Script
Importa criaturas OBJ generadas con materials procedurales basados en genética

USO EN BLENDER:
1. Abre Blender
2. Scripting > New Text File
3. Pega este código
4. Ejecuta (Alt + P)
5. Selecciona creature_0.obj cuando se abra el diálogo
"""

import bpy
import os
import json
import re
from pathlib import Path
from mathutils import Color

class CreatureImporter:
    def __init__(self):
        self.base_path = Path(__file__).parent
        self.creature_data = {}
        self.mesh_object = None
        
    def parse_creature_metadata(self, obj_file_path):
        """
        Extrae metadatos de cabecera del OBJ
        # Genetic Code: G1-A0-M0-D0-H100-AC2-GILLS-SWIMMI-SALTTO
        """
        metadata = {}
        try:
            with open(obj_file_path, 'r') as f:
                for line in f:
                    if line.startswith('# Genetic Code:'):
                        metadata['genetic_code'] = line.split(':')[1].strip()
                    elif line.startswith('# Physical:'):
                        physical_str = line.split(':')[1].strip()
                        parts = physical_str.split(',')
                        if len(parts) >= 4:
                            metadata['weight'] = float(parts[0].split()[0])
                            metadata['height'] = float(parts[1].split()[0])
                            metadata['locomotion'] = parts[2].strip()
                            metadata['skin_type'] = parts[3].strip()
                    elif line.startswith('# Lineage:'):
                        metadata['lineage'] = int(line.split(':')[1].strip().split('|')[0])
                    elif line.startswith('# Age:'):
                        metadata['age'] = int(line.split(':')[1].strip())
        except Exception as e:
            print(f"⚠️  Error parsing metadata: {e}")
        
        return metadata
    
    def load_json_parameters(self, json_file_path):
        """Carga parámetros de creature_1.json"""
        try:
            with open(json_file_path, 'r') as f:
                data = json.load(f)
                return data.get('creature', {})
        except Exception as e:
            print(f"⚠️  Error loading JSON: {e}")
            return {}
    
    def create_material(self, creature_data):
        """
        Crea material procedural basado en características genéticas
        """
        mat_name = f"Creature_Material_{creature_data.get('genetic_code', 'Unknown')[:8]}"
        mat = bpy.data.materials.new(name=mat_name)
        mat.use_nodes = True
        mat.shadow_method = 'HASHED'
        
        bsdf = mat.node_tree.nodes["Principled BSDF"]
        
        # Determinamos color basado en tipo de piel
        color = self.get_color_by_skin_type(
            creature_data.get('skin_type', 'Scales'),
            creature_data.get('physical', {}).get('gender', 'MALE')
        )
        
        bsdf.inputs['Base Color'].default_value = color
        bsdf.inputs['Roughness'].default_value = 0.6
        bsdf.inputs['Metallic'].default_value = 0.1
        
        # Dimorfismo sexual: Hembras más vibrantes
        if creature_data.get('physical', {}).get('gender') == 'FEMALE':
            bsdf.inputs['Subsurface Weight'].default_value = 0.3
        
        return mat
    
    def get_color_by_skin_type(self, skin_type, gender):
        """
        Mapea tipo de piel a color RGBA
        Devuelve tupla (R, G, B, A)
        """
        colors = {
            'Scales': (0.2, 0.6, 0.3, 1.0),          # Verde
            'Fur': (0.5, 0.4, 0.3, 1.0),             # Marrón
            'Feathers': (0.8, 0.6, 0.2, 1.0),        # Dorado
            'Skin': (0.8, 0.7, 0.6, 1.0),            # Beige
            'Chitinous': (0.3, 0.3, 0.2, 1.0),       # Negro
            'Crystalline': (0.9, 0.9, 0.95, 1.0),    # Blanco
        }
        
        base_color = colors.get(skin_type, (0.5, 0.5, 0.5, 1.0))
        
        # Dimorfismo: Hembras con matiz magenta
        if gender == 'FEMALE':
            r, g, b, a = base_color
            return (r * 1.1, g * 0.8, b * 1.2, a)
        
        return base_color
    
    def apply_modifiers(self, obj, creature_data):
        """
        Aplica modificadores basados en locomotión
        """
        locomotion = creature_data.get('locomotion', 'BIPEDAL')
        
        # Smooth Shading
        bpy.context.view_layer.objects.active = obj
        bpy.ops.object.shade_smooth()
        
        # Subdivision Surface para detalles
        modifier = obj.modifiers.new(name="Subdivision", type='SUBSURF')
        modifier.levels = 2
        modifier.render_levels = 3
        
        # Solidify para efecto de profundidad
        if locomotion in ['SWIMMING', 'FLYING']:
            solid = obj.modifiers.new(name="Solidify", type='SOLIDIFY')
            solid.thickness = 0.02
    
    def import_creature(self, obj_file_path, json_file_path=None):
        """
        Flujo principal de importación
        """
        print(f"🔄 Importando criatura: {obj_file_path}")
        
        # 1. Parsear metadatos
        metadata = self.parse_creature_metadata(obj_file_path)
        print(f"📊 Genoma: {metadata.get('genetic_code', 'N/A')}")
        print(f"📐 Peso: {metadata.get('weight', 'N/A')} kg | Alto: {metadata.get('height', 'N/A')} m")
        print(f"🚴 Locomoción: {metadata.get('locomotion', 'N/A')}")
        
        # 2. Importar OBJ
        bpy.ops.import_scene.obj(filepath=str(obj_file_path))
        imported_obj = bpy.context.selected_objects[0]
        self.mesh_object = imported_obj
        
        # 3. Cargar parámetros JSON si existe
        if json_file_path and os.path.exists(json_file_path):
            creature_data = self.load_json_parameters(json_file_path)
            metadata.update(creature_data)
        
        # 4. Crear y aplicar material
        material = self.create_material(metadata)
        if imported_obj.data.materials:
            imported_obj.data.materials[0] = material
        else:
            imported_obj.data.materials.append(material)
        
        # 5. Aplicar modificadores
        self.apply_modifiers(imported_obj, metadata)
        
        # 6. Renombrar objeto
        creature_name = f"Creature_{metadata.get('genetic_code', 'Unknown')[:8]}"
        imported_obj.name = creature_name
        imported_obj.data.name = creature_name + "_Mesh"
        
        # 7. Crear armadura básica (esqueleto)
        self.create_basic_armature(imported_obj, metadata)
        
        print(f"✅ Criatura importada: {creature_name}")
        return imported_obj
    
    def create_basic_armature(self, obj, creature_data):
        """
        Crea estructura de huesos para animaciones procedurales
        """
        locomotion = creature_data.get('locomotion', 'BIPEDAL')
        
        bpy.ops.object.armature_add(name=f"{obj.name}_Rig")
        armature = bpy.context.object
        
        # Editar modo para agregar huesos
        bpy.context.view_layer.objects.active = armature
        bpy.ops.object.mode_set(mode='EDIT')
        
        # Estructura básica de esqueleto según locomoción
        if locomotion == 'BIPEDAL':
            self._create_bipedal_rig(armature)
        elif locomotion == 'SWIMMING':
            self._create_swimming_rig(armature)
        elif locomotion == 'QUADRUPEDAL':
            self._create_quadrupedal_rig(armature)
        elif locomotion == 'FLYING':
            self._create_flying_rig(armature)
        elif locomotion == 'CRAWLING':
            self._create_crawling_rig(armature)
        
        bpy.ops.object.mode_set(mode='OBJECT')
        
        # Agregar modifier Armature al mesh
        bpy.context.view_layer.objects.active = obj
        modifier = obj.modifiers.new(name="Armature", type='ARMATURE')
        modifier.object = armature
    
    def _create_bipedal_rig(self, armature):
        """Esqueleto humanoide"""
        edit_bones = armature.data.edit_bones
        
        # Columna vertebral
        spine = edit_bones.new("Spine")
        spine.head = (0, 0, 0)
        spine.tail = (0, 0, 0.5)
        
        # Cabeza
        head = edit_bones.new("Head")
        head.parent = spine
        head.head = spine.tail
        head.tail = spine.tail + (0, 0, 0.3)
        
        # Extremidades
        for side in [(-0.2, "Left"), (0.2, "Right")]:
            offset, name = side
            
            # Brazo
            arm = edit_bones.new(f"{name}_Arm")
            arm.parent = spine
            arm.head = spine.tail + (offset, 0, 0.3)
            arm.tail = arm.head + (offset * 0.5, -0.5, 0)
            
            # Pierna
            leg = edit_bones.new(f"{name}_Leg")
            leg.parent = spine
            leg.head = spine.head + (offset, 0, 0)
            leg.tail = leg.head + (offset * 0.3, 0, -0.6)
    
    def _create_swimming_rig(self, armature):
        """Esqueleto acuático (sin extremidades, movimiento ondulante)"""
        edit_bones = armature.data.edit_bones
        
        # Espina dorsal ondulante
        segments = 5
        prev_bone = None
        
        for i in range(segments):
            bone = edit_bones.new(f"Spine_Segment_{i}")
            if prev_bone:
                bone.parent = prev_bone
                bone.head = prev_bone.tail
            else:
                bone.head = (0, 0, -0.2)
            
            bone.tail = bone.head + (0, 0, 0.15)
            prev_bone = bone
    
    def _create_quadrupedal_rig(self, armature):
        """Esqueleto cuadrúpedo"""
        edit_bones = armature.data.edit_bones
        
        # Columna vertebral
        spine = edit_bones.new("Spine")
        spine.head = (0, 0, 0)
        spine.tail = (0, 0, 0.4)
        
        # Cabeza
        head = edit_bones.new("Head")
        head.parent = spine
        head.head = spine.tail
        head.tail = spine.tail + (0, 0.3, 0)
        
        # Cuatro patas
        for side in [(-0.2, "Front"), (-0.2, "Back")]:
            for offset in [-0.2, 0.2]:
                leg_type, _ = side
                leg = edit_bones.new(f"Leg_{leg_type}_{offset}")
                leg.parent = spine
                leg.head = spine.head + (offset, 0, 0) if leg_type == "Front" else spine.tail + (offset, 0, 0)
                leg.tail = leg.head + (0, -0.4, 0)
    
    def _create_flying_rig(self, armature):
        """Esqueleto volador (con alas)"""
        edit_bones = armature.data.edit_bones
        
        # Cuerpo central
        body = edit_bones.new("Body")
        body.head = (0, 0, 0)
        body.tail = (0, 0, 0.3)
        
        # Alas
        for offset in [-0.3, 0.3]:
            wing = edit_bones.new(f"Wing_{offset}")
            wing.parent = body
            wing.head = body.head + (offset, 0, 0)
            wing.tail = wing.head + (offset * 0.3, 0.5, 0)
    
    def _create_crawling_rig(self, armature):
        """Esqueleto reptiliano (múltiples segmentos)"""
        edit_bones = armature.data.edit_bones
        
        # Segmentos del cuerpo (tipo oruga)
        segments = 6
        
        for i in range(segments):
            segment = edit_bones.new(f"Segment_{i}")
            if i == 0:
                segment.head = (0, 0, 0)
            else:
                segment.head = edit_bones[f"Segment_{i-1}"].tail
            
            segment.tail = segment.head + (0, 0.2, 0)
            
            if i > 0:
                segment.parent = edit_bones[f"Segment_{i-1}"]


def main():
    """
    Función principal - Abre diálogo para seleccionar archivo
    """
    print("=" * 60)
    print("🎨 BLENDER CREATURE IMPORTER")
    print("=" * 60)
    
    importer = CreatureImporter()
    
    # Buscar archivos OBJ en el directorio actual
    script_dir = Path(__file__).parent
    obj_files = list(script_dir.glob("creature_*.obj"))
    
    if obj_files:
        print(f"📁 Encontrados {len(obj_files)} archivos OBJ:")
        for i, f in enumerate(obj_files, 1):
            print(f"  {i}. {f.name}")
        
        # Importar el primero (o personalizar lógica)
        target_obj = obj_files[0]
        target_json = script_dir / target_obj.stem.replace('.obj', '.json')
        
        creature = importer.import_creature(
            str(target_obj),
            str(target_json) if target_json.exists() else None
        )
        
        # Configurar cámara
        for obj in bpy.context.scene.objects:
            if obj.type == 'CAMERA':
                obj.location = (1.5, -1.5, 1.2)
                obj.rotation_euler = (1.1, 0, 0.785)
                bpy.context.scene.camera = obj
        
        # Configurar iluminación
        for obj in bpy.context.scene.objects:
            if obj.type == 'LIGHT':
                obj.data.energy = 1000
        
        print("\n✅ Importación completada")
        print("💡 Próximos pasos:")
        print("   1. Tab → Edita pesos del esqueleto (weight painting)")
        print("   2. Agrega acciones (Actions) para animaciones")
        print("   3. Render → Configura iluminación y cámara")
        print("   4. Exporta a formato final (FBX/glTF/Alembic)")
        
    else:
        print("❌ No se encontraron archivos creature_*.obj en el directorio actual")
        print(f"   Buscar en: {script_dir}")


if __name__ == "__main__":
    main()
