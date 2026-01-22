// 🎮 Unreal Engine - Creature Generation System
// C++ Integration for Unreal Engine 5.x

// ============================================================================
// ARCHIVO: PlanetaCreature.h
// Estructura de datos para criaturas importadas
// ============================================================================

#pragma once

#include "CoreMinimal.h"
#include "GameFramework/Character.h"
#include "InputActionValue.h"
#include "PlanetaCreature.generated.h"

/**
 * 📊 Código genético en formato comprimido
 * Ejemplo: G1-A0-M0-D0-H100-AC2-GILLS-SWIMMI-SALTTO
 * 
 * G1 = Generación 1
 * A0 = Adaptabilidad 0
 * M0 = Mutación 0
 * D0 = Daño 0
 * H100 = Health 100
 * AC2 = Adaptive Charges 2
 * GILLS = Branquias
 * SWIMMI = Locomotión: Natación
 * SALTTO = Tolerancia a sal
 */
USTRUCT(BlueprintType)
struct FGeneticCode
{
    GENERATED_BODY()

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    int32 Generation = 1;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    int32 Adaptability = 0;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    int32 MutationCount = 0;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    float TotalDamage = 0.0f;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    float Health = 100.0f;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    int32 AdaptiveCharges = 2;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    TArray<FString> Traits;

    FString ToCompactString() const
    {
        FString Code = FString::Printf(TEXT("G%d-A%d-M%d-D%.0f-H%.0f-AC%d"),
            Generation, Adaptability, MutationCount, TotalDamage, Health, AdaptiveCharges);
        
        for (const FString& Trait : Traits)
        {
            Code += TEXT("-") + Trait;
        }
        
        return Code;
    }
};

/**
 * 🧬 Datos físicos de la criatura
 */
USTRUCT(BlueprintType)
struct FPhysicalProperties
{
    GENERATED_BODY()

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Physical")
    float Weight = 10.0f;  // kg

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Physical")
    float Height = 1.0f;   // metros

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Physical")
    FVector Scale = FVector(1.0f, 1.0f, 1.0f);

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Physical")
    FString Gender = TEXT("MALE");

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Physical")
    FString LocomotionType = TEXT("BIPEDAL");

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Physical")
    FString SkinType = TEXT("Fur");

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Physical")
    FLinearColor SkinColor = FLinearColor::Yellow;

    FVector GetScaleFromPhysics() const
    {
        // Altura mapea 1:1 a escala Z
        // Peso mapea a X/Z girth (weight / 50.0)
        float Girth = Weight / 50.0f;
        return FVector(Girth, Girth, Height);
    }
};

/**
 * 🎨 Información de material procedural
 */
USTRUCT(BlueprintType)
struct FCreatureMaterial
{
    GENERATED_BODY()

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Material")
    FString TexturePath = TEXT("");

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Material")
    FLinearColor BaseColor = FLinearColor::White;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Material")
    float Roughness = 0.6f;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Material")
    float Metallic = 0.1f;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Material")
    float Subsurface = 0.0f;
};

/**
 * 🦴 Estructura de esqueleto (para animaciones procedurales)
 */
UENUM(BlueprintType)
enum class ELocomotionType : uint8
{
    LT_Bipedal UMETA(DisplayName = "Bipedal"),
    LT_Quadrupedal UMETA(DisplayName = "Quadrupedal"),
    LT_Swimming UMETA(DisplayName = "Swimming"),
    LT_Flying UMETA(DisplayName = "Flying"),
    LT_Crawling UMETA(DisplayName = "Crawling")
};

/**
 * 🧬 Clase principal de Criatura
 */
UCLASS()
class PLANETA_API APlanetaCreature : public ACharacter
{
    GENERATED_BODY()

public:
    APlanetaCreature();

    virtual void BeginPlay() override;
    virtual void Tick(float DeltaTime) override;

    // ===== GENÉTICA =====

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    FGeneticCode GeneticCode;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    FPhysicalProperties PhysicalProperties;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    FCreatureMaterial CreatureMaterial;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    int32 LineageID = 1;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Genetics")
    int32 Age = 0;

    // ===== ANIMACIÓN =====

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Animation")
    ELocomotionType CurrentLocomotion = ELocomotionType::LT_Bipedal;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Animation")
    UAnimMontage* IdleAnimation = nullptr;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Animation")
    UAnimMontage* MoveAnimation = nullptr;

    UPROPERTY(BlueprintReadWrite, EditAnywhere, Category = "Animation")
    UAnimMontage* AttackAnimation = nullptr;

    // ===== FUNCIONES =====

