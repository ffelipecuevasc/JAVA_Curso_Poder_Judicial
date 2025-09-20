# 📚 Curso de Java

Este repositorio contiene todos los proyectos desarrollados en el marco del curso **Java 17 + Spring Framework** utilizando el IDE **IntelliJ Ultimate 2025.1**, enfocado en crear un sistema integral de **Administración de Casos Judiciales en Chile**.

---

## 🧭 Estructura del Curso

El curso está dividido en **7 clases** (2 horas cada una) que abordan desde fundamentos de Java hasta la construcción de una API REST con persistencia en base de datos Oracle.

Cada clase aplica conceptos de control de versiones con Git/GitHub, buenas prácticas y enfoque práctico.

---

## 🧩 Contenido del Curso analizado clase a clase

### 1️⃣ Clase 1: Fundamentos de Java 17, IntelliJ y Git
- Verificación de JDK y configuración de entorno.
- Creación de un proyecto Maven y estructura básica (`pom.xml`).
- Uso de `main(String[] args)` para construir un **menú de consola mínimo**.
- Lectura con `Scanner`, uso de `switch`, `String.format` y `text blocks`.
- Introducción a Git: `.gitignore`, commits y push inicial.

### 2️⃣ Clase 2: Métodos y Manejo de Excepciones
- Diseño de métodos con firmas limpias, sobrecarga y `varargs`.
- Validaciones reutilizables: ejemplo `esRolValido`, `esFechaValida`.
- Manejo de errores con `try/catch`, `finally`, `multi-catch`.
- Uso de `Optional` para evitar `null`.
- Git: trabajo en rama `feature/metodos-excepciones` y creación de PRs.

### 3️⃣ Clase 3: Programación Orientada a Objetos (POO)
- Modelado de dominio: `Caso`, `Persona`, `Parte`, `Audiencia`.
- Encapsulamiento, `toString`, `equals`, `hashCode`.
- Herencia vs composición, `enum EstadoCaso`.
- Interfaces con `default methods`, uso de `Comparator`.

### 4️⃣ Clase 4: Persistencia con Oracle XE (Docker) y JDBC
- Conexión a base de datos Oracle XE en Docker (`XEPDB1`).
- CRUD con `PreparedStatement`, uso de transacciones.
- Buenas prácticas: mapeo de fechas, manejo de errores SQL.
- Variables externas para conexión (sin subir credenciales).

### 5️⃣ Clase 5: Collections, Genéricos, Lambdas y Streams
- Uso de `List`, `Set`, `Map` para filtrar y ordenar casos judiciales.
- Aplicación de `Lambdas`, `method references`, y `Collectors`.
- Paginación y filtrado en memoria con `Stream.skip/limit`.
- Análisis sobre cuándo consultar en memoria vs SQL.

### 6️⃣ Clase 6: Spring Boot API REST - Parte 1
- Creación de proyecto Spring Boot con starters: `web`, `validation`.
- Aplicación de IoC/DI con `@Component`, `@Service`, `@Repository`.
- Controlador REST con validación (`@Valid`) y manejo de errores (`@ControllerAdvice`).
- Configuración con `application.properties` y perfiles (`dev`).

### 7️⃣ Clase 7: Spring Data JPA + API REST con Oracle XE
- Uso de `@Entity`, `JpaRepository` y query methods (`findBy...`).
- Filtros, paginación (`Pageable`), ordenamiento (`Sort`) en endpoints.
- Validaciones de negocio: ROL único, transición de estados.
- Manejo adecuado de errores HTTP (`400`, `404`) y serialización.

---

## 🔧 Tecnologías utilizadas

- ☕ Java 17
- 🧠 Spring Framework / Spring Boot
- 🐳 Oracle XE vía Docker Desktop
- 🧪 JDBC + JPA
- 💻 IntelliJ IDEA Ultimate
- 🛠️ Git y GitHub
- 🧪 Maven

---

## 📄 Certificaciones sugeridas

- **OCFA Java Foundations (1Z0-811)** – Nivel Fundamentos
- **OCA Java Associate** – Nivel Intermedio
- **OCP Java Professional** – Nivel Avanzado

> Añadí a este curso orientación y recursos para preparar la certificación OCFA, junto con tips prácticos de estudio.

---

## Instructor

**Francisco Felipe Cuevas Cerón**  
Instructor certificado en Java | Oracle  
🔗 [LinkedIn](https://www.linkedin.com/in/ffelipecuevasc/)

---
