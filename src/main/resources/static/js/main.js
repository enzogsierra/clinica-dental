document.addEventListener("DOMContentLoaded", () =>
{
    // Cargar los tooltips de bootstrapp
    loadBootstrapUtils(); 
});


// Cargar los tooltips de bootstrap
function loadBootstrapUtils()
{
    // Cargar tooltips
    const tooltips = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltips.forEach(tooltip => new bootstrap.Tooltip(tooltip));

    // Cargar popovers
    const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]');
    const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl, {html: true}));
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

// Boton para copiar texto
function onCopyTextContent()
{
    const event = this.event;
    const btn = event.target;
    const copyTarget = btn.getAttribute("copy-target");

    const copyText = document.querySelector(copyTarget).textContent.trim();
    navigator.clipboard.writeText(copyText);
}


// Funcion para hacer clickeable un <tr> de un <table>
$(document).ready(function() 
{
    $(".clickable-row").click(function() {
        window.location = $(this).data("href");
    });
});
