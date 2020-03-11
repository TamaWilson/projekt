$(document).ready(function(){
    body = $("body");
    body.on("click",".favoritar", function () {
        var botao = $(this);
        $.ajax({
            type: "POST",
            url: botao.attr('data-url'),
            success: function (data) {
                botao.children().eq(0).removeClass();
                botao.children().eq(0).addClass(data);
            }
        });
    });

    body.on("click",".filtros", function (e) {
        e.preventDefault();
        var botao = $(this);
        $.ajax({
            type: "GET",
            url: botao.attr('data-url'),
            success: function (data) {
                $(".tabela-conteudo").replaceWith(data);
            }
        });
    });

});
