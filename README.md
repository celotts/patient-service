# 🏥 Microservicio Patient - Spring Boot

## 📖 Descripción

El **Microservicio Patient** es un componente del sistema de gestión de citas médicas desarrollado en **Spring Boot**. Este microservicio se encarga específicamente de la gestión de pacientes, siguiendo la arquitectura de microservicios con bases de datos independientes.

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.2** (Web, Data JPA, Security, Cloud Config, Eureka Client)
- **PostgreSQL 14**
- **Docker & Docker Compose**
- **Maven**

## 📦 Dependencias

- `spring-boot-starter-actuator` - Monitoreo y métricas
- `spring-boot-starter-data-jpa` - Persistencia de datos
- `spring-boot-starter-web` - API REST
- `spring-boot-starter-security` - Seguridad
- `spring-cloud-starter-netflix-eureka-client` - Registro de servicio
- `spring-cloud-starter-config` - Configuración centralizada
- `postgresql` - Base de datos
- `lombok` - Reducción de código boilerplate
- `springdoc-openapi` - Documentación API

---

## 🏗️ Arquitectura

Este microservicio sigue el patrón "Database per Service", donde cada microservicio tiene su propia base de datos independiente. Este enfoque proporciona:

- **Aislamiento de datos**: Los datos de pacientes se mantienen en una base de datos dedicada
- **Escalabilidad independiente**: El servicio puede escalar según sus propias necesidades
- **Resiliencia**: Los fallos en otros servicios no afectan la disponibilidad de los datos de pacientes

## 🛠️ Configuración del Entorno

Antes de ejecutar el microservicio, asegúrate de tener instalado:

✅ **Docker y Docker Compose**  
✅ **JDK 17 o superior**  
✅ **Maven**  
✅ **IntelliJ IDEA**

---

## ⚙️ Pasos para ejecutar el proyecto

### 🔹 1. Configurar el Config Server (Opcional)

```bash
cd config-server
mvn clean package -DskipTests
cd ..
```

### 🔹 2. Compilar el proyecto

```bash
mvn clean package -DskipTests
```

### 🔹 3. Levantar con Docker Compose

```bash
docker-compose up -d
```

### 🔹 4. Verificar el estado de los servicios

```bash
docker-compose ps
```

### 🔹 5. Comprobar la salud del servicio

```bash
curl http://localhost:8081/actuator/health
```
### 🔹 6. Swagger

```bash
http://localhost:8081/api/swagger-ui/index.html
```

### Config server (UP)
Para que se visualice el config server en Eureka se debe ejecutar el siguiente comando en la raiz del proyecto

```bash 
Hacer click en el boton RUN de inteliji

```
---

### 🔹 6. Ejecutar en modo desarrollo (Opcional)

Para ejecutar el servicio directamente desde IntelliJ:
1. Crear el archivo `src/main/resources/application-local.yml` con la siguiente configuración:
   ```yaml
   server:
     port: 8082

   spring:
     datasource:
       url: jdbc:postgresql://localhost:5433/patient_service
       username: patient
       password: Patient0513
       driver-class-name: org.postgresql.Driver
     jpa:
       hibernate:
         ddl-auto: validate

   eureka:
     client:
       service-url:
         defaultZone: http://localhost:8761/eureka/

   management:
     endpoints:
       web:
         exposure:
           include: "*"
     endpoint:
       health:
         show-details: always
   ```
2. Ejecutar la aplicación con el perfil "local"

---

## 📡 Endpoints API REST

Una vez iniciado el servicio, accede a la documentación **Swagger**:

🔹 [Swagger UI](http://localhost:8081/swagger-ui.html)  
🔹 [OpenAPI JSON](http://localhost:8081/v3/api-docs)

**Principales Endpoints:**

| Método     | Endpoint        | URL                                      | Descripción                |
|------------|-----------------|------------------------------------------|----------------------------|
| **POST**   | createPatient   | `http://localhost:8081/v1/patients`      | Crear un nuevo paciente    |
| **GET**    | allPatients     | `http://localhost:8081/v1/patients`      | Listar todos los pacientes |
| **GET**    | getPatientById  | `http://localhost:8081/v1/patients/{id}` | Obtener paciente por ID    |
| **PUT**    | updatePatient   | `http://localhost:8081/v1/patients/{id}` | Actualizar un paciente     |
| **DELETE** | deletePatient   | `http://localhost:8081/v1/patients/{id}` | Eliminar un paciente       |

## 🐳 Estructura Docker

El proyecto utiliza Docker Compose para orquestar los siguientes servicios:

- **patient-db**: Base de datos PostgreSQL dedicada para el servicio de pacientes
- **patient-service**: El microservicio de gestión de pacientes
- **registry-server**: Servidor Eureka para registro y descubrimiento de servicios
- **config-server**: Servidor de configuración centralizada
- **Microservice-config**: Configuración centralizada para los microservicios, Almacena las credenciales de la bd. La rama es main ruta https://github.com/celotts/microservice-config/blob/main/patient-service.yml
---

## 🚀 Gitflow - Flujo de trabajo con Git

### 🔹 Ramas principales

| Rama      | Descripción                                                 |
|-----------|-------------------------------------------------------------|
| `main`    | Contiene la versión estable del proyecto.                   |
| `develop` | Rama donde se integran los cambios antes de pasar a `main`. |

### 🔹 Ramas de soporte

| Rama        | Descripción                                             |
|-------------|---------------------------------------------------------|
| `feature/*` | Para desarrollar nuevas funcionalidades.                |
| `bugfix/*`  | Para corregir errores en `develop`.                     |
| `release/*` | Para preparar una versión antes de integrarla a `main`. |
| `hotfix/*`  | Para corregir errores críticos en `main`.               |
| `support/*` | Ramas de soporte para mantenimiento a largo plazo.      |

🔹 Tipos de commits en Git

| Tipo         | Descripción                                      | Ejemplo de commit                                                          |
|--------------|--------------------------------------------------|----------------------------------------------------------------------------|
| **feat**     | ✨ Agregar una nueva funcionalidad                | `git commit -m "feat(patient): implementar búsqueda por nombre"`           |
| **fix**      | 🐛 Corregir un error o bug                       | `git commit -m "fix(patient): corregir error en la búsqueda por nombre"`   |
| **refactor** | 🔨 Mejorar código sin cambiar funcionalidad      | `git commit -m "refactor(patient): simplificar lógica de búsqueda"`        |
| **docs**     | 📖 Cambios en documentación                      | `git commit -m "docs(patient): agregar explicación de búsqueda en README"` |
| **test**     | ✅ Agregar o modificar pruebas                    | `git commit -m "test(patient): agregar test unitario para la búsqueda"`    |
| **style**    | 🎨 Mejorar formato (sin cambiar funcionalidad)   | `git commit -m "style(patient): mejorar formato del código"`               |
| **chore**    | 🔧 Mantenimiento o configuración                 | `git commit -m "chore: actualizar dependencias de Spring Boot"`            |
| **perf**     | ⚡ Mejorar rendimiento o eficiencia               | `git commit -m "perf(patient): optimizar consulta SQL de búsqueda"`        |
| **ci**       | 🛠️ Cambios en configuración de CI/CD            | `git commit -m "ci: actualizar pipeline de GitHub Actions"`                |
| **build**    | 🏗️ Configuración de compilación/dependencias    | `git commit -m "build: agregar configuración de Docker Compose"`           |
| **revert**   | 🔄 Revertir un commit anterior                   | `git commit -m "revert: deshacer feat(patient): búsqueda por nombre"`      |




### 🔹 Mensajes de Commit en Gitflow

Para mantener un historial claro, se deben seguir las convenciones de **Conventional Commits**:

```bash
git commit -m "<tipo>(<área>): <descripción corta>"
```

Ejemplo:

```bash
git commit -m "feat(patient): implementar búsqueda por nombre"
```

---
# Instalar dependencias
```bash
npm install --save-dev @commitlint/cli @commitlint/config-conventional husky
```

# Configurar husky
```bash
npm pkg set scripts.prepare="husky install"
npm run prepare
```

# Configurar el hook de commit-msg
```bash
echo "NODE_NO_WARNINGS=1 npx --no -- commitlint --edit \$1" > .husky/commit-msg
chmod +x .husky/commit-msg
```

# Crear configuración de commitlint
```bash
cat > commitlint.config.js << 'EOF'
module.exports = {
   extends: ['@commitlint/config-conventional'],
   rules: {
      'type-enum': [2, 'always', ['build', 'chore', 'ci', 'docs', 'feat', 'fix', 'perf','refactor', 'revert', 'style', 'test']],
      'type-case': [2, 'always', 'lower-case'],
      'type-empty': [2, 'never'],
      'subject-empty': [2, 'never'],
      'subject-full-stop': [2, 'never', '.'],
      'subject-case': [0, 'always', 'sentence-case']
   }
};
EOF
```

---
# Para levantar una instancias se debe hacer los siguientes pasos en el docker-compose:

### 🔹 Paso 1/1 Levantar el docker
- docker-compose down --rmi all
- docker-compose up -d

---
# Para levantar varias instancias se debe hacer lo siguiente en el docker-compose:
### Pasos:

### 🔹 Paso 1/4
### Eliminar el container_name en el Docker-compose

### 🔹 Paso 2/4 - Eliminar PORT en el Docker-compose


### 🔹 Paso 3/4 Bajar el docker
### - docker-compose down --rmi all

### 🔹 Paso 4/4 Levantar el docker con las instancias
### - docker-compose up -d --scale patient-service=3

---

## 👤 Autor

📝 **Carlos Lott**

📜 **Licencia**

Este proyecto está bajo la licencia **MIT**.