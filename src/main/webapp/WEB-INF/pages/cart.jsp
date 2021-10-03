<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
  <p>
    <input type="button" onclick="location.href='${pageContext.servletContext.contextPath}/products'" value="Back"/>
  </p>
  <p>
    <c:if test="${not empty param.message}">
      <div class="success">
        ${param.message}
      </div>
    </c:if>
    <c:if test="${not empty errors}">
      <div class="error">
        An error occurred while updating the cart.
      </div>
    </c:if>
  </p>
  <c:if test="${not empty cart.items}">
    <form method="post" action="${pageContext.servletContext.contextPath}/cart">
      <table>
        <thead>
          <tr>
            <td>Image</td>
            <td>Description</td>
            <td>Quantity</td>
            <td class="price">Price</td>
            <td></td>
          </tr>
        </thead>
        <c:forEach var="cartItem" items="${cart.items}" varStatus="status">
          <tr>
            <td>
              <img class="product-tile" src="${cartItem.product.imageUrl}">
            </td>
            <td>
            <a href="${pageContext.servletContext.contextPath}/products/${cartItem.product.id}">
            ${cartItem.product.description}
            </td>
            <td>
              <fmt:formatNumber value="${cartItem.quantity}" var="quantity"/>
              <c:set var="error" value="${errors[cartItem.product.id]}"/>
              <input name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : cartItem.quantity}"/>
              <c:if test="${not empty error}">
                <div class="error">
                  ${errors[cartItem.product.id]}
                </div>
              </c:if>
              <input type="hidden" name="productId" value="${cartItem.product.id}"/>
            </td>
            <td class="price">
              <a href="${pageContext.servletContext.contextPath}/products/price-history/${cartItem.product.id}">
              <fmt:formatNumber value="${cartItem.product.price}" type="currency" currencySymbol="${cartItem.product.currency.symbol}"/>
            </td>
            <td>
              <button type="submit" form="deleteCartItem"
                formaction="${pageContext.servletContext.contextPath}/cart/delete-cart-item/${cartItem.product.id}">
                Delete
              </button>
            </td>
          </tr>
        </c:forEach>
      </table>
        <p>
          <p>Total cost:
             <fmt:formatNumber value="${cart.totalCost}" type="currency"
                  currencySymbol="${cart.items[0].product.currency.symbol}"/>
          </p>
        </p>
      <p>
        <button>Update</button>
      </p>
    </form>
    <form action="${pageContext.servletContext.contextPath}/checkout">
      <button>Checkout</button>
    </form>
    <form id="deleteCartItem" method="post"></form>
  </c:if>
</tags:master>