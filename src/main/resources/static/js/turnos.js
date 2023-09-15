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
