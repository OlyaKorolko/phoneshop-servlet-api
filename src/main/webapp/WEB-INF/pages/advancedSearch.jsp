<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Advanced search">
  <form name="searchForm" method="post">
    <h2>Advanced search</h2>
    <table class="search">
      <tr class="search">
        <td class="search">Description</td>
        <td class="search"><input name="description" value="${param.description}"</td>
        <td>
          <select name="selector">
            <c:forEach var="word_selector" items="${selectors}">
            <option>${word_selector}</option>
            </c:forEach>
          </select>
        </td>
      </tr>
      <tr class="search">
        <td class="search">Min price</td>
        <td class="search"><input name="min_price" value="${param.min_price}"</td>
      </tr>
      <tr class="search">
        <td class="search">Max price</td>
        <td class="search"><input name="max_price" value="${param.max_price}"</td>
      </tr>
    </table>
    <p>
      <button>Search</button>
    </p>
    <table>
      <thead>
        <tr>
          <td>Image</td>
          <td>
          Description
          </td>
          <td class="price">
          Price
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
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </td>
        </tr>
      </c:forEach>
    </table>
  </form>
</tags:master>