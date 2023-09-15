document.addEventListener("DOMContentLoaded", () =>
{
    // Cargar los tooltips de bootstrapp
    loadTooltips(); 
});


// Cargar los tooltips de bootstrap
function loadTooltips()
{
    const tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltips.forEach(tooltip => new bootstrap.Tooltip(tooltip));
}

// Boton que controla el navbar
function onToggleNavbar()
{
    document.querySelector("body").classList.toggle("collapse-navbar");
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
