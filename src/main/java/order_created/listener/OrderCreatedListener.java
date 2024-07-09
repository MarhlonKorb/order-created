package order_created.listener;

import order_created.listener.dto.OrderCreatedEvent;
import order_created.service.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import static order_created.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

/**
 * Classe configurada para ser a consumidora(listener) do serviço de mensageria de criação de pedidos.
 */
@Component
public class OrderCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    private final OrderServiceImpl orderService;

    public OrderCreatedListener(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    /**
     * Método que será chamado quando uma mensagem for recebida na fila ORDER_CREATED_QUEUE
     * @param message
     */
    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message){
        logger.info("Message consumed: {}", message);
        // Salva o evento de criação do pedido usando o serviço
        orderService.save(message.getPayload());
    }
}
