$(document).ready(function() {
	cargarTabla();
});

function cargarTabla() {

	$.ajax({
		url: "TablaPosicionesServlet",
		type: "GET",
		dataType: "json",
		success: function(respuesta) {
			if (respuesta.exito) {
				let tabla = "";
				let posicion = 1;
				respuesta.tabla.forEach(function (equipo) {
				    let diferencia = equipo.golesAFavor - equipo.golesEnContra;
				    tabla += `
				        <tr>
				            <td>${posicion}</td>
				            <td>${equipo.nombreClub}</td>
				            <td>${equipo.partidosJugados}</td>
				            <td>${equipo.ganados}</td>
				            <td>${equipo.empatados}</td>
				            <td>${equipo.perdidos}</td>
				            <td>${equipo.golesAFavor}</td>
				            <td>${equipo.golesEnContra}</td>
				            <td>${diferencia}</td>
				            <td><strong>${equipo.puntos}</strong></td>
				        </tr>
				    `;
				    posicion++;
				});

				$("#tablaPosiciones").html(tabla);

			} else {
				alert(respuesta.mensaje);
			}
		},

		error: function() {
			alert("Error al cargar la tabla de posiciones.");
		}
	});
}