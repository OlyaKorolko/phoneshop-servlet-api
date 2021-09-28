package com.es.phoneshop.service;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.viewHistory.ViewHistory;
import com.es.phoneshop.service.impl.DefaultViewHistoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultViewHistoryServiceTest {
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;

    private ViewHistoryService viewHistoryService;
    private ViewHistory viewHistory;
    private Product product;
    private Product product2;

    @Before
    public void setUp() {
        when(request.getSession()).thenReturn(session);

        product = new Product("test", "", new BigDecimal(100), null, 100, null);
        product2 = new Product("test2", "", new BigDecimal(200), null, 200, null);
        viewHistoryService = DefaultViewHistoryService.getInstance();
        viewHistory = new ViewHistory();
        viewHistory.getHistory().add(product);
    }

    @Test
    public void testAddToHistoryAlreadyExists() {
        viewHistoryService.addToHistory(viewHistory.getHistory(), product);

        when(session.getAttribute(any())).thenReturn(viewHistory);
        assertEquals(1, viewHistoryService.getViewHistory(request).getHistory().size());
        assertEquals(viewHistoryService.getViewHistory(request)
                .getHistory().get(viewHistoryService.getViewHistory(request).getHistory().size() - 1).getId(), product.getId());
    }

    @Test
    public void testAddToHistory() {
        viewHistoryService.addToHistory(viewHistory.getHistory(), product2);

        when(session.getAttribute(any())).thenReturn(viewHistory);
        assertEquals(2, viewHistoryService.getViewHistory(request).getHistory().size());
    }

    @Test
    public void testAddToHistoryListIsFull() {
        Product product3 = new Product("test3", "", new BigDecimal(200), null, 300, null);
        Product product4 = new Product("test4", "", new BigDecimal(200), null, 400, null);
        viewHistoryService.addToHistory(viewHistory.getHistory(), product3);
        viewHistoryService.addToHistory(viewHistory.getHistory(), product4);

        when(session.getAttribute(any())).thenReturn(viewHistory);
        assertEquals(viewHistoryService.getViewHistory(request).getHistory().get(0).getId(), product2.getId());
        assertEquals(viewHistoryService.getViewHistory(request).getHistory().get(1).getId(), product3.getId());
        assertEquals(viewHistoryService.getViewHistory(request).getHistory().get(2).getId(), product4.getId());
    }

    @Test
    public void testGetNullViewHistory() {
        when(session.getAttribute(any())).thenReturn(null);
        assertTrue(viewHistoryService.getViewHistory(request).getHistory().isEmpty());
    }

    @Test
    public void testGetViewHistory() {
        when(session.getAttribute(any())).thenReturn(viewHistory);
        assertEquals(1, viewHistoryService.getViewHistory(request).getHistory().size());
    }
}