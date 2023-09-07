// Cuando el usuario hace click en un turno vacio de la tabla de turnos
function onNewDate(title)
{
    const btn = this.event.target; // Obtener el elemento que llamó el evento
    const fecha = btn.getAttribute("turno-fecha"); // Obtener la fecha seleccionada
    const hora = btn.getAttribute("turno-hora"); // ^
    
    onNewEntity(title); // Mostrar el modal para crear un turno

    // Editar form para que tenga la fecha y hora seleccionada
    const modalTarget = btn.getAttribute("data-bs-target");
    const modal = document.querySelector(modalTarget);
    const form = modal.querySelector("form");

    form.elements["fecha"].value = fecha; // Cambiar el valor del input de la hora
    form.elements["hora"].value = hora; // ...
}


// Autocomplete - cuando el usuario busca un paciente para agendar un turno
$(document).ready(function()
{
    $("input#pacienteNombre").autocomplete(
    {
        source: function(req, res) // Función para obtener la lista de pacientes
        {
            $.ajax(
            {
                url: `/pacientes/findAll/${req.term}`, // Url donde hará la petición
                data: {term: req.term},  // 'data' es el término que buscará el usuario (nombre, apellido, etc)
                dataType: "json",
                success: function(data) 
                {
                    res($.map(data, function(item)
                    {
                        return {
                            label: `${item.nombre} ${item.apellido}`, // Lo que se mostrará en la lista
                            value: item // La variable que enviará a "select" cuando se seleccione un item
                        }
                    }));
                }
            });
        },
        select: function(event, ui) // Cuando el usuario selecciona un item
        {
            const paciente = ui.item.value;

            event.preventDefault(); // Evita que autocomplete rellene el input
            $("input#pacienteNombre").val(`${paciente.nombre} ${paciente.apellido}`); // Cambiamos el texto del input
            $("input#paciente").val(paciente.id); // Asignamos la id del paciente seleccionado
        }
    });
});

/*
// Cuando abre un card de un turno
async function onShowDateInfo(event)
{
    const btn = event.target; // Obtenemos el <button> que se presionó para abrir el modal
    const id = btn.getAttribute("turno-id") ?? 0; // Obtenemos el id de la entidad a través del atributo "turno-id", si no tiene, por defecto será 0

    const api = await fetch(`/turnos/get/${id}`); // Hacemos una petición a la url que nos traerá toda la información de la entidad (o una entidad vacía si no se encontró)
    const entity = await api.json(); // Convertimos la información de la entidad a formato json

    // Rellenar modal
    const div = document.querySelector("#infoModal"); // Obtener modal de informacion
    for(const key in entity) // Iteramos sobre cada atributo de la entidad
    {
        // Cambiar valor de los inputs
        const input = div.querySelector(`input#${key}`);
        if(input) input.value = entity[key];

        // Cambiar href de links
        const a = div.querySelector(`a#${key}`);
        if(a) a.href = `https://wa.me/54${entity[key]}`;
    }

    // Cambiar el entity-id de los botones (editar, eliminar)
    const actionBtns = div.querySelectorAll("button[entity-id]");
    actionBtns.forEach(btn => btn.setAttribute("entity-id", id));
}
*/
