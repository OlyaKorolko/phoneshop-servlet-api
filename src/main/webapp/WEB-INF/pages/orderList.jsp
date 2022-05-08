<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="items" type="java.util.Map" scope="request"/>
<tags:master pageTitle="Order List">
    <ul>
        <c:forEach var="orderEntry" items="${items.entrySet()}">
            <li>
                <p>First name: ${orderEntry.key.firstName}</p>
                <p>Last name: ${orderEntry.key.lastName}</p>
                <p>Phone: ${orderEntry.key.phone}</p>
                <p>Delivery date: ${orderEntry.key.deliveryDate}</p>
                <p>Delivery address: ${orderEntry.key.deliveryAddress}</p>
                <p>Payment method: ${orderEntry.key.paymentMethod}</p>
                <p>Subtotal cost: ${orderEntry.key.subtotalCost}</p>
                <p>Delivery cost: ${orderEntry.key.deliveryCost}</p>

                <table>
                    <thead>
                    <tr>
                        <td>Image</td>
                        <td>
                            Description
                            <tags:sortLink sortBy="description" order="asc"/>
                            <tags:sortLink sortBy="description" order="desc"/>
                        </td>
                        <td class="price">
                            Price
                            <tags:sortLink sortBy="price" order="asc"/>
                            <tags:sortLink sortBy="price" order="desc"/>
                        </td>
                        <td>Quantity</td>
                    </tr>
                    </thead>
                    <c:forEach var="cartItem" items="${orderEntry.value}" varStatus="status">
                        <tr>
                            <td><img class="product-tile" src="${cartItem.product.imageUrl}" alt="product-image"></td>
                            <td>
                                <a href="${pageContext.servletContext.contextPath}/products/${cartItem.product.id}">
                                        ${cartItem.product.description}
                            </td>
                            <td class="price">
                                <a href="${pageContext.servletContext.contextPath}/products/price-history/${cartItem.product.id}">
                                        <fmt:formatNumber value="${cartItem.product.price}" type="currency"
                                                          currencySymbol="${cartItem.product.currency.symbol}"/>
                            </td>
                            <td>${cartItem.quantity}</td>
                        </tr>
                    </c:forEach>
                </table>
            </li>
        </c:forEach>
    </ul>
</tags:master>