    /**
     * 🔧 Inicializa la criatura desde archivo OBJ/JSON
     * @param CreaturePath Ruta al archivo creature_X.obj o creature_X.json
     */
    UFUNCTION(BlueprintCallable, Category = "Creature")
    void InitializeFromFile(const FString& CreaturePath);

    /**
     * 🎨 Aplica material procedural basado en genética
     */
    UFUNCTION(BlueprintCallable, Category = "Creature")
    void ApplyProceduralMaterial();

    /**
     * 🚴 Cambia tipo de locomoción
     */
    UFUNCTION(BlueprintCallable, Category = "Creature")
    void SetLocomotion(ELocomotionType NewLocomotion);

    /**
     * 📊 Obtiene código genético comprimido
     */
    UFUNCTION(BlueprintPure, Category = "Creature")
    FString GetGeneticCodeString() const { return GeneticCode.ToCompactString(); }

    /**
     * ❤️ Aplica daño y gestiona evolución adaptativa
     */
    UFUNCTION(BlueprintCallable, Category = "Creature")
    void TakeDamage(float Amount, FVector HitLocation);

    /**
     * 🧬 Muta criatura basada en estrés ambiental
     */
    UFUNCTION(BlueprintCallable, Category = "Creature")
    void ApplyMutation(const TArray<FString>& AvailableTraits);

    /**
     * 🎬 Reproduce animación de ataque/defensa
     */
    UFUNCTION(BlueprintCallable, Category = "Creature")
    void PlayCombatAnimation(const FString& ActionType);

protected:
    virtual void SetupCharacterMaterial(class UMaterialInstanceDynamic* DynamicMaterial);

    void UpdateLocationSpecificAdaptations();

private:
    UPROPERTY()
    class USkeletalMeshComponent* CreatureMesh;

    UPROPERTY()
    class UMaterialInstanceDynamic* DynamicMaterial;

    FTimerHandle RegenerationTimerHandle;
    void RegenerateHealth();
};

// ============================================================================
// ARCHIVO: PlanetaCreature.cpp
// Implementación
// ============================================================================

#include "PlanetaCreature.h"
#include "GameFramework/CharacterMovementComponent.h"
#include "Animation/AnimInstance.h"
#include "Materials/MaterialInstanceDynamic.h"
#include "Engine/World.h"

APlanetaCreature::APlanetaCreature()
{
    PrimaryActorTick.TickInterval = 0.1f;
    PrimaryActorTick.TickType = ETickingGroup::TG_PrePhysics;

    GetCharacterMovement()->MaxWalkSpeed = 600.0f;
    GetCharacterMovement()->MaxSwimSpeed = 800.0f;
    GetCharacterMovement()->MaxFlySpeed = 1000.0f;

    // Configurar mesh
    GetMesh()->SetRelativeLocation(FVector(0.0f, 0.0f, -88.0f));
    GetMesh()->SetRelativeRotation(FRotator(0.0f, -90.0f, 0.0f));
}

void APlanetaCreature::BeginPlay()
{
    Super::BeginPlay();

    // Aplicar escala física
    FVector Scale = PhysicalProperties.GetScaleFromPhysics();
    GetMesh()->SetRelativeScale3D(Scale);

    // Crear material dinámico
    if (GetMesh()->GetMaterial(0))
    {
        DynamicMaterial = UMaterialInstanceDynamic::Create(
            GetMesh()->GetMaterial(0),
            this
        );
        GetMesh()->SetMaterial(0, DynamicMaterial);
        ApplyProceduralMaterial();
    }

    // Iniciar regeneración cada 5 segundos
    GetWorld()->GetTimerManager().SetTimer(
        RegenerationTimerHandle,
        this,
        &APlanetaCreature::RegenerateHealth,
        5.0f,
        true
    );

    UE_LOG(LogTemp, Warning, TEXT("🧬 Criatura creada: %s"), *GetGeneticCodeString());
}

void APlanetaCreature::Tick(float DeltaTime)
{
    Super::Tick(DeltaTime);
    UpdateLocationSpecificAdaptations();
}

void APlanetaCreature::InitializeFromFile(const FString& CreaturePath)
{
    // Implementación: Parsear JSON/OBJ y rellenar propiedades
    // Ejemplo: FJsonObjectWrapper::FromString() para JSON
    
    UE_LOG(LogTemp, Warning, TEXT("📂 Cargando criatura desde: %s"), *CreaturePath);

    // Cargar malla OBJ
    // Cargar parámetros JSON
    // Inicializar propiedades físicas
}

