$(document).ready(function () {
    cargarFixture();
});

function generarFixture() {

    $.ajax({
        url: "FixtureServlet",
        type: "POST",
        dataType: "json",

        success: function (respuesta) {

            alert(respuesta.mensaje);

            if (respuesta.exito) {
                cargarFixture();
            }

        },

        error: function () {
            alert("Error al generar el fixture.");
        }

    });

}

function cargarFixture() {

    $.ajax({

        url: "FixtureServlet",
        type: "GET",
        dataType: "json",

        success: function (respuesta) {

            if (respuesta.exito) {

                let tabla = "";

                respuesta.partidos.forEach(function (partido) {

                    let golesLocal = (partido.golesLocal != null) ? partido.golesLocal : "";
                    let golesVisitante = (partido.golesVisitante != null) ? partido.golesVisitante : "";

                    tabla += `
                    <tr>
                        <td>${partido.fechaJornada}</td>
                        <td>${partido.nombreClubLocal}</td>
                        <td>${partido.nombreClubVisitante}</td>

                        <td>
                            <input
                                type="number"
                                id="local${partido.id}"
                                class="form-control"
                                value="${golesLocal}">
                        </td>

                        <td>
                            <input
                                type="number"
                                id="visitante${partido.id}"
                                class="form-control"
                                value="${golesVisitante}">
                        </td>

                        <td>
                            <button
                                class="btn btn-primary btn-sm"
                                onclick="guardarResultado(${partido.id})">
                                Guardar
                            </button>
                        </td>

                    </tr>
                    `;

                });

                $("#tablaFixture").html(tabla);

            } else {

                alert(respuesta.mensaje);

            }

        },

        error: function () {

            alert("Error al cargar el fixture.");

        }

    });

}

function guardarResultado(id) {

    let golesLocal = $("#local" + id).val();
    let golesVisitante = $("#visitante" + id).val();

    $.ajax({

        url: "ResultadoServlet",

        type: "POST",

        data: {
            partidoId: id,
            golesLocal: golesLocal,
            golesVisitante: golesVisitante
        },

        dataType: "json",

        success: function (respuesta) {

            alert(respuesta.mensaje);

            if (respuesta.exito) {
                cargarFixture();
            }

        },

        error: function () {

            alert("Error al guardar el resultado.");

        }

    });

}