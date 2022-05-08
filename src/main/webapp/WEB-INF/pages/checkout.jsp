<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <p>
        <input type="button" onclick="location.href='${pageContext.servletContext.contextPath}/products'" value="Back"/>
    </p>
    <p>
    <c:if test="${not empty param.message}">
        <div class="success">${param.message}</div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">An error occurred while placing the order.</div>
    </c:if>
    </p>
    <c:if test="${not empty cart.items}">
        <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
            <table>
                <thead>
                <tr>
                    <td>Image</td>
                    <td>Description</td>
                    <td>Quantity</td>
                    <td class="price">Price</td>
                </tr>
                </thead>
                <c:forEach var="cartItem" items="${cart.items}" varStatus="status">
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
            <p>
            <p>Subtotal cost:
                <fmt:formatNumber value="${order.subtotalCost}" type="currency"
                                  currencySymbol="${cart.items[0].product.currency.symbol}"/>
            </p>
            <p>Delivery cost:
                <fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                  currencySymbol="${cart.items[0].product.currency.symbol}"/>
            </p>
            <p>Total cost:
                <fmt:formatNumber value="${order.subtotalCost + order.deliveryCost}" type="currency"
                                  currencySymbol="${cart.items[0].product.currency.symbol}"/>
            </p>
            </p>
            <h2>Your details</h2>
            <table>
                <tags:orderFormRow name="firstName" label="First name" order="${order}" errors="${errors}"/>
                <tags:orderFormRow name="lastName" label="Last name" order="${order}" errors="${errors}"/>
                <tr>
                    <td>Phone<span style="color:red">*</span></td>
                    <td>
                        <c:set var="error" value="${errors[phone]}"/>
                        <label>
                            <input name="phone" value="${not empty error ? param[phone] : order[phone]}" type="tel"
                                   pattern="(\+?(\d){8,})|((\d){2,}-?\s?){3,}?"/>
                        </label>
                        <c:if test="${not empty error}">
                            <div class="error">${error}</div>
                        </c:if>
                    </td>
                </tr>
                <tags:orderFormRow name="deliveryDate" label="Delivery date" type="date" order="${order}"
                                   errors="${errors}"/>
                <tags:orderFormRow name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}"/>
                <tr>
                    <td>Payment method<span style="color:red">*</span></td>
                    <td>
                        <label>
                            <select name="paymentMethod">
                                <option></option>
                                <c:forEach var="paymentMethod" items="${paymentMethods}">
                                    <option>${paymentMethod}</option>
                                </c:forEach>
                            </select>
                        </label>
                        <c:set var="error" value="${errors['paymentMethod']}"/>
                        <c:if test="${not empty error}">
                            <div class="error">${error}</div>
                        </c:if>
                    </td>
                </tr>
            </table>
            <p>
                <button>Place order</button>
            </p>
        </form>
    </c:if>
</tags:master>