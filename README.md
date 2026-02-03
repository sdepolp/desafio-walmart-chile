# Desafío Técnico – Ventanas de Despacho con Cobertura Geográfica

Backend en **Java 21 + Spring Boot** usando **Arquitectura Hexagonal (Ports & Adapters)** para modelar y reservar ventanas de despacho con **capacidad total** y **capacidad por zona**, evitando **sobre-reservas** bajo concurrencia.

---

## Objetivo

Implementar un sistema que permita:

- Definir **zonas geográficas** (cobertura).
- Listar **ventanas disponibles** para una dirección (en este caso, por **comuna**).
- Mostrar bloques horarios con **costo**.
- Reservar ventanas respetando:
    - `capacityTotal` (capacidad total por ventana)
    - `capacityByZone` (capacidad por zona)
- Manejar correctamente ventanas agotadas y consistencia bajo concurrencia.

---

## Stack

- Java 21
- Spring Boot (Web, Validation, Data JPA)
- H2 (in-memory) para ejecución local
- JUnit 5 + Mockito para tests
- SpringBootTest para tests integrales

---

## Estructura (Arquitectura Hexagonal)

Paquete base: `cl.walmart.desafio`

- `domain/`
    - `model/` entidades/VOs del dominio
    - `ports/` interfaces (puertos)
    - `exception/` excepciones del dominio
- `application/`
    - `usecase/` contratos de casos de uso
    - `service/` implementación de casos de uso
    - `dto/` request/response DTOs
- `adapters/`
    - `in/rest/` controllers REST + handler de errores
    - `out/persistence/` adapters JPA (repos + entities)
    - `out/zone/` resolución de zona (por comuna)
    - `out/bootstrap/` carga seed desde JSON
- `config/` configuración de Jackson

---

## Requisitos previos

- Java 21 instalado
- Maven (o usar `mvnw` si el proyecto lo trae)

---

## Cómo ejecutar

1. Asegúrate de tener estos archivos en:
    - `src/main/resources/windows.json`
    - `src/main/resources/application.properties`

2. Levanta la app:

```bash
./mvnw spring-boot:run
