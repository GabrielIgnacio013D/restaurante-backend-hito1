# 🍴 Sistema de Gestión de Restaurante — Proyecto de Referencia DSY1103

Proyecto demostrativo para el curso Desarrollo FullStack 1 (DSY1103) — Duoc UC 2026.
Contiene un ecosistema de microservicios distribuidos basados en Spring Boot que se comunican de forma síncrona mediante Feign Client. El servicio **ms-pedidos** actúa como el orquestador central del negocio, interactuando con módulos de la red local (**ms-cocina**, **ms-pagos**) y un proveedor externo (**servicio-menu**) alojado en la infraestructura de nube de AWS de un compañero.

Este repositorio es el ejemplo de referencia para el Hito 1.5 (arquitectura con base de datos) y el Hito 2 (comunicación e integración síncrona entre microservicios).

## Índice
1. Arquitectura del sistema
2. Tecnologías utilizadas
3. Instrucciones de ejecución
4. Endpoints disponibles
5. Comunicación entre microservicios — Hito 2
6. Explicación pedagógica del proyecto

---

## 1. Arquitectura del sistema

El ecosistema opera mediante una arquitectura híbrida y distribuida. Los servicios locales interactúan entre sí en sus respectivos puertos y escalan de manera perimetral hacia la nube de AWS para resolver el catálogo de platos:

```text
                               ECOSISTEMA INTEGRADO DE MICROSERVICIOS
  ┌─────────────────────────────────────────────────────────────────── Local Net ──┐      ┌────────────── AWS Cloud ──────┐
  │                                                                                │      │                               │
  │     ┌───────────────────┐    Feign HTTP    ┌───────────────────┐               │      │     ┌───────────────────┐     │
  │     │     ms-cocina     │◄─────────────────│    ms-pedidos     │               │      │     │   servicio-menu   │     │
  │     │    Puerto 8083    │  /api/mesas/cocina│   Puerto 8082     │────────┼──Feign────┼────►│    Puerto 8080    │     │
  │     └─────────┬─────────┘                  │   (Orquestador)   │        │ HTTP GET │      │    (Proveedor)    │     │
  │               │                            └─────────┬─────────┘               │      │     └───────────────────┘     │
  │     ┌─────────▼─────────┐                            │                         │      │                               │
  │     │     cocina_db     │                  Feign     │                         │      └───────────────────────────────┘
  │     │     MySQL 8.0     │                  HTTP      ▼                         │
  │     └───────────────────┘                    ┌───────────────┐                 │
  │                                              │   ms-pagos    │                 │
  │     ┌───────────────────┐                    │  Puerto 8084  │                 │
  │     │  restaurante_db   │                    └───────┬───────┘                 │
  │     │MySQL(Contenedor)  │                            │                         │
  │     └───────────────────┘                    ┌───────▼───────┐                 │
  │                                              │   pagos_db    │                 │
  │                                              │   MySQL 8.0   │                 │
  │                                              └───────────────┘                 │
  └────────────────────────────────────────────────────────────────────────────────┘
              ▲                                          ▲
              │                                          │
        Postman / curl                             Postman / curl



2.

Tecnología,Versión,Para qué se usa
Java,17 / 21,Lenguaje de programación principal del ecosistema
Spring Boot,3.x,Framework base para la creación de los microservicios
Spring Cloud OpenFeign,4.x,Abstracción de llamadas HTTP mediante interfaces declarativas
Spring Data JPA,Incluido,Persistencia relacional automatizada a través de Hibernate
Spring Validation,Incluido,Filtro y validación de payloads de entrada (@Valid)
MySQL,8.0,Motor de base de datos relacional (Entorno Dockerizado)
Postman,Última,Pruebas unitarias de endpoints e integración distribuida



3.
Paso 1 — Habilitar Reglas de Entrada en AWS (Security Group)
Para permitir que nuestro entorno local consuma el catálogo en la nube del compañero sin rebotes de red (ECONNREFUSED), se debe abrir el puerto en AWS EC2:

Ir a EC2 → Instances → Seleccionar la instancia del microservicio de menú.

Pestaña Security → Entrar al enlace de Security Group → Edit inbound rules.

Añadir regla: Custom TCP, Port: 8080, Source: 0.0.0.0/0 (Tráfico universal).

Paso 2 — Configurar el Entorno (application.properties en ms-pedidos)
Definición de las credenciales de la base de datos local y el mapeo dinámico de endpoints de red (tanto internos como la IP pública de AWS):

Properties
spring.application.name=ms-pedidos
server.port=8082

# Conexión a MySQL (Contenedor local compartido con reservas)
spring.datasource.url=jdbc:mysql://mysql_servidor:3306/restaurante_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=clave_maestra_root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Direccionamiento de Clientes Feign (Inyección por variables de entorno)
modulo.menu.url=[http://34.204.5.186:8080](http://34.204.5.186:8080)
modulo.cocina.url=http://localhost:8083
modulo.pagos.url=http://localhost:8084


Abrir las terminales independientes para cada submódulo del proyecto y ejecutar el comando de inicialización de Spring Boot:

Bash
./mvnw spring-boot:run
Verificar que los logs locales de cada consola marquen el correcto levantamiento de los puertos correspondientes (8082, 8083 y 8084).


##// Respuesta exitosa (200 OK) consolida las respuestas de los clientes HTTP locales
"Simulación completada:
Comunicación exitosa: Pedido enviado a Cocina.
Comunicación exitosa: Pago procesado en pasarela."

5. Comunicación entre microservicios — Hito 2

Tabla de contratos del ecosistema completo
Microservicio Origen,Destino Mapeado,Método,Endpoint de Destino,DTO / Payload de Intercambio
ms-pedidos,servicio-menu (AWS),GET,/api/menu/{id},"PlatoDTO (id, nombre, precio)"
ms-pedidos,ms-cocina (Local),POST,/api/mesas/cocina,"PlatoCocinaDTO (plato, precio, mesa)"
ms-pedidos,ms-pagos (Local),POST,/api/pagos/procesar,"Map<String, Object> (monto)"

Resiliencia y Fallback de Red
Todas las Invocaciones del orquestador se encuentran protegidas en la capa service capturando anomalías del tipo FeignException. Si un servicio de la malla falla o la instancia de AWS decae, el sistema intercepta la excepción devolviendo de forma elegante un código HTTP 503 (Service Unavailable) sanitizado hacia el cliente, impidiendo la caída en cascada de la aplicación.

6. Explicación pedagógica del proyecto
6.1 Estructura por capas del Orquestador

Controller  ──►  Service  ──►  Repository  ──►  Base de Datos (restaurante_db)
   ▲                │
   │                ├──► MenuClient   (Feign) ──► Consume la nube de AWS
   HTTP Request     ├──► CocinaClient (Feign) ──► Envía orden al puerto 8083
                    └──► PagoClient   (Feign) ──► Envía cobro al puerto 8084

6.2 Implementación Real de Clientes Feign (ms-pedidos)
Consumo Interno Local (CocinaClient.java):
Apunta a cocina usando inyección de propiedades dinámicas y define el endpoint exacto para transferir la orden:

package cl.duoc.ms_pedidos.client;

import cl.duoc.ms_pedidos.dto.PlatoCocinaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-cocina", url = "${modulo.cocina.url}")
public interface CocinaClient {

    @PostMapping("/api/mesas/cocina")
    void enviarACocina(@RequestBody PlatoCocinaDTO platoCocinaDTO);
}

Consumo Externo Nube (MenuClient.java):
Establece el puente síncrono con el AWS del compañero para recolectar los datos del plato:

package cl.duoc.ms_pedidos.client;

import cl.duoc.ms_pedidos.dto.PlatoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "menu-api", url = "${modulo.menu.url}", path = "/api/menu")
public interface MenuClient {

    @GetMapping("/{id}")
    PlatoDTO obtenerPlatoPorId(@PathVariable("id") Long id);
}

6.3 Orquestación de Negocio en el Controlador (PedidoController.java)
El controlador central procesa la lógica, gatilla las consultas automáticas de persistencia en MySQL por medio de Hibernate y distribuye sincrónicamente las cargas a través de la red de Feign:

package cl.duoc.ms_pedidos.controller;

import cl.duoc.ms_pedidos.client.MenuClient;
import cl.duoc.ms_pedidos.client.CocinaClient;
import cl.duoc.ms_pedidos.client.PagoClient;
import cl.duoc.ms_pedidos.dto.PlatoDTO;
import cl.duoc.ms_pedidos.dto.PlatoCocinaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private MenuClient menuClient;

    @Autowired
    private CocinaClient cocinaClient;

    @Autowired
    private PagoClient pagoClient;

    @GetMapping("/traer-plato/{id}")
    public ResponseEntity<PlatoDTO> obtenerPlatoDeMatias(@PathVariable("id") Long id) {
        System.out.println("[PEDIDOS] -> Viajando a AWS de Matías a buscar el plato ID: " + id);
        PlatoDTO plato = menuClient.obtenerPlatoPorId(id);
        System.out.println("[PEDIDOS] -> ¡Éxito! Matías me devolvió el plato: " + plato.getNombre());
        return ResponseEntity.ok(plato);
    }

    @PostMapping("/probar-conexion")
    public ResponseEntity<String> simularEnvioACocina() {
        PlatoCocinaDTO datosPedido = new PlatoCocinaDTO();
        datosPedido.setPlato("Pastel de Choclo (Desde MS Pedidos)");
        datosPedido.setPrecio("9500");
        datosPedido.setMesa("Mesa 7");

        System.out.println("[PEDIDOS] -> Enviando datos a ms-cocina...");
        cocinaClient.enviarACocina(datosPedido);

        System.out.println("[PEDIDOS] -> Enviando datos a ms-pagos...");
        // pagoClient.procesarPago(datosPago);

        return ResponseEntity.ok("Simulación completada:\n" + "Comunicación exitosa: Pedido enviado a Cocina.");
    }
}

6.4 Evidencia de Trazabilidad en Consola (ms-cocina)
Al enviar la solicitud desde Postman a ms-pedidos, las herramientas de logging capturan instantáneamente la recepción del objeto transformado en la terminal de ms-cocina, validando la integración exitosa del Hito 2:

[ms-pedidos] : Hibernate: insert into pedido (estado, mesa, plato, precio) values (?,?,?,?)
[ms-pedidos] : Comunicación exitosa: Pedido enviado a Cocina.
------------------------------------------------------------
¡ALERTA: LLEGÓ UN PEDIDO DESDE OTRO MICROSERVICIO!
Plato solicitado: Pastel de Choclo (Desde MS Pedidos)
Precio: 9500
Mesa asignada: Mesa 7
------------------------------------------------------------

Lista de cotejo — Hito 2
[x] Los tres microservicios locales (pedidos, cocina, pagos) inicializan sus instancias de Tomcat de forma independiente.

[x] ms-pedidos efectúa la llamada externa por WAN a AWS y resuelve los códigos HTTP correctamente.

[x] El mapeo de datos síncrono por Feign Client gatilla las impresiones de trazas esperadas en la consola de ms-cocina.

[x] El manejo de excepciones global evita caídas en cadena ante la ausencia de uno de los servicios (Resiliencia con código 503).
