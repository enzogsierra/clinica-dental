const addPatientBtn = document.querySelector("#addPatientBtn");
const editPatientBtn = document.querySelectorAll("#editPatientBtn");
const deletePatientBtn = document.querySelectorAll("#deletePatientBtn");


// Esta variable verifica si el popup ya esta abierto
var popupOpened = false;


document.addEventListener("DOMContentLoaded", () => 
{
    if(addPatientBtn) addPatientBtn.addEventListener("click", onAddDoctor); // Cuando hace click en el boton de "nuevo paciente"

    editPatientBtn.forEach(btn => btn.addEventListener("click", onEditDoctor));
    deletePatientBtn.forEach(btn => btn.addEventListener("click", onDeleteDoctor));
});


function onAddDoctor()
{
    if(popupOpened)
    {
        return alert("Ya hay una ventana para crear/editar un paciente, ciérrela antes de abrir otra")
    }

    // Mostrar una ventana emergente que renderiza un formulario para crear un nuevo paciente
    const popup = window.open("/pacientes/nuevo", "_blank", `width=600, height=800, menubar=0, status=0, titlebar=no`);

    // Escuchar por el evento "unload" - se llama cuando la ventana emergente se abre o cierra
    popup.addEventListener("unload", onPopupClose);
} 

function onEditDoctor(e)
{
    if(popupOpened)
    {
        return alert("Ya hay una ventana para crear/editar un paciente, ciérrela antes de abrir otra")
    }

    const patientId = e.target.getAttribute("patient-id");
    
    // Mostrar una ventana emergente que renderiza un formulario para editar un paciente
    const popup = window.open(`/pacientes/editar/${patientId}`, "_blank", `width=600, height=800, menubar=0, status=0, titlebar=no`);

    // Escuchar por el evento "unload" - se llama cuando la ventana emergente se abre o cierra
    popup.addEventListener("unload", onPopupClose);
}

function onDeleteDoctor(e)
{
    // Mostrar un prompt antes de eliminar
    if(confirm("¿Seguro que quieres eliminar este paciente? Esta acción es irreversible") == true)
    {
        // Eliminar paciente
        const patientId = e.target.getAttribute("patient-id");
        window.location.href = `/pacientes/eliminar/${patientId}`; // Redirigir ventana actual al endpoint donde se eliminan pacientes, el controlador redirigira nuevamente a /pacientes
    }
}


function onPopupClose()
{
    if(popupOpened) // La ventana se cerró
    {
        window.location.reload();
    }

    popupOpened = !popupOpened; // Cambiar entre true/false. Está en "false" cuando la ventana se abre, y se pone en "true" cuando se cierra
}
