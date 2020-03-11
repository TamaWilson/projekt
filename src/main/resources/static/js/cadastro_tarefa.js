$('#descricao').on("input", function () {

    contador = 140 - $(this).val().length;

    $('#contador-letras').removeClass();

    $('#contador-letras').html(contador);

    if (contador < 40) {
        $('#contador-letras').addClass('text-danger');

    } else if (contador < 70) {
        $('#contador-letras').addClass('text-warning');

    }
});