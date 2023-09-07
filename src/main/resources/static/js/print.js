const printBtn = document.querySelector("button#printBtn"); // Seleccionamos el button de "Imprimir"

document.addEventListener("DOMContentLoaded", () =>
{
    if(printBtn) // Si el boton de imprimir existe
    {
        printBtn.addEventListener("click", () => // Escuchar cuando se haga click en el boton de imprimir
        {
            const printSrc = printBtn.getAttribute("print-src"); // Obtener la url de la vista que queremos imprimir desde el atributo "print-src" del <button>

            const iframe = document.createElement("IFRAME"); // Crear un iframe
            iframe.src = printSrc; // Establecer el src del iframe
            iframe.style.display = "none"; // No mostrar el iframe
            iframe.onload = setPrint;
            document.body.appendChild(iframe);
        });

        // const printSrc = printBtn.getAttribute("print-src"); // Obtener la url de la vista que queremos imprimir desde el atributo "print-src" del <button>

        // const iframe = document.createElement("IFRAME"); // Crear un iframe
        // iframe.src = printSrc; // Establecer el src del iframe
        // iframe.style.display = "none"; // No mostrar el iframe
        // iframe.onload = setPrint;
        // document.body.appendChild(iframe);
    }
});

function setPrint()
{
    const closePrint = () => 
    {
        document.body.removeChild(this);
    }

    this.contentWindow.onbeforeunload = closePrint;
    this.contentWindow.onafterprint = closePrint;
    this.contentWindow.print();
}

