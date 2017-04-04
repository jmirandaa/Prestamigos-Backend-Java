<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!doctype html>
<!--
  Material Design Lite
  Copyright 2015 Google Inc. All rights reserved.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License
-->
<html lang="en">
  <head>
    <meta name="description" content="A front-end template that helps you build fast, modern mobile web apps.">
    <title>Deudas</title>

	<!-- CSS y JS -->
	<%@include file="fragJSP/estilos.jspf"%>
    <%@include file="fragJSP/javascript.jspf"%>   
        
    <style>
    #view-source {
      position: fixed;
      display: block;
      right: 0;
      bottom: 0;
      margin-right: 40px;
      margin-bottom: 40px;
      z-index: 900;
    }
    </style>
  </head>
  <body>
      
    <!-- Navegación -->
    <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
      <header class="mdl-layout__header">
        <div class="mdl-layout__header-row">
          <span class="mdl-layout-title"><c:if test="${requestScope.tipo == 1}">Me deben</c:if><c:if test="${requestScope.tipo == 2}">Debo</c:if></span>
          <!-- Add spacer, to align navigation to the right -->
          <div class="mdl-layout-spacer"></div>
          <!-- Navigation. We hide it in small screens. -->
          <nav class="mdl-navigation">
            <label class="mdl-button mdl-js-button mdl-button--icon">
                <i id="nuevaDeuda" class="material-icons">add</i>
            </label>
            <div class="mdl-textfield mdl-js-textfield mdl-textfield--expandable
                  mdl-textfield--floating-label mdl-textfield--align-right">
                <!--                
                <label class="mdl-button mdl-js-button mdl-button--icon" for="fixed-header-drawer-exp">
                    <i class="material-icons">search</i>
                </label>
                -->
        <div class="mdl-textfield__expandable-holder">
          <input class="mdl-textfield__input" type="text" name="sample"
                 id="fixed-header-drawer-exp">
        </div>                
                
            </div>
          </nav>
        </div>
      </header>
      <div class="mdl-layout__drawer">
        <span class="mdl-layout-title"><c:if test="${requestScope.tipo == 1}">Me deben</c:if><c:if test="${requestScope.tipo == 2}">Debo</c:if></span>
        <nav class="mdl-navigation">
        <c:if test="${requestScope.tipo == 2}"><a class="mdl-navigation__link" href="deudasDeben.do">Me deben</a></c:if>
   		  <c:if test="${requestScope.tipo == 1}"><a class="mdl-navigation__link" href="deudasDebo.do">Mis deudas</a></c:if>
          <a class="mdl-navigation__link" href="resumen.do">Resumen</a>
          <a class="mdl-navigation__link" href="historial.do">Historial</a>
          <a class="mdl-navigation__link" href="amigos.do">Amigos</a>
          <a class="mdl-navigation__link" href="perfil.do">Perfil</a>
          <a class="mdl-navigation__link" href="cerrarSesion.do">Cerrar sesión</a>
        </nav>
      </div>
      
      <!-- Contenido-->
      <main class="mdl-layout__content">
        <div class="page-content mdl-grid">
                  
          <c:forEach var="deuda" items="${deudas}">
           <div data-id-deuda="${deuda.id}" class="mdl-cell mdl-cell--12-col">
            <div class="demo-card-wide mdl-card mdl-shadow--2dp">
              <div class="mdl-card__title">
                <h2 class="mdl-card__title-text"><c:if test="${deuda.usuario.id == sessionScope.id}"><c:out value="${fn:trim(deuda.usuarioDestino.nombre)}" /> <c:out value="${fn:trim(deuda.usuarioDestino.apellidos)}" /></c:if><c:if test="${deuda.usuario.id != sessionScope.id}"><c:out value="${fn:trim(deuda.usuario.nombre)}" /> <c:out value="${fn:trim(deuda.usuario.apellidos)}" /></c:if>                
                </h2>
              </div>
              <div class="mdl-card__supporting-text">
                <div><fmt:formatDate pattern="dd/MM/yyyy" value="${deuda.fechaRegistro}" /></div>
                <div><c:out value="${fn:trim(deuda.concepto)}" /></div>
                <div class="card-cantidad"><c:out value="${deuda.cantidad - deuda.saldado}" />€</div>
              </div>
              <div class="mdl-card__actions mdl-card--border">
                <a class="dialog-saldar mdl-button mdl-button--accent mdl-js-button mdl-js-ripple-effect">
                  Saldar
                </a>
                <a class="dialog-aumentar mdl-button mdl-button--accent mdl-js-button mdl-js-ripple-effect">
                  Incrementar
                </a>                
              </div>
            </div> 
           </div>
          </c:forEach>                  
                  
         <!-- Diálogo-->
         <dialog id="dialog-d-aumentar" class="mdl-dialog">
            <h4 class="mdl-dialog__title">Incrementar</h4>
            <div class="mdl-dialog__content">
              <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="number" id="campo_cantidad_incrementar">
                <label class="mdl-textfield__label" for="campo_cantidad_incrementar">Cantidad</label>
              </div>
            </div>
            <div class="mdl-dialog__actions">
              <button id="aceptar-aumentar" type="button" class="mdl-button">Aceptar</button>
              <button type="button" class="mdl-button close">Cancelar</button>
            </div>
        </dialog> 
         <dialog id="dialog-d-saldar" class="mdl-dialog">
            <h4 class="mdl-dialog__title">Saldar</h4>
            <div class="mdl-dialog__content">
              <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input class="mdl-textfield__input" type="number" id="campo_cantidad_saldar">
                <label class="mdl-textfield__label" for="campo_cantidad_saldar">Cantidad</label>
              </div>
            </div>
            <div class="mdl-dialog__actions">
              <button id="aceptar-saldar" type="button" class="mdl-button">Aceptar</button>
              <button type="button" class="mdl-button close">Cancelar</button>
            </div>
        </dialog>
        
        <div id="formInvisible">
        </div>         
        
      <script>
      	//Enviar nueva operacion
      	function nuevaOp(idDeuda, cantidad, tipoOp)
      	{
    		$("#formInvisible").append('<form id="formEnviar" action="nuevaOp.do" method="POST">');
    		$("#formInvisible form").append('<br><input type="hidden" name="idUsuarioOrigen" value="${sessionScope.id}" />');
    		$("#formInvisible form").append('<br><input type="hidden" name="idDeuda" value="'+idDeuda+'" />');
    		$("#formInvisible form").append('<br><input type="hidden" name="cantidad" value="'+cantidad+'" />');
    		$("#formInvisible form").append('<br><input type="hidden" name="tipo" value="${requestScope.tipo}" />');
    		$("#formInvisible form").append('<br><input type="hidden" name="tipoOp" value="'+tipoOp+'" />');
    		$("#formInvisible form").append('<br><input type="submit" id="savebutton" value="Save" />');
    		//Ocultar formulario
    		$("#formEnviar").hide();
    		//Enviar
    		$("#formEnviar").submit();      		
      	}
      
        //Aumentar
        var dialogAumentar = document.querySelector('#dialog-d-aumentar');
        var dialogSaldar = document.querySelector('#dialog-d-saldar');
 		var idDeuda = -1;
 		var cantidad = 0;
 		var tipoOp = -1;
        
        if (! dialogAumentar.showModal) {
          dialogPolyfill.registerDialog(dialogAumentar);
        }
        if (!dialogSaldar.showModal){
          dialogPolyfill.registerDialog(dialogSaldar);
        }
        dialogAumentar.querySelector('.close').addEventListener('click', function() {
          dialogAumentar.close(); 
        });       
        dialogSaldar.querySelector('.close').addEventListener('click', function() {
          dialogSaldar.close(); 
        });         
        
        $( ".dialog-aumentar" ).click(function(event) {
            dialogAumentar.showModal();
            
            //Obtener el id de la deuda
            idDeuda = $(this).parent().parent().parent().attr("data-id-deuda");
            tipoOp = 1;

        });
             
        //Saldar
        $( ".dialog-saldar" ).click(function(event) {
            dialogSaldar.showModal();
            
            //Obtener el id de la deuda
            idDeuda = $(this).parent().parent().parent().attr("data-id-deuda");
            tipoOp = 0;

        });
        
        //Al pulsar aceptar Aumentar
        $( "#aceptar-aumentar" ).click(function(event) {
        	cantidad = $("#campo_cantidad_incrementar").val();
        	console.log("usuario: ${sessionScope.id}, idDeuda: "+idDeuda+", cantidad: "+cantidad+", tipoOp: "+tipoOp+", tipo: ${requestScope.tipo}");
        	nuevaOp(idDeuda, cantidad, tipoOp);
            dialogAumentar.close();     
        });        
        //Al pulsar aceptar Saldar       
        $( "#aceptar-saldar" ).click(function(event) {
        	cantidad = $("#campo_cantidad_saldar").val();
        	console.log("usuario: ${sessionScope.id}, idDeuda: "+idDeuda+", cantidad: "+cantidad+", tipoOp: "+tipoOp+", tipo: ${requestScope.tipo}");
        	nuevaOp(idDeuda, cantidad, tipoOp);
            dialogSaldar.close(); 
        });
                          
      </script>
      <script>
          //Acción nueva deuda
          $("#nuevaDeuda").click(function(event) {
            window.location = "nuevaDeuda.do?tipo=${requestScope.tipo}";
          });
          
          //Pulsar en titulo deuda
          $(".mdl-card__title").click(function(event) {
          	var idDeuda = $(".mdl-card__title").parent().parent().attr("data-id-deuda");
          	window.location = "detallesDeuda.do?idDeuda="+idDeuda;
          });
      </script>       
                             
                        
        </div>
      </main>
    </div>
     
   	<!-- Snackbar -->
	<%@include file="fragJSP/snackError.jspf"%>  
    
  </body>
</html>
