package com.cmpe275.CartShare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmpe275.CartShare.dao.OrderRepository;
import com.cmpe275.CartShare.model.Order;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.service.OrderService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CartShareApplication.class)
public class OrderServiceTest {

	@Autowired
	private OrderService orderService;
	
	@MockBean
	private OrderRepository orderRepository;
	
	Order order = new Order(new Pool(), new Date(), "test status", new User(), new User(), false);
	
	@Test
	public void findById() {
		Mockito.when(orderRepository.findById(1)).thenReturn(order);
		Order newOrder = orderService.findByOrderId(1);
		assertEquals(newOrder.getBuyerid(), order.getBuyerid());
	}
	
	@Test
	public void save() {
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(order);
		Order newOrder = orderService.save(new Order(new Pool(), new Date(), "test status", new User(), new User(), false));
		assertEquals(newOrder.getBuyerid(), order.getBuyerid());
	}

}
