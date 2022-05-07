<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product Details">
    <p>
        <input type="button" onclick="location.href='${pageContext.servletContext.contextPath}/products'" value="Back"/>
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">${param.message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    <p>${product.description}</p>
    <form method="post">
        <table>
            <tr>
                <td>Image</td>
                <td><img class="product" src="${product.imageUrl}" alt="product-image"></td>
            </tr>
            <tr>
                <td>Code</td>
                <td>${product.code}</td>
            </tr>
            <tr>
                <td>Price</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>Stock</td>
                <td>${product.stock}</td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td>
                    <label>
                        <input name="quantity" value="${not empty error ? param.quantity : 1}" class="quantity"
                               pattern="\d+" required>
                    </label>
                    <c:if test="${not empty error}">
                        <div class="error">${error}</div>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Add to cart</button>
        </p>
    </form>
    <c:if test="${not empty view_history.getHistory()}">
        <p>Recently viewed</p>
    </c:if>
    <ul>
        <c:forEach var="product" items="${view_history.getHistory()}">
            <li class="cell">
                <img class="product-tile" src="${product.imageUrl}" alt="product-image">
                <p>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}
                </p>
                <p>
                    <a href="${pageContext.servletContext.contextPath}/products/price-history/${product.id}">
                            <fmt:formatNumber value="${product.price}" type="currency"
                                              currencySymbol="${product.currency.symbol}"/>
                </p>
            </li>
        </c:forEach>
    </ul>
</tags:master>