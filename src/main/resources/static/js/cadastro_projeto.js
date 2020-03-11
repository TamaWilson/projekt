$('.datepicker').datepicker({
    format: 'dd/mm/yyyy',
    language: 'pt-BR',
    autoclose: true,
    startDate: previsaoEntrega ? new Date(previsaoEntrega) : new Date(),
    todayHighlight : true
});