package com.cmpe275.CartShare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmpe275.CartShare.dao.PoolMembershipRepository;
import com.cmpe275.CartShare.model.PoolMembership;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.service.PoolMembershipService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CartShareApplication.class)
public class PoolMembershipTest {
	
	@Autowired
	PoolMembershipService poolMembershipService;
	
	@MockBean
	PoolMembershipRepository poolMembershipRepository;
	
	PoolMembership poolMembership = new PoolMembership("test", new User(), 1, false, false);
	
	@Test
	public void findById() {
		Mockito.when(poolMembershipRepository.findById(1)).thenReturn(poolMembership);
		PoolMembership poolMembershipNew = poolMembershipService.findById(1);
		assertEquals(poolMembership.getId(), poolMembershipNew.getId());
	}
	
	@Test
	public void findByUser() {
		Mockito.when(poolMembershipRepository.findByUser(Mockito.any())).thenReturn(poolMembership);
		PoolMembership poolMembershipNew = poolMembershipService.findByUser(new User());
		assertEquals(poolMembership.getId(), poolMembershipNew.getId());
	}

}
