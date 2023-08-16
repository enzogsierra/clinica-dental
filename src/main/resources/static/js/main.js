// Cargar los tooltips de bootstrapp
document.addEventListener("DOMContentLoaded", () =>
{
    loadTooltips();
});

function loadTooltips()
{
    const tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltips.forEach(tooltip => new bootstrap.Tooltip(tooltip));
}


// Funcion para hacer clickeable un <tr> de un <table>
$(document).ready(function() 
{
    $(".clickable-row").click(function() {
        window.location = $(this).data("href");
    });
});