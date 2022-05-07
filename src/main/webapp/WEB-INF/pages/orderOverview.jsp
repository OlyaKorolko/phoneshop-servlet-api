<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order Overview">
    <p>
        <input type="button" onclick="location.href='${pageContext.servletContext.contextPath}/products'" value="Back"/>
    </p>
    <h1>Order Overview</h1>
    <c:if test="${not empty order.items}">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description</td>
                <td>Quantity</td>
                <td class="price">Price</td>
            </tr>
            </thead>
            <c:forEach var="cartItem" items="${order.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${cartItem.product.imageUrl}" alt="product-image">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${cartItem.product.id}">
                                ${cartItem.product.description}
                    </td>
                    <td>${cartItem.quantity}</td>
                    <td class="price">
                        <a href="${pageContext.servletContext.contextPath}/products/price-history/${cartItem.product.id}">
                                <fmt:formatNumber value="${cartItem.product.price}" type="currency"
                                                  currencySymbol="${cartItem.product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <p>Subtotal cost:
            <fmt:formatNumber value="${order.subtotalCost}" type="currency"
                              currencySymbol="${order.items[0].product.currency.symbol}"/>
        </p>
        <p>Delivery cost:
            <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                              currencySymbol="${order.items[0].product.currency.symbol}"/>
        </p>
        <p>Total cost:
            <fmt:formatNumber value="${order.totalCost}" type="currency"
                              currencySymbol="${order.items[0].product.currency.symbol}"/>
        </p>
        <h2>Your details</h2>
        <table>
            <tags:orderOverviewRow name="firstName" label="First name" order="${order}"/>
            <tags:orderOverviewRow name="lastName" label="Last name" order="${order}"/>
            <tags:orderOverviewRow name="phone" label="Phone" order="${order}"/>
            <tags:orderOverviewRow name="deliveryDate" label="Delivery date" order="${order}"/>
            <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}"/>
            <tr>
                <td>Payment method</td>
                <td>${order.paymentMethod}</td>
            </tr>
        </table>
    </c:if>
</tags:master>