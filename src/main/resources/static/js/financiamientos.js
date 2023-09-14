// Cuando se hace submit del form del financiamiento
function onFinancingFormSubmit(event)
{
    const form = event.target; // Obtenemos el form
    const src = event.submitter; // Obtenemos el elemento que disparó el evento

    if(src.tagName == "BUTTON" && src.id == "generateFeeBtn") // Si el elemento que disparó el submit fue el button de generar cuotas
    {
        form.action = "/financiamientos/generarCuotas"; // Cambiamos el endpoint del action
    }
    else
    {
        form.action = "/financiamientos/form";
    }
}



// Autocomplete - cuando el usuario busca al paciente que se le hará el financiamiento
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
