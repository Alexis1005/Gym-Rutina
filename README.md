# 🏋️ GymRutine

> Una aplicación web para gestionar rutinas de entrenamiento personalizadas con generación automática de PDFs

**GymRutine** es una herramienta diseñada para entrenadores personales como que necesitan:
- ✅ Crear rutinas de entrenamiento organizadas por día y con progresión semanal
- ✅ Buscar ejercicios filtrando por grupo muscular y sus clasificaciones
- ✅ Asignar rutinas a alumnos
- ✅ Generar PDFs personalizados listos para imprimir

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tech Stack](#-tech-stack)
- [Modelo de datos](#-modelo-de-datos)
- [Video Demo](#-video-demo)
- [Instalación](#-instalación)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Datos iniciales](#-datos-iniciales)
- [Guía de Uso](#-guía-de-uso)
- [Deployment](#-deployment)
- [Roadmap](#-roadmap)
- [Licencia](#-licencia)

---

## ✨ Características

### 📝 Gestión de Ejercicios y Grupos Musculares
- Catálogo de ejercicios organizado por grupo muscular (Bíceps, Espalda, Pectoral, Tríceps, Hombros, Antebrazos, Cuádriceps, Isquiotibiales, Glúteos, Pantorrillas, Abdomen, Lumbares y Pliometría)
- Cada ejercicio se clasifica por **posición**, **elemento**, **cadena cinética**, **lateralidad** y **tipo articular** (monoarticular, biarticular o multiarticular)

### 💪 Rutinas Personalizadas
- Rutinas con cantidad de semanas y de días definida al crearlas
- Ejercicios ordenados dentro de cada día
- Progresión semanal por ejercicio: series, repeticiones, peso, descanso y RIR
- Buscador de ejercicios por día, con filtros combinables y resultados en orden alfabético
- Edición de la rutina: nombre, observaciones y ejercicios (las semanas y los días quedan fijos)
- Una rutina asignada a alumnos no se puede eliminar

### 👥 Gestión de Alumnos
- Registro de alumnos con sus observaciones
- Asignación de rutinas y desasignación
- Historial de rutinas asignadas, con fecha

### 📄 Exportación a PDF
- PDF horizontal (A4) personalizado por alumno
- Encabezado con logo, nombre del alumno y fecha de asignación
- Nombre de la rutina y observaciones
- Una tabla por día, con un grupo de columnas por semana (Series, Reps, Kg, Descanso, RIR)
- Paginación automática (máx. 2 días por página)
- Diseño en azul con acento rojo, en línea con el logo

### 🔐 Seguridad
- Autenticación basada en sesiones (Spring Security)
- Protección CSRF en todos los formularios

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

## 📊 Modelo de datos

El siguiente diagrama muestra las entidades principales y sus relaciones:

```mermaid
erDiagram
    GRUPO_MUSCULAR ||--o{ EJERCICIO : "clasifica"
    EJERCICIO ||--o{ RUTINA_EJERCICIO : "se usa en"
    RUTINA ||--o{ RUTINA_EJERCICIO : "contiene"
    RUTINA_EJERCICIO ||--o{ RUTINA_EJERCICIO_SEMANA : "progresa por semana"
    ALUMNO ||--o{ ASIGNACION_RUTINA : "recibe"
    RUTINA ||--o{ ASIGNACION_RUTINA : "se asigna en"

    GRUPO_MUSCULAR {
        string nombre
    }
    EJERCICIO {
        string nombre
        enum posicion
        enum elemento
        enum cadenaCinetica
        enum lateralidad
        enum tipoArticular
    }
    RUTINA {
        string nombre
        string descripcion
        int cantidadSemanas
        int cantidadDias
    }
    RUTINA_EJERCICIO {
        int dia
        int orden
    }
    RUTINA_EJERCICIO_SEMANA {
        int semana
        int series
        string repeticiones
        string pesoKg
        string descansoMinutos
        int rir
    }
    ALUMNO {
        string nombreApellido
        string observaciones
    }
    ASIGNACION_RUTINA {
        date fechaAsignacion
    }
```

### Entidades Principales

| Entidad | Descripción |
|---------|-----------|
| **GrupoMuscular** | Clasificación principal de los ejercicios (pectoral, espalda, piernas, etc.) |
| **Ejercicio** | Movimiento específico, con sus clasificaciones (posición, elemento, cadena cinética, lateralidad y tipo articular) |
| **Rutina** | Plan de entrenamiento con cantidad de semanas y de días |
| **RutinaEjercicio** | Ubicación de un ejercicio dentro de la rutina: en qué día y en qué orden |
| **RutinaEjercicioSemana** | Progresión de ese ejercicio en cada semana: series, repeticiones, peso, descanso y RIR |
| **Alumno** | Cliente del entrenador |
| **AsignacionRutina** | Vinculación entre un alumno y una rutina, con fecha |

> 📌 **Nota**: `RutinaEjercicio`, `RutinaEjercicioSemana` y `AsignacionRutina` son entidades intermedias que permiten relaciones muchos-a-muchos con datos propios.

---

## 🎥 Video Demo

¿Quieres ver GymRutine en acción?

🚀 **[Ver video demo de la aplicación](https://youtube.com/tu-canal-aqui)** *(próximamente)*

En el video podrás observar:
- 📌 Gestión de ejercicios y grupos musculares
- 📌 Armado de una rutina con búsqueda de ejercicios por filtros
- 📌 Carga de la progresión semanal
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

#### 3. Configurar los perfiles de Spring

El proyecto usa perfiles. Por defecto se activa `local`:

```properties
# application.properties
spring.profiles.active=local
```

La configuración propia de cada entorno va en su archivo de perfil (por ejemplo `application-local.properties`, que **no se versiona** porque contiene credenciales):

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

> En producción **no se modifica** `spring.profiles.active` en el archivo: se activa el perfil `prod` con la variable de entorno `SPRING_PROFILES_ACTIVE=prod` (ver [Deployment](#-deployment)).

#### 4. Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```

Al arrancar, Flyway aplica las migraciones pendientes, incluida la carga inicial de ejercicios.

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
│   ├── service/              # Lógica de negocio (incluye la generación del PDF)
│   ├── repository/           # Acceso a datos (JPA)
│   ├── exception/
│   ├── model/                # Entidades JPA
│   │   └── enums/            # Posición, Elemento, CadenaCinetica, Lateralidad, TipoArticular
│   ├── dto/                  # Data Transfer Objects
│   │   ├── grupoMuscular/
│   │   ├── ejercicio/
│   │   ├── rutina/
│   │   ├── asignacionRutina/
│   │   └── alumno/
│   └── config/               # Configuración (Spring Security, etc.)
├── src/main/resources/
│   ├── static/               # Logo y favicon
│   ├── templates/            # Plantillas Thymeleaf
│   │   ├── grupos-musculares/
│   │   ├── ejercicios/
│   │   ├── rutina/
│   │   ├── error/
│   │   ├── fragments/
│   │   └── alumnos/
│   ├── db/migration/         # Scripts Flyway (estructura y datos iniciales)
│   └── application.properties
├── pom.xml                   # Dependencias Maven
└── README.md
```

---

## 🌱 Datos iniciales

El catálogo de ejercicios se carga con migraciones de Flyway, **un archivo por grupo muscular**, y los tipos articulares se completan con migraciones de actualización, también por grupo. Se aplican automáticamente al arrancar, tanto en local como en producción.

Reglas para agregar o corregir datos:

- **Nunca se edita una migración ya aplicada**: Flyway guarda un checksum y se niega a arrancar si el archivo cambia. Cualquier corrección va en una migración nueva con la versión siguiente.
- Los ejercicios se insertan con `WHERE NOT EXISTS`, para no duplicarse si ya existen.
- Cada ejercicio busca su grupo muscular por nombre, sin IDs fijos.
- Los valores de los enums se escriben exactamente como las constantes de Java (por ejemplo `DE_PIE`, `MULTIARTICULAR`).

---

## 🎯 Guía de Uso

### Flujo 1: Crear un Grupo muscular
1. Navega a **Grupos Musculares** → **Nuevo grupo muscular**
2. Rellena el nombre
3. Guarda

### Flujo 2: Crear un Ejercicio
1. Navega a **Ejercicios** → **Nuevo Ejercicio**
2. Rellena el nombre y selecciona el grupo muscular
3. Define sus clasificaciones: posición, elemento, cadena cinética, lateralidad y tipo articular
4. Guarda

### Flujo 3: Crear una Rutina
1. Ve a **Rutinas** → **Nueva Rutina**
2. Completa nombre, observaciones, cantidad de semanas y cantidad de días
3. Toca **Generar días**
4. En cada día:
   - Filtra por grupo muscular y clasificaciones, y toca **Buscar ejercicios**
   - Elige el ejercicio de la lista (aparece en orden alfabético)
   - Toca **Cargar progresión** y completa series, repeticiones, kg, descanso y RIR de cada semana
   - Toca **Agregar ejercicio a este día**
5. Guarda la rutina

> Las semanas y los días no se pueden modificar una vez creada la rutina.

### Flujo 4: Editar una Rutina
1. Abre la rutina → **Editar**
2. Modifica el nombre, las observaciones o los ejercicios de cada día
3. Con **Editar** en un ejercicio ya cargado cambias su progresión; con **Eliminar** lo quitas
4. Guarda los cambios

### Flujo 5: Asignar a Alumno
1. Ve a **Alumnos** → selecciona un alumno (o abre la rutina)
2. **Asignar Rutina** → elige la rutina
3. Se registra la fecha de asignación

### Flujo 6: Exportar PDF
1. En la rutina asignada al alumno, haz clic en **Exportar PDF**
2. Se genera un PDF horizontal con:
   - Logo, nombre del alumno y fecha de asignación
   - Nombre de la rutina y observaciones
   - Una tabla por día, con columnas por semana (Series, Reps, Kg, Descanso, RIR)

---

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
3. **Copiar los datos de conexión** (host, base, usuario y contraseña):
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
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://ep-xxx.us-east-1.neon.tech/gymrutine_db?sslmode=require
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
SPRING_FLYWAY_LOCATIONS=classpath:db/migration
```

> La URL de conexión lleva el prefijo `jdbc:` y **sin** usuario ni contraseña: esos van en sus propias variables.

#### Paso 4: Deploy

Haz clic en **Create Web Service** → Render inicia el build automáticamente ✅

El deploy tarda ~3-5 minutos. Podrás ver logs en **Logs** tab. En cada arranque, Flyway aplica en Neon las migraciones que falten.

### ⏱️ Arranque en frío

En el plan gratuito, Render apaga el servicio tras unos 15 minutos sin tráfico, y la primera visita posterior puede tardar cerca de un minuto en responder. Neon también suspende su cómputo por inactividad. Para evitarlo se puede usar un plan pago de Render o un monitor externo que consulte periódicamente la página `/login`.

---

## 🗺️ Roadmap

### ✅ Completado
- [x] Entidades principales (GrupoMuscular, Ejercicio, Rutina, Alumno y sus intermedias)
- [x] CRUD completo para cada entidad
- [x] Rutinas por día con progresión semanal y buscador de ejercicios por filtros
- [x] Clasificación de ejercicios (posición, elemento, cadena cinética, lateralidad y tipo articular)
- [x] Carga inicial del catálogo de ejercicios con migraciones de Flyway
- [x] PDF export con iText 7, con diseño propio
- [x] Autenticación con Spring Security (sesiones)
- [x] Interfaz Thymeleaf + Bootstrap 5
- [x] Deploy en Render + Neon

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
- [Flyway Documentation](https://documentation.red-gate.com/flyway)
- [PostgreSQL Manual](https://www.postgresql.org/docs/)
- [Bootstrap 5 Docs](https://getbootstrap.com/docs/5.0/)

---

**Última actualización:** Octubre 2026
**Estado:** En desarrollo activo 🚀
