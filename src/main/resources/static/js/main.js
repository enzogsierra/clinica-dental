//const printBtn = document.querySelector("button.btn-print"); // Selecciona el boton de "imprimir", si existe


document.addEventListener("DOMContentLoaded", () =>
{
    // Cargar los tooltips de bootstrapp
    loadTooltips(); 

    // Si existe un boton de "imprimir", añadir un eventListener para cuando se haga click
    //if(printBtn) printBtn.addEventListener("click", onPrintButtonClick);
});


// Cargar los tooltips de bootstrap
function loadTooltips()
{
    const tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltips.forEach(tooltip => new bootstrap.Tooltip(tooltip));
}


// Boton "imprimir"
function onPrintButtonClick() {
    window.print(); // Muestra un dialogo para imprimir la pagina actual
}


// Funcion para hacer clickeable un <tr> de un <table>
$(document).ready(function() 
{
    $(".clickable-row").click(function() {
        window.location = $(this).data("href");
    });
});