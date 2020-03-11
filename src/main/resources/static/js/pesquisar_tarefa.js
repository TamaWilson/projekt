$("#pesquisar").on("input", function () {
    if($(this).val().length > 2) {
        $.ajax({
            type: "POST",
            url: "/tarefa/pesquisar",
            data: {'tituloTarefa' : $(this).val() },
            success: function (data) {
                $('#resultados').html(data);
            }
        });
    } else {
        $('#resultados').html("<span>Nenhum resultado para exibir.</span>");
    }
});
