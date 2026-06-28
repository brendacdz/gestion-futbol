function login() {
	const username = $('#username').val().trim();
	const password = $('#password').val();

	if (!username || !password) {
		$('#errorMsg').text('Completá usuario y contraseña.').show();
		return;
	}

	const btn = $('#btnLogin');
	btn.text('Ingresando...').prop('disabled', true);
	$('#errorMsg').hide();

	$.ajax({
		url: 'LoginServlet',
		method: 'POST',
		data: { username: username, password: password },
		success: function(res) {
			if (res.exito) {
				sessionStorage.setItem('usuario', JSON.stringify({
					nombre: res.nombreClub,
					rol: res.rol,
					clubId: res.clubId
				}));
				if (res.rol === 'ADMIN') {
					window.location.href = 'admin.html';
				} else {
					window.location.href = 'jugadores.html';
				}
			} else {
				$('#errorMsg').text(res.mensaje || 'Usuario o contraseña incorrectos.').show();
				btn.text('Ingresar').prop('disabled', false);
			}
		},
		error: function() {
			$('#errorMsg').text('Error de conexión. ¿Está corriendo el servidor?').show();
			btn.text('Ingresar').prop('disabled', false);
		}
	});
}


$('#password').keypress(function(e) {
	if (e.which === 13) login();
});