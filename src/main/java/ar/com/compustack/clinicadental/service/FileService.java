package ar.com.compustack.clinicadental.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileService 
{
    private final String resourcesFolder = "assets//"; // Carpeta donde se guardarán las imagenes (comienza desde la carpeta raiz del proyecto)


    // Guardar archivo
    public String save(MultipartFile file, String filename) throws IOException
    {
        if(!file.isEmpty()) // Archivo existente
        {
            // Generar un nombre de archivo para el archivo
            final String ext = getFileExtension(file.getOriginalFilename()).get(); // Obtenemos la extensión del archivo
            //final String filename = UUID.randomUUID().toString() + ext; // Generamos un nombre random al archivo y anexamos su extension

            // Guardar archivo
            byte[] bytes = file.getBytes();
            Path path = Paths.get(resourcesFolder + filename + ext);
            Files.write(path, bytes);

            return filename + ext; // Retornamos el nombre del archivo + su extension
        }
        else throw new IOException("No se encontró un archivo");
    }

    // Eliminar archivo
    public void delete(String path) throws IOException
    {
        File file = new File(resourcesFolder + path);
        if(file.exists()) file.delete();
        else throw new IOException("Error al eliminar archivo: archivo inexistente");
    }


    // Retorna la extension del archivo (".exe", ".png", etc)
    private Optional<String> getFileExtension(String filename) 
    {
        return Optional.ofNullable(filename)
            .filter(str -> str.contains("."))
            .map(str -> str.substring(filename.lastIndexOf(".")));
    }
}