void APlanetaCreature::ApplyProceduralMaterial()
{
    if (!DynamicMaterial) return;

    // Color base según tipo de piel
    FLinearColor BaseColor = FLinearColor::Gray;

    if (PhysicalProperties.SkinType == TEXT("Scales"))
        BaseColor = FLinearColor(0.2f, 0.6f, 0.3f, 1.0f);  // Verde
    else if (PhysicalProperties.SkinType == TEXT("Fur"))
        BaseColor = FLinearColor(0.5f, 0.4f, 0.3f, 1.0f);  // Marrón
    else if (PhysicalProperties.SkinType == TEXT("Feathers"))
        BaseColor = FLinearColor(0.8f, 0.6f, 0.2f, 1.0f);  // Dorado

    // Dimorfismo sexual
    if (PhysicalProperties.Gender == TEXT("FEMALE"))
    {
        BaseColor.R *= 1.1f;
        BaseColor.G *= 0.8f;
        BaseColor.B *= 1.2f;
    }

    DynamicMaterial->SetVectorParameterValue(FName("BaseColor"), BaseColor);
    DynamicMaterial->SetScalarParameterValue(FName("Roughness"), CreatureMaterial.Roughness);
    DynamicMaterial->SetScalarParameterValue(FName("Metallic"), CreatureMaterial.Metallic);
}

void APlanetaCreature::SetLocomotion(ELocomotionType NewLocomotion)
{
    CurrentLocomotion = NewLocomotion;

    switch (NewLocomotion)
    {
        case ELocomotionType::LT_Bipedal:
            GetCharacterMovement()->MaxWalkSpeed = 600.0f;
            GetCharacterMovement()->MovementMode = MOVE_Walking;
            break;
        case ELocomotionType::LT_Quadrupedal:
            GetCharacterMovement()->MaxWalkSpeed = 800.0f;
            GetCharacterMovement()->MovementMode = MOVE_Walking;
            break;
        case ELocomotionType::LT_Swimming:
            GetCharacterMovement()->MaxSwimSpeed = 800.0f;
            GetCharacterMovement()->MovementMode = MOVE_Swimming;
            break;
        case ELocomotionType::LT_Flying:
            GetCharacterMovement()->MaxFlySpeed = 1000.0f;
            GetCharacterMovement()->MovementMode = MOVE_Flying;
            break;
        case ELocomotionType::LT_Crawling:
            GetCharacterMovement()->MaxWalkSpeed = 400.0f;
            GetCharacterMovement()->MovementMode = MOVE_Walking;
            break;
    }
}

void APlanetaCreature::TakeDamage(float Amount, FVector HitLocation)
{
    GeneticCode.TotalDamage += Amount;
    GeneticCode.Health -= Amount;

    if (GeneticCode.Health <= 0.0f)
    {
        GeneticCode.Health = 0.0f;
        Destroy();
    }
    else if (GeneticCode.AdaptiveCharges > 0 && Amount > 20.0f)
    {
        // Adaptación defensiva: 50% daño mitigation con carga adaptativa
        GeneticCode.AdaptiveCharges--;
        GeneticCode.Health += Amount * 0.5f;  // Recupera 50%
        UE_LOG(LogTemp, Warning, TEXT("🛡️  Adaptación activada. Cargas restantes: %d"), 
            GeneticCode.AdaptiveCharges);
    }
}

void APlanetaCreature::ApplyMutation(const TArray<FString>& AvailableTraits)
{
    if (AvailableTraits.Num() == 0) return;

    int32 RandomIndex = FMath::RandRange(0, AvailableTraits.Num() - 1);
    FString NewTrait = AvailableTraits[RandomIndex];

    if (!GeneticCode.Traits.Contains(NewTrait))
    {
        GeneticCode.Traits.Add(NewTrait);
        GeneticCode.MutationCount++;
        GeneticCode.Adaptability++;

        UE_LOG(LogTemp, Warning, TEXT("🧬 Mutación aplicada: %s"), *NewTrait);
    }
}

void APlanetaCreature::PlayCombatAnimation(const FString& ActionType)
{
    if (ActionType == TEXT("Attack") && AttackAnimation)
    {
        GetMesh()->GetAnimInstance()->Montage_Play(AttackAnimation);
    }
    else if (ActionType == TEXT("Idle") && IdleAnimation)
    {
        GetMesh()->GetAnimInstance()->Montage_Play(IdleAnimation);
    }
}

void APlanetaCreature::RegenerateHealth()
{
    if (GeneticCode.Health < 100.0f)
    {
        GeneticCode.Health = FMath::Min(GeneticCode.Health + 5.0f, 100.0f);
    }
}

void APlanetaCreature::UpdateLocationSpecificAdaptations()
{
    // Ejemplo: En agua, las branquias activan
    // En volcanes, resistencia al fuego
    // Etc.
}
