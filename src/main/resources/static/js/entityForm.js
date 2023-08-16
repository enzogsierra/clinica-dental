const modal = document.querySelector("#entityModal");
const form = document.querySelector("form#entityForm");
const deleteBtns = document.querySelectorAll("button#deleteEntityBtn");



// Cuando se hace submit del form
async function onFormSubmit(event, url, text1)
{
    event.preventDefault(); // Evitamos que el form haga submit por default
    clearValidations(); // Limpiamos los mensajes de validaciones

    const data = new FormData(form); // Guardamos los datos del formulario
    const api = await fetch(url, {body: data, method: "POST"}); // Enviamos los datos de la entidad al controlador

    if(api.status == 200) // El controlador respondió bien, la entidad se creó correctamente
    {
        const id = (form.elements["id"] != undefined) ? form.elements["id"].value : 0; // Obtenemos el id de la entidad

        // Mostramos un mensaje de confirmacion
        Swal.fire(
        {
            icon: "success",
            title: id >= 1 ? (`${text1} editado correctamente`) : (`${text1} añadido correctamente`)
        }).then(() =>
        {
            // Cuando el usuario sale del mensaje, recargamos la pagina
            window.location.reload();
        })
    }
    else if(api.status == 400) // Ocurrió un error de validación
    {
        const validations = await api.json(); // Convertimos los mensajes de validaciones a json
        console.log(validations);

        for(const key in validations) // Iteramos sobre cada mensaje de validacion
        {
            // "key" hace referencia al atributo que está siendo validado (por ejemplo, "nombre")
            // Podemos acceder a los mensajes a traves de su indice (por ejemplo, validations[nombre] mostrará el mensaje de validacion)
            const div = form.querySelector(`div#error-${key}`); // Seleccionamos el div que mostrará el mensaje de validacion
            div.classList.add('d-block'); // Mostramos el div (por defecto está oculto)
            div.textContent = validations[key]; // El texto del div será ahora el mensaje de validacion
        }
    }
}

// Cuando se presiona en el <button> para crear una entidad
function onNewEntity(text1)
{
    clearForm();
    // setModalTitle(`Crear ${text1.toLowerCase()}`);
    setModalTitle(text1)
}

// Cuando se presiona en un <button> para editar una entidad
async function onEditEntity(event, url, text1)
{
    clearForm();
    // setModalTitle(`Editar ${text1.toLowerCase()}`);
    setModalTitle(text1);

    const btn = event.target; // Obtenemos el <button> que se presionó para abrir el modal
    const id = btn.getAttribute("entity-id") ?? 0; // Obtenemos el id de la entidad a través del atributo "entity-id", si no tiene, por defecto será 0

    const api = await fetch(`${url}/${id}`); // Hacemos una petición a la url que nos traerá toda la información de la entidad (o una entidad vacía si no se encontró)
    const entity = await api.json(); // Convertimos la información de la entidad a formato json

    // Iteramos sobre cada atributo de la entidad
    for(const key in entity)
    {
        // "key" es el nombre del atributo que está siendo iterado actualmente ("id", "nombre", etc...)
        if(form.elements[key]) // Verificamos que el form tenga un elemento (<input>, etc) con el mismo name que "key" (por ejemplo, name="id")
        {
            form.elements[key].value = entity[key]; // Cambiamos el valor del elemento
        }
    }
}

// Cuando se presiona en un botón para eliminar una entidad
function onDeleteEntity(event, url, name)
{
    const btn = event.target;
    const text = btn.getAttribute("entity-text"); // Obtener nombre de la entidad

    // Mostrar aviso
    Swal.fire(
    {
        title: text,
        text: `Estás a punto de eliminar esta entrada, ¡esta acción es irreversible!`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Eliminar',
        cancelButtonText: "Cancelar"
    }).then(async (result) => 
    {
        if(result.isConfirmed) // Eliminar
        {
            const id = btn.getAttribute("entity-id"); // Obtener id de la entidad

            // Crear un form que enviará solamente el id de la identidad
            const data = new FormData();
            data.append("id", id);

            // Hacer un fetch y refrescar la pagina, sin esperar una respuesta
            // En los metodos DELETE generalmente no se esperan una respuesta por parte del servidor
            await fetch(url, {body: data, method: "DELETE"});

            Swal.fire(
            {
                icon: "success",
                title: `${name} eliminado correctamente`
            }).then(() =>
            {
                // Cuando el usuario sale del swal, recargamos la pagina
                window.location.reload();
            });
        }
    });
}


// Limpia todo el formulario
function clearForm()
{
    form.reset(); // Limpia el formulario, excepto los input hidden
    form.querySelectorAll("input[type=hidden]").forEach(input => input.value = ''); // Limpia todos los inputs hidden
    clearValidations();
}

// Quita todos los mensajes de validacion del formulario
function clearValidations()
{
    form.querySelectorAll("div.invalid-feedback").forEach(div =>
    {
        div.classList.remove("d-block");
        div.textContent = "";
    });
}

// Cambia el título del modal
function setModalTitle(title)
{
    modal.querySelector(".modal-title").textContent = title;
}


