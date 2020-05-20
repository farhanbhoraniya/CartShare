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
import com.cmpe275.CartShare.dao.PoolRepository;
import com.cmpe275.CartShare.model.Pool;
import com.cmpe275.CartShare.model.User;
import com.cmpe275.CartShare.service.PoolService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PoolServiceTest {

	@Autowired
	private PoolService poolService;
	
	@MockBean
	private PoolRepository poolRepository;
	
	@MockBean
	private PoolMembershipRepository poolMembershipRepository;
	
	Pool pool = new Pool("test", "testpool", "test neighborhood", "description", "test", new User());
	
	@Test
	public void findByName() {
		Mockito.when(poolRepository.findByName("testpool")).thenReturn(pool);
		Mockito.when(poolMembershipRepository.findByPool(Mockito.any())).thenReturn(null);
		Pool poolNew = poolService.findByName("testpool");
		assertEquals(poolNew.getName(), pool.getName());
	}
	
	@Test
	public void findById() {
		Mockito.when(poolRepository.findById("test")).thenReturn(pool);
		Mockito.when(poolMembershipRepository.findByPool(Mockito.any())).thenReturn(null);
		Pool poolNew = poolService.findById("test");
		assertEquals(poolNew.getId(), pool.getId());
	}
	
	@Test
	public void save() {
		Mockito.when(poolRepository.save(Mockito.any())).thenReturn(pool);
		Mockito.when(poolRepository.findByName(Mockito.any())).thenReturn(pool);
		Pool poolNew = poolService.save(new Pool("test", "testpool", "test neighborhood", "description", "test", new User()));
		assertEquals(poolNew.getId(), pool.getId());
	}
}
