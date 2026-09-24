# ms-rutaexpress-audit

Timeline de auditoría. Consume eventos de Kafka y los persiste.

Spring Boot 3.3.5, Java 17+, Maven (`./mvnw`). Puerto local: **8085**. Responsable: compañero / opencode.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/audit` | timeline con filtros `shipmentId`, `status`, `actor`, `from`, `to` (ISO-8601) |

## Perfiles

- **por defecto (dev)**: H2 en memoria y **sin seguridad** (solo para desarrollo local).
- **`secure`**: valida el JWT de Azure AD (`AZURE_TENANT_ID` + `AZURE_API_AUDIENCE`) y aplica roles `Admin` y `Auditor` desde el claim `roles`.
- **`prod`**: PostgreSQL.

## Variables de entorno

`AZURE_TENANT_ID`, `AZURE_API_AUDIENCE` (GUID de la API, `<API_CLIENT_ID>`; perfil `secure`), KAFKA_BOOTSTRAP, DB_HOST/PORT/NAME/USER/PASS (perfil prod)

## Pruebas

`./mvnw test` ejecuta 16 pruebas: servicio, filtros (@DataJpaTest con H2), API, consumidor Kafka (JSON válido/inválido) y seguridad por perfil `secure`. No necesitan brokers ni base de datos externos (H2 en memoria; los listeners de Kafka se desactivan en los tests).

## Ejecutar

```bash
./mvnw test
./mvnw spring-boot:run
SPRING_PROFILES_ACTIVE=secure AZURE_TENANT_ID=<tenant> AZURE_API_AUDIENCE=<api-client-id> ./mvnw spring-boot:run
```

Los DTOs compartidos están copiados en `src/main/java/com/rutaexpress/contracts`; la fuente de verdad de los contratos está en el repo `Cloud-Native-1` (`contratos/`).
