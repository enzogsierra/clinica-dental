const addDoctorBtn = document.querySelector("#addDoctorBtn");
const editDoctorBtn = document.querySelectorAll("#editDoctorBtn");
const deleteDoctorBtn = document.querySelectorAll("#deleteDoctorBtn");


// Esta variable verifica si el popup ya esta abierto
var popupOpened = false;


document.addEventListener("DOMContentLoaded", () => 
{
    if(addDoctorBtn) addDoctorBtn.addEventListener("click", onAddDoctor); // Cuando hace click en el boton de "nuevo doctor"

    editDoctorBtn.forEach(btn => btn.addEventListener("click", onEditDoctor));
    deleteDoctorBtn.forEach(btn => btn.addEventListener("click", onDeleteDoctor));
});


function onAddDoctor()
{
    if(popupOpened)
    {
        return alert("Ya hay una ventana para crear/editar un doctor, ciérrela antes de abrir otra")
    }

    // Mostrar una ventana emergente que renderiza un formulario para crear un nuevo doctor
    const popup = window.open("/doctores/nuevo", "_blank", `width=600, height=800, menubar=0, status=0, titlebar=no`);

    // Escuchar por el evento "unload" - se llama cuando la ventana emergente se abre o cierra
    popup.addEventListener("unload", onPopupClose);
} 

function onEditDoctor(e)
{
    if(popupOpened)
    {
        return alert("Ya hay una ventana para crear/editar un doctor, ciérrela antes de abrir otra")
    }

    const doctorId = e.target.getAttribute("doctor-id");
    
    // Mostrar una ventana emergente que renderiza un formulario para editar un doctor
    const popup = window.open(`/doctores/editar/${doctorId}`, "_blank", `width=600, height=800, menubar=0, status=0, titlebar=no`);

    // Escuchar por el evento "unload" - se llama cuando la ventana emergente se abre o cierra
    popup.addEventListener("unload", onPopupClose);
}

function onDeleteDoctor(e)
{
    // Mostrar un prompt antes de eliminar
    if(confirm("¿Seguro que quieres eliminar este doctor? Esta acción es irreversible") == true)
    {
        // Eliminar doctor
        const doctorId = e.target.getAttribute("doctor-id");
        window.location.href = `/doctores/eliminar/${doctorId}`; // Redirigir ventana actual al endpoint donde se eliminan doctores, el controlador redirigira nuevamente a /doctores
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
