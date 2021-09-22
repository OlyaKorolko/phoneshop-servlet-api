<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
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
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
        ${product.description}
        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/products/price-history/${product.id}">
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
        </td>
      </tr>
    </c:forEach>
  </table>
  <c:if test="${not empty view_history.getHistory()}">
    <p>Recently viewed</p>
  </c:if>
  <ul>
    <c:forEach var="product" items="${view_history.getHistory()}">
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