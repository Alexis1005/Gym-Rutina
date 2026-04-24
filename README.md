# 🏋️ GymRutine

> Una aplicación web para gestionar rutinas de entrenamiento personalizadas con generación automática de PDFs

**GymRutine** es una herramienta diseñada para entrenadores personales como **Joana Román** que necesitan:
- ✅ Crear y organizar rutinas de entrenamiento por bloques
- ✅ Asignar rutinas a alumnos con seguimiento por semana
- ✅ Generar PDFs personalizados listos para imprimir
- ✅ Mantener un historial de ejercicios, grupos musculares y bloques

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tech Stack](#-tech-stack)
- [Diagrama UML](#-diagrama-uml)
- [Video Demo](#-video-demo)
- [Instalación](#-instalación)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Guía de Uso](#-guía-de-uso)
- [Deployment](#-deployment)
- [Roadmap](#-roadmap)
- [Licencia](#-licencia)

---

## ✨ Características

### 📝 Gestión de Ejercicios
- Crear y catalogar ejercicios
- Asignar a grupos musculares (pecho, espalda, piernas, etc.)
- Incluir descripciones y notas técnicas

### 🎯 Bloques de Entrenamiento
- Agrupar ejercicios en bloques temáticos
- Definir series, repeticiones, peso y descanso
- Reutilizar bloques en múltiples rutinas

### 💪 Rutinas Personalizadas
- Crear rutinas multi-semana
- Asignar bloques por semana
- Ordenamiento visual de bloques

### 👥 Gestión de Alumnos
- Registro de estudiantes
- Seguimiento de edad y observaciones
- Historial de rutinas asignadas

### 📄 Exportación a PDF
- Generar PDFs personalizados por alumno
- Incluye nombre, fecha, rutina y observaciones
- Tablas dinámicas con columnas por semana
- Casillas de verificación (▢) para marcar en papel
- Paginación automática (máx. 3 bloques por página)

### 🔐 Seguridad
- Autenticación basada en sesiones (Spring Security)
- Control de acceso por usuario

---

## 🛠️ Tech Stack

| Componente | Tecnología | Versión |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 3.4.3 |
| **JDK** | Java | 17 |
| **Base de Datos** | PostgreSQL | 15+ |
| **Migraciones** | Flyway | Latest |
| **Frontend** | Thymeleaf + Bootstrap | 5.x |
| **PDF** | iText 7 | Latest |
| **Seguridad** | Spring Security | 6.x |
| **Control de Versiones** | Git | - |

---

## 📊 Diagrama UML

El siguiente diagrama muestra la arquitectura de datos y las relaciones entre entidades:

![Diagrama UML GymRutine](./docs/diagrama_gymrutine.png)

### Entidades Principales

| Entidad | Descripción |
|---------|-----------|
| **GrupoMuscular** | Clasificación de ejercicios (pecho, espalda, piernas, etc.) |
| **Ejercicio** | Movimiento o ejercicio específico |
| **Bloque** | Conjunto de ejercicios agrupados |
| **BloqueEjercicio** | Configuración de un ejercicio dentro de un bloque (series, reps, peso) |
| **Rutina** | Plan de entrenamiento multi-semana |
| **RutinaBloque** | Asignación de bloques a semanas específicas |
| **Alumno** | Estudiante/cliente del entrenador |
| **AsignacionRutina** | Vinculación entre alumno y rutina con fecha |

> 📌 **Nota**: Las entidades intermedias (`BloqueEjercicio`, `RutinaBloque`, `AsignacionRutina`) permiten relaciones muchos-a-muchos con datos adicionales.

---

## 🎥 Video Demo

¿Quieres ver GymRutine en acción? 

🚀 **[Ver video demo de la aplicación](https://youtube.com/tu-canal-aqui)** *(próximamente)*

En el video podrás observar:
- 📌 Creación de ejercicios y grupos musculares
- 📌 Armado de bloques de entrenamiento
- 📌 Diseño de rutinas multi-semana
- 📌 Asignación de rutinas a alumnos
- 📌 Generación y descarga de PDFs personalizados
- 📌 Navegación por la interfaz

---

## 🚀 Instalación

### Requisitos Previos
- **JDK 17** instalado
- **PostgreSQL 15+** en ejecución
- **Git** para clonar el repositorio

### Pasos de Setup

#### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/GymRutine.git
cd GymRutine
```

#### 2. Configurar la base de datos
Crear base de datos en PostgreSQL:
```sql
CREATE DATABASE gymrutine_db;
CREATE USER gymrutine_user WITH PASSWORD 'tu_contraseña';
GRANT ALL PRIVILEGES ON DATABASE gymrutine_db TO gymrutine_user;
```

#### 3. Configurar `application.properties`
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/gymrutine_db
spring.datasource.username=gymrutine_user
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Flyway
spring.flyway.locations=classpath:db/migration

# Port
server.port=8080
```

#### 4. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

#### 5. Acceder a la app
```
http://localhost:8080
```

---

## 📁 Estructura del Proyecto

```
GymRutine/
├── src/main/java/com/joana/gymrutine/
│   ├── controller/           # Controllers (Thymeleaf + REST)
│   ├── service/              # Lógica de negocio
│   ├── repository/           # Acceso a datos (JPA)
│   ├── exception/
│   ├── model/                # Entidades JPA
│   ├── dto/                  # Data Transfer Objects
│   │   ├── grupoMuscular/
│   │   ├── ejercicio/
│   │   ├── bloque/
│   │   ├── rutina/
│   │   ├── asignacionRutina/
│   │   └── alumno/
│   └── config/               # Configuración (Spring Security, etc.)
├── src/main/resources/
│   ├── templates/            # Plantillas Thymeleaf
│   │   ├── grupos-musculares/
│   │   ├── ejercicios/
│   │   ├── bloques/
│   │   ├── rutina/
│   │   ├── error/
│   │   ├── fragments/
│   │   └── alumnos/
│   ├── db/migration/         # Scripts Flyway
│   └── application.properties
├── pom.xml                   # Dependencias Maven
└── README.md
```

---

## 🎯 Guía de Uso

### Flujo 1: Crear un Grupo muscular
1. Navega a **Grupos Musculares** → **Nuevo grupo muscular**
2. Rellena nombre
3. Guarda
  
### Flujo 2: Crear un Ejercicio
1. Navega a **Ejercicios** → **Nuevo Ejercicio**
2. Rellena nombre, descripción y selecciona grupo muscular
3. Guarda

### Flujo 3: Crear un Bloque
1. Ve a **Bloques** → **Nuevo Bloque**
2. Asigna nombre
3. **Agrega ejercicios**: selecciona ejercicio, define descanso
4. Guarda

### Flujo 3: Crear una Rutina (Etapa 1)
1. Ve a **Rutinas** → **Nueva Rutina**
2. Nombre, descripción, cantidad de semanas
3. Selecciona bloques y ordénalos por semana
4. Guarda estructura

### Flujo 4: Configurar Reps/Peso por Semana (Etapa 2)
1. Abre la rutina creada
2. Por cada bloque y ejercicio, define **series/reps/peso para cada semana**
3. Guarda

### Flujo 5: Asignar a Alumno
1. Ve a **Alumnos** → selecciona alumno
2. **Asignar Rutina** → elige rutina
3. Se registra la fecha de asignación

### Flujo 6: Exportar PDF
1. En la rutina asignada, haz clic en **Descargar PDF**
2. Se genera un PDF personalizado con:
   - Nombre del alumno
   - Fecha de asignación
   - Rutina completa con observaciones
   - Tablas por bloque con columnas por semana
   - ---

## 🌐 Deployment (Render + Neon - Stack 100% Gratuito)
 
### 🏗️ Arquitectura
 
```
GitHub (Repo)
    ↓
Render.com (Web Service + Docker)
    ↓
Neon.tech (PostgreSQL Serverless)
```
 
### 1️⃣ Preparar Base de Datos en Neon
 
1. **Crear cuenta** en [Neon.tech](https://neon.tech) (gratuito)
2. **Crear proyecto** y base de datos
3. **Copiar connection string**:
   ```
   postgresql://user:password@ep-xxx.us-east-1.neon.tech/gymrutine_db?sslmode=require
   ```
4. Guardar credenciales de forma segura ✅
### 2️⃣ Configurar Docker (Dockerfile en repo)
 
El proyecto incluye `Dockerfile` en la raíz:
 
```dockerfile
FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
 
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```
 
Render automáticamente detecta el Dockerfile y lo construye.
 
### 3️⃣ Desplegar en Render
 
#### Paso 1: Conectar repositorio
- Ve a [render.com](https://render.com)
- Dashboard → **New +** → **Web Service**
- Conecta tu repo GitHub de GymRutine
- Selecciona rama `main`
#### Paso 2: Configurar Web Service
 
| Campo | Valor |
|-------|-------|
| **Name** | `gymrutine` (o similar) |
| **Runtime** | `Docker` |
| **Region** | `Oregon (us-west)` o la más cercana |
| **Branch** | `main` |
 
#### Paso 3: Variables de Entorno
 
En **Environment** agrega:
 
```properties
SPRING_DATASOURCE_URL=postgresql://user:password@ep-xxx.us-east-1.neon.tech/gymrutine_db?sslmode=require
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
SPRING_FLYWAY_LOCATIONS=classpath:db/migration
```
 
#### Paso 4: Deploy
 
Haz clic en **Create Web Service** → Render inicia el build automáticamente ✅
 
El deploy tarda ~3-5 minutos. Podrás ver logs en **Logs** tab.
 
---
## 🗺️ Roadmap

### ✅ Completado
- [x] Todas las entidades (GrupoMuscular, Ejercicio, Bloque, Rutina, Alumno, etc.)
- [x] CRUD completo para cada entidad
- [x] PDF export con iText7
- [x] Autenticación con Spring Security (sesiones)
- [x] Interfaz Thymeleaf + Bootstrap 5

### 📋 Próximos (Futuro)
- [ ] **Historial de cambios** en rutinas
- [ ] **Interfaz móvil responsiva** mejorada
- [ ] **Backup automático** de datos

---

## 📝 Licencia

Este proyecto es de **uso privado** para Joana Román y su equipo de entrenamiento.

Para consultas sobre uso comercial o redistribución, contacta directamente.

---

## 👋 Contacto & Soporte

**Desarrollado por:** Alexis  
**Para:** Joana Román - Entrenadora Personal

---

## 📚 Recursos Útiles

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Docs](https://www.thymeleaf.org/)
- [iText 7 Guide](https://itext.com/developers/itext-7)
- [PostgreSQL Manual](https://www.postgresql.org/docs/)
- [Bootstrap 5 Docs](https://getbootstrap.com/docs/5.0/)

---

**Última actualización:** Abril 2026  
**Estado:** En desarrollo activo 🚀
