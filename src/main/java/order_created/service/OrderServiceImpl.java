package order_created.service;

import order_created.controller.dto.OrderResponse;
import order_created.entity.Order;
import order_created.entity.OrderItem;
import order_created.listener.dto.OrderCreatedEvent;
import order_created.repository.OrderRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl {
    private final OrderRepository orderRepository;

    private final MongoTemplate mongoTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Calcula o valor total de um pedido.
     * @param event
     * @return
     */
    private static BigDecimal getTotal(OrderCreatedEvent event) {
        return event.itens()
                .stream()
                .map(item -> item.preco().multiply(BigDecimal.valueOf(item.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.itens().stream().map(item -> new OrderItem(item.produto(), item.quantidade(), item.preco())).toList();
    }

    public void save(OrderCreatedEvent event) {
        final var entity = new Order();
        entity.setOrderId(event.codigoPedido());
        entity.setCustomerId(event.codigoCliente());
        entity.setOrderItems(getOrderItems(event));
        entity.setTotal(getTotal(event));
        orderRepository.save(entity);
    }

    /**
     * Busca todos os pedidos do cliente pelo seu id.
     * Retorna os registros com paginação.
     *
     * @param customerId
     * @param pageRequest
     * @return
     */
    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        final var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(OrderResponse::fromEntity);
    }

    /**
     * Busca o valor total dos pedidos de um cliente pelo seu id
     *
     * @param customerId
     * @return BigDecimal
     */
    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = Aggregation.newAggregation(
                (AggregationOperation) Aggregation.match(Criteria.where("customerId").is(customerId)),
                Aggregation.group().sum("total").as("total")
        );
        // Executa a busca dos registros de acordo com a aggregation criada
        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);
        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }
}
