package com.example.borrow;

import com.example.borrow.model.BorrowRecord;
import com.example.borrow.repository.BorrowRepository;
import com.example.borrow.service.BorrowService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class BorrowServiceTest {
    @Test
    void verifiesUserAndBookAndMarksBookUnavailable() {
        BorrowRepository repository = mock(BorrowRepository.class);
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(once(), requestTo("http://user-service/users/7"))
                .andRespond(withSuccess("{\"id\":7,\"name\":\"Ada\",\"email\":\"ada@example.com\"}", MediaType.APPLICATION_JSON));
        server.expect(once(), requestTo("http://book-service/books/3"))
                .andRespond(withSuccess("{\"id\":3,\"title\":\"DDD\",\"author\":\"Evans\",\"available\":true}", MediaType.APPLICATION_JSON));
        server.expect(once(), requestTo("http://book-service/books/3/availability"))
                .andExpect(method(org.springframework.http.HttpMethod.PUT))
                .andExpect(content().json("{\"available\":false}"))
                .andRespond(withSuccess());
        when(repository.existsByBookIdAndStatus(3L, BorrowRecord.Status.BORROWED)).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> {
            BorrowRecord record = invocation.getArgument(0); record.setId(1L); return record;
        });

        BorrowRecord record = new BorrowService(repository, restTemplate).borrow(3L, 7L);

        assertEquals(BorrowRecord.Status.BORROWED, record.getStatus());
        assertEquals(3L, record.getBookId());
        assertEquals(7L, record.getUserId());
        server.verify();
    }
}
