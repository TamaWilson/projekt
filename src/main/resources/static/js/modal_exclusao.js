$('#modalExclusao').on('show.bs.modal', function(e) {

    $(this).find('#btn-deletar').attr('href', $(e.relatedTarget).data('url'));
});