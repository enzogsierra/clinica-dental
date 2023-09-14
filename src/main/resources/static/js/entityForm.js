// Cuando se presiona en el <button> para crear una entidad
function onNewEntity(title)
{
    const button = this.event.target; // Seleccionamos el button que disparó el evento
    const modalTarget = button.getAttribute("data-bs-target"); // A través del atributo "data-bs-target" del button, obtenemos a qué modal apunta
    const modal = document.querySelector(modalTarget); // Seleccionamos el modal que está asociado al button
    const form = modal.querySelector("form"); // Seleccionamos el form que está dentro del modal

    clearForm(form); // Limpiamos el form
    modal.querySelector(".modal-title").textContent = title; // Cambiamos el título del modal
}

// Cuando se presiona en el <button> para editar una entidad
async function onEditEntity(url, title)
{
    const button = this.event.target; // Seleccionamos el button que disparó el evento
    const modalTarget = button.getAttribute("data-bs-target"); // A través del atributo "data-bs-target" del button, obtenemos a qué modal apunta
    const modal = document.querySelector(modalTarget); // Seleccionamos el modal que está asociado al button
    const form = modal.querySelector("form"); // Seleccionamos el form que está dentro del modal

    clearForm(form); // Limpiamos el form
    modal.querySelector(".modal-title").textContent = title; // Cambiamos el título del modal


    // Enviar datos del formulario
    const id = button.getAttribute("entity-id"); // Obtenemos el id de la entidad a través del atributo "entity-id" que está en el button
    const api = await fetch(`${url}/${id}`); // Hacemos una petición a la url que nos traerá toda la información de la entidad (o una entidad vacía si no se encontró)
    const entity = await api.json(); // Convertimos la información de la entidad a formato json

    // Iteramos sobre cada propiedad de la entidad
    for(const propertyName in entity)
    {
        // "propertyName" es el nombre de la propiedad que está siendo iterado actualmente ("id", "nombre", etc...)
        if(form.elements[propertyName]) // Verificamos que el form tenga un input (tambien puede ser un <select>, <textarea>, etc) con el mismo name que "propertyName" (por ejemplo, name="id")
        {
            // Creamos una variable que almacenará el valor que tendrá el input
            // Si la propiedad que está siendo iterada es de tipo "object", entonces extraeremos el valor "id" dentro de la propiedad (por ejemplo, {turno.paciente.id})
            // Si no es tipo object, entonces simplemente obtenemos el valor de la propiedad
            // Hay propiedades de tipo "Boolean" que javascript también los toma como de tipo object
            const value = (typeof entity[propertyName] == "object" && entity[propertyName] != null) ? entity[propertyName].id : entity[propertyName];

            if(form.elements[propertyName].type == "checkbox") // Si el input es tipo "checkbox"
            {
                // Los inputs de tipo "checkbox" no trabajan con el atributo "value", sino con el "checked"
                form.elements[propertyName].checked = value; // Cambiamos el atributo "checked" del input (<input checked="checked">)
            }
            else // Si no es tipo checkbox
            {
                form.elements[propertyName].value = value; // Cambiamos el atributo "value" del input (<input value="...">)
            }
        }
        else // La propiedad de la entidad no tiene asignado un elemento (<input>, etc) dentro del form
        {
            // A veces hay ciertas propiedades de la entidad que no es necesario mostrar dentro del form, como por ejemplo "id"
            // Entonces crearemos un input que almacene el valor de esa propiedad y lo añadiremos al form
            // De esta forma los datos de la entidad no quedan incompletos, debido a que todos los datos de la entidad deben estar dentro del form
            const input = document.createElement("INPUT"); // Creamos el input que almacenará el valor de la propiedad
            input.type = "hidden"; // Hacemos que el input sea del tipo "hidden", así no se muestra en el form
            input.name = propertyName; // Le damos el name de la propiedad (por ej: name="id")

            if(typeof entity[propertyName] == "object") // Si la propiedad es un objeto (por ejemplo, {turno.paciente})
            {
                const value = (entity[propertyName] == null) ? null : entity[propertyName].id; // Si el objeto está vacío, el valor será null, sino, el valor será el valor de la propiedad "id" del objeto (por ejemplo, {turno.paciente.id})
                input.value = value; // Cambiamos el value del input al valor obtenido arriba
            }
            else input.value = entity[propertyName]; // Si la propiedad no es un objeto, entonces el input tendrá el valor de la propiedad

            form.append(input); // Añadimos el input al form
        }

        // Si la propieda que está siendo iterada es de tipo "object" y no está vacía
        if(typeof entity[propertyName] == "object" && entity[propertyName] != null)
        {
            // Algunos inputs son solo para mostrar información adicional y no tienen el atributo "name", por lo que no enviarán esa información al controlador
            // Entonces buscaremos los inputs que muestran esa información
            for(const [objPropertyName, objPropertyValue] of Object.entries(entity[propertyName])) // Iteramos las propiedades de este object, "...Name" es el nombre de la propiedad y "...Value" es el valor de la propiedad
            {
                const name = `#${propertyName}_${objPropertyName}`; // Buscamos el input que tenga id="propiedad_nombrePropiedad" (por ejemplo, id="paciente_nombre", que corresponde a {turno.paciente.nombre})
                const input = form.querySelector(name); // Seleccionamos el input que tenga esa id

                if(input) // Existe un input que muestra esa informacion
                {
                    input.value = objPropertyValue; // Cambiamos el value del input al valor de la propiedad
                }
            }
        }
    }
}

// Cuando se hace submit del form
async function onFormSubmit(url, entityName)
{
    const event = this.event; // Obtenemos el evento
    const form = event.target; // Obtenemos el <form> que disparó el evento

    event.preventDefault(); // Evitamos que el form haga submit por default
    clearValidations(form); // Limpiamos los mensajes de validaciones


    // Enviar datos del form al controlador
    const data = new FormData(form); // Guardamos los datos del formulario
    const api = await fetch(url, {body: data, method: "POST"}); // Enviamos los datos de la entidad al controlador

    if(api.status == 200) // El controlador respondió bien, la entidad se creó correctamente
    {
        const id = (form.elements["id"] != undefined) ? form.elements["id"].value : 0; // Obtenemos el id de la entidad

        // Mostramos un mensaje de confirmacion
        Swal.fire(
        {
            icon: "success",
            title: id >= 1 ? (`${entityName} editado correctamente`) : (`${entityName} añadido correctamente`)
        }).then(() =>
        {
            // Cuando el usuario sale del mensaje, recargamos la pagina
            window.location.reload();
        })
    }
    else if(api.status == 400) // Ocurrió un error de validación
    {
        const validations = await api.json(); // Convertimos los mensajes de validaciones a json

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

// Cuando se presiona en un botón para eliminar una entidad
function onDeleteEntity(url, entityName)
{
    const event = this.event; // Obtenemos el evento
    const button = event.target; // Obtenemos el button que disparó el evento
    const text = button.getAttribute("entity-text"); // Obtener nombre de la entidad

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
            const id = button.getAttribute("entity-id"); // Obtener id de la entidad

            // Crear un form que enviará solamente el id de la identidad
            const data = new FormData();
            data.append("id", id);

            // Hacer un fetch y refrescar la pagina, sin esperar una respuesta
            // En los metodos DELETE generalmente no se esperan una respuesta por parte del servidor
            await fetch(url, {body: data, method: "DELETE"});

            Swal.fire(
            {
                icon: "success",
                title: `${entityName} eliminado correctamente`
            }).then(() =>
            {
                // Cuando el usuario sale del swal, recargamos la pagina
                window.location.reload();
            });
        }
    });
}


// Limpia todo el formulario
function clearForm(form)
{
    form.reset(); // Limpia el formulario, excepto los input hidden
    form.querySelectorAll("input[type=hidden]").forEach(input => input.value = ''); // Limpia todos los inputs hidden
    clearValidations(form);
}

// Quita todos los mensajes de validacion del formulario
function clearValidations(form)
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


