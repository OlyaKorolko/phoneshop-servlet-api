<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <p>
    <input id="cart-button" type="button"
           onclick="location.href='${pageContext.servletContext.contextPath}/cart'" value="Cart"/>
  </p>
  <form>
    <input name="query" value="${param.query}" placeholder="Search product...">
    <button>Search</button>
  </form>
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
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
        Description
        <tags:sortLink sortBy="description" order="asc"/>
        <tags:sortLink sortBy="description" order="desc"/>
        </td>
        <td>
          Quantity
        </td>
        <td class="price">
        Price
        <tags:sortLink sortBy="price" order="asc"/>
        <tags:sortLink sortBy="price" order="desc"/>
        </td>
        <td></td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}" varStatus="status">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
        ${product.description}
        </td>
        <td>
          <c:set var="error" value="${errors[product.id]}"/>
          <input id="quantity" form="addItemToCart" name="quantity"
            value="${not empty error ? paramValues['quantity'][status.index] : 1}">
            <c:if test="${not empty error}">
              <div class="error">
                ${error}
              </div>
            </c:if>
          <input type="hidden" name="productId" value="${product.id}" form="addItemToCart"/>
        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/products/price-history/${product.id}">
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
        <td>
          <button type="submit" form="addItemToCart"
                  formaction="${pageContext.servletContext.contextPath}/products/add-to-cart/${product.id}">
            Add to cart
          </button>
        </td>
      </tr>
    </c:forEach>
  </table>

  <form method="post" id="addItemToCart">
  </form>

  <c:if test="${not empty view_history.history}">
    <p>Recently viewed</p>
  </c:if>
  <ul>
    <c:forEach var="product" items="${view_history.history}">
      <li class="cell">
        <img class="product-tile" src="${product.imageUrl}">
          <p>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
            ${product.description}
          </p>
          <p>
            <a href="${pageContext.servletContext.contextPath}/products/price-history/${product.id}">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </p>
      </li>
    </c:forEach>
  </ul>
</tags:master>