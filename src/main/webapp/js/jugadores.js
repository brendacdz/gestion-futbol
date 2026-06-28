$(document).ready(function() {
	cargarJugadores();
});

let idEnEdicion = null;

function cargarJugadores() {
	$.ajax({
		url: "JugadorServlet",
		type: "GET",
		dataType: "json",
		success: function(respuesta) {
			if (respuesta.exito) {
				let tabla = "";

				respuesta.jugadores.forEach(function(jugador) {
					tabla += `
                        <tr>
                            <td>${jugador.nombre}</td>
                            <td>${jugador.edad}</td>
                            <td>${jugador.altura}</td>
                            <td>${jugador.peso}</td>
                            <td>${jugador.habilidad}</td>
                            <td>${jugador.puesto}</td>
                            <td>
                                <button class="btn btn-primary btn-sm"
                                    onclick='editarJugador(${JSON.stringify(jugador)})'>
                                    Editar
                                </button>
                                <button class="btn btn-danger btn-sm"
                                    onclick="eliminarJugador(${jugador.id})">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    `;
				});
				$("#tablaJugadores").html(tabla);
			} else {
				alert(respuesta.mensaje);
			}
		},
		error: function() {
			alert("Error al cargar los jugadores.");
		}
	});
}

function guardarJugador() {

	let nombre = $("#nombre").val();
	let edad = $("#edad").val();
	let altura = $("#altura").val();
	let peso = $("#peso").val();
	let habilidad = $("#habilidad").val();
	let puesto = $("#puesto").val();

	if (
		nombre === "" ||
		edad === "" ||
		altura === "" ||
		peso === "" ||
		habilidad === "" ||
		puesto === ""
	) {
		alert("Complete todos los campos.");
		return;
	}

	let datosEnvio = {
		accion: idEnEdicion ? "editar" : "guardar",
		nombre: nombre,
		edad: edad,
		altura: altura,
		peso: peso,
		habilidad: habilidad,
		puesto: puesto
	};

	if (idEnEdicion) {
		datosEnvio.id = idEnEdicion;
	}

	$.ajax({
		url: "JugadorServlet",
		type: "POST",
		data: datosEnvio,
		dataType: "json",
		success: function(respuesta) {
			if (respuesta.exito) {
				let mensaje = idEnEdicion ? "Jugador actualizado correctamente." : "Jugador guardado correctamente.";
				limpiarFormulario();
				cargarJugadores();
				alert(mensaje);
			} else {
				alert(respuesta.mensaje);
			}
		},
		error: function() {

			alert("Error al guardar el jugador.");
		}
	});
}

function editarJugador(jugador) {
	idEnEdicion = jugador.id;

	$("#nombre").val(jugador.nombre);
	$("#edad").val(jugador.edad);
	$("#altura").val(jugador.altura);
	$("#peso").val(jugador.peso);
	$("#habilidad").val(jugador.habilidad);
	$("#puesto").val(jugador.puesto);

	$("#btnGuardarJugador").text("Actualizar jugador");
}

function eliminarJugador(id) {

	if (!confirm("¿Desea eliminar este jugador?")) {
		return;
	}
	$.ajax({
		url: "JugadorServlet",
		type: "POST",
		data: {
			accion: "eliminar",
			id: id
		},
		dataType: "json",
		success: function(respuesta) {
			if (respuesta.exito) {
				cargarJugadores();
			} else {
				alert(respuesta.mensaje);
			}
		},
		error: function() {
			alert("Error al eliminar el jugador.");
		}
	});
}
function limpiarFormulario() {

	idEnEdicion = null;

	$("#nombre").val("");
	$("#edad").val("");
	$("#altura").val("");
	$("#peso").val("");
	$("#habilidad").val("");
	$("#puesto").val("");

	$("#btnGuardarJugador").text("Agregar jugador");

}