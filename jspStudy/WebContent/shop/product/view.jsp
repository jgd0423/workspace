<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../include/inc_header.jsp" %>

<table border="1" align="center" width="80%">
	<tr>
		<td colspan="2">
			<h1>상품상세보기</h1>
		</td>
	</tr>
	<tr>
		<td width="150">상품코드 : </td>
		<td>${dto.no }</td>
	</tr>
	<tr>
		<td>상품사진 : </td>
		<td>
			<c:if test="${dto.product_img == '-,-,-' }">
				이미지X
			</c:if>
			<c:if test="${dto.product_img != '-,-,-' }">
				<c:set var="temp1" value="${fn:split(dto.product_img, ',')[0] }"></c:set>
				<c:set var="temp2" value="${fn:split(temp1, '|')[0] }"></c:set>
				<c:set var="temp3" value="${fn:split(temp1, '|')[1] }"></c:set>
				<img 
					src="${path }/attach/product_img/${temp3}" 
					alt="${dto.name }" 
					title="${dto.name }" 
					style="width: 100px; height: 100px;"
				/>
			</c:if>
		</td>
	</tr>
	<tr>
		<td>상품명 : </td>
		<td>${dto.name }</td>
	</tr>
	<tr>
		<td>가격 : </td>
		<td><fmt:formatNumber type="number" maxFractionDigits="3" value="${dto.price }"/></td>
	</tr>
	<tr>
		<td style="align: center;">상품설명 : </td>
		<td id="content">${dto.description}</td>
	</tr>
	<tr>
		<td>파일명 : </td>
		<td>${fn:split(dto.product_img, '|')[0] }</td>
	</tr>
	<tr>
		<td>등록일 : </td>
		<td>${dto.regi_date }</td>
	</tr>
	<tr>
		<td>장바구니수량 : </td>
		<td><fmt:formatNumber type="number" maxFractionDigits="3" value=""/></td>
	</tr>
	<tr>
		<td colspan="2">
			<button type="button" onclick="chooseProc('write', '1', '')">상품등록</button>
			<button type="button" onclick="chooseProc('modify', '', '${dto.no}')">상품수정</button>
			<button type="button" onclick="chooseProc('delete', '', '${dto.no}')">상품삭제</button>
			<button type="button" onclick="chooseProc('list', '1', '')">목록으로</button>
		</td>
	</tr>
</table>