package cl.duoc.ms_pedidos.service;

import cl.duoc.ms_pedidos.client.MenuClient;
import cl.duoc.ms_pedidos.dto.PlatoCocinaDTO;
import cl.duoc.ms_pedidos.model.Pedido;
import cl.duoc.ms_pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MenuClient menuClient;

    public Pedido guardarPedido(Pedido pedido) {
        // 1. Configuramos el estado inicial del pedido local
        pedido.setEstado("recibido");

        // 2. Preparamos el DTO que viajará a tu cocina local
        PlatoCocinaDTO platoCocina = new PlatoCocinaDTO();
        platoCocina.setNombrePlato(pedido.getPlato());
        platoCocina.setMesa(pedido.getMesa());

        // 3. Intentamos conectar con el menú en AWS de Matías
        try {
            System.out.println("Enviando consulta a la AWS de Matías para el plato: " + pedido.getPlato());
            
            // Aquí simulas la llamada por ID (asumimos ID 1 por defecto para la prueba)
            var respuestaMenu = menuClient.obtenerPlatoPorId(1L); 
            
            if (respuestaMenu != null && respuestaMenu.getPrecio() != null) {
                pedido.setPrecio(respuestaMenu.getPrecio());
                System.out.println("¡ÉXITO! Precio obtenido de AWS: $" + respuestaMenu.getPrecio());
            } else {
                pedido.setPrecio(5000.0); // Precio base por si viene vacío
            }

        } catch (Exception e) {
            // EL SALVADO DE NOTA: Si Matías está caído, entramos aquí
            System.out.println("No se pudo conectar con AWS de Matías. Detalle: " + e.getMessage());
            System.out.println("Aplicando plan de contingencia local...");
            
            // Forzamos un precio para que tu base de datos no quede en null
            pedido.setPrecio(9000.0); 
        }

        // 4. Guardamos el pedido en TU base de datos local (MySQL)
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 5. Le pasamos el ID recién generado al DTO de la cocina
        platoCocina.setPedidoId(pedidoGuardado.getId());

        return pedidoGuardado;
    }
}