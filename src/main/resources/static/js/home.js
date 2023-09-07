const completeDateBtns = document.querySelectorAll("button#completeDateBtn");

document.addEventListener("DOMContentLoaded", () =>
{
    if(completeDateBtns) completeDateBtns.forEach(btn => btn.addEventListener("click", onCompleteDate));
});

function onCompleteDate(e)
{
    Swal.fire(
    {
        title: '¿Archivar turno?',
        text: 'Este turno se marcará como completado/finalizado.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: "Archivar",
        cancelButtonText: "Cancelar",
    }).then(async (result) =>
    {
        if(result.isConfirmed) // Archivar turno
        {
            const id = e.target.getAttribute("turno-id"); // Obtener id del turno

            const data = new FormData(); // Crear un form 
            data.append("id", id); // Almacenar la id del turno en el form
            const api = await fetch("finishDate", {body: data, method: "POST"}); // Enviar el form al controlador

            if(api.status == 200) // Turno archivado correctamente
            {
                Swal.fire(
                {
                    icon: "success",
                    title: 'Turno archivado correctamente'
                }).then(() =>
                {
                    // El usuario salio del mensaje, recargamos la pagina
                    window.location.reload();
                });
            }
            else // Ocurrió un error
            {
                Swal.fire(
                {
                    icon: "error",
                    title: 'Ocurrió un error al archivar el turno. Intenta otra vez'
                }).then(() =>
                {
                    // El usuario salio del mensaje, recargamos la pagina
                    window.location.reload();
                });
            }
        }
    });
}
