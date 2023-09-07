const financingForm = document.querySelector("form#financingForm");

document.addEventListener("DOMContentLoaded", () =>
{
    financingForm.addEventListener("submit", onFinancingFormSubmit);
});


function onFinancingFormSubmit(e)
{
    const btn = e.submitter;
    
    if(btn.id == "generateFeeBtn")
    {
        e.preventDefault();
        generateFee();
    }
}


function generateFee()
{
    // Mostrar la lista de cuotas
    const feeList = document.querySelector("#feeList");
    feeList.classList.remove("d-none");

    //
    const financingValue = document.querySelector("input#financing_Value").value;
    const monthlyInt = document.querySelector("input#financing_MonthlyInt").value;
    const feesAmount = document.querySelector("select#financing_feesAmount").value;


    // Ocultar todas las cuotas por default, y quitar los "name" de todos sus inputs
    const feeList_items = feeList.querySelectorAll("#feeList_item"); // Seleccionamos todos los divs que contienen una cuota
    feeList_items.forEach(item =>
    {
        item.classList.add("d-none"); // Añadimos esta clase para ocultar el div

        const inputs = item.querySelectorAll("input"); // Seleccionamos todos los inputs de la cuota
        inputs.forEach(input =>
        {
            input.removeAttribute("name"); // Le quitamos el atributo "name" para evitar que los datos de esta cuota sean enviados al controlador
        });
    });

    let totalCost = 0;

    // Mostrar las cuotas segun la cantidad de cuotas seleccionadas
    for(let i = 0; i < feesAmount; i++)
    {
        const fee = feeList.querySelector(`#feeList_item[fee-id="${i}"]`); // Buscar el div contenedor de la cuota
        fee.classList.remove("d-none"); // Quitamos esta clase del div, para que así se muestre en el documento

        // Seleccionamos los inputs que contienen informacion sobre la cuota
        const nroCuota = fee.querySelector("input#nroCuota"); 
        const monto = fee.querySelector("input#monto"); 
        const fechaCobro = fee.querySelector("input#fechaCobro");

        // Añadimos el atributo "name" a los inputs de esta cuota, así se enviarán esos datos al controlador
        nroCuota.setAttribute("name", `cuotas${i}.nroCuota`);
        monto.setAttribute("name", `cuotas${i}.monto`);
        fechaCobro.setAttribute("name", `cuotas${i}.fechaCobro`);

        // Establecer el monto de la cuota y la fecha de cobro
        const feeCost = (financingValue / feesAmount) * (1 + (monthlyInt / 12));
        monto.value = feeCost.toFixed(0);
        totalCost += feeCost;
    }

    console.log(totalCost);
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
