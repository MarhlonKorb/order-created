package order_created.controller;

import order_created.controller.dto.ApiResponse;
import order_created.controller.dto.OrderResponse;
import order_created.controller.dto.Paginationresponse;
import order_created.service.OrderServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class OrderController {

    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pagesize", defaultValue = "10") Integer pageSize) {
        var pageResponse = orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));
        var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);
        return ResponseEntity.ok(new ApiResponse<>(Map.of("totalOnOrders", totalOnOrders), pageResponse.getContent(), Paginationresponse.fromPage(pageResponse)));
    }
}
