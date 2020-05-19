package com.cmpe275.CartShare;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CartShareApplicationTests {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    void updateItemInCartTest() throws Exception {
//        mockMvc.perform(post("/updateItemInCart")
//                .con)
    }

}
