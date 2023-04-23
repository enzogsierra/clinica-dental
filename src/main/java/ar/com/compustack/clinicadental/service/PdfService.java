package ar.com.compustack.clinicadental.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;


@Service
public class PdfService 
{
    private static final String PATH_DEST = "src/main/resources/static/"; // Seleccionamos esta ruta así los usuarios pueden ver los archivos pdf a traves de la url ({url}/archivo.pdf)


    // Generar un archivo pdf a partir de una plantilla html
    public static void generate(String html, String filename) throws DocumentException
    {
        File pdf = new File(PATH_DEST + filename); // Ruta donde se creará el archivo pdf

        ITextRenderer renderer = new ITextRenderer(); // Objeto que generará el pdf
        renderer.setDocumentFromString(html); // Pasamos el contenido HTML que se convertirá en PDF
        renderer.layout(); // Realiza el proceso de diseño del PDF a partir del HTML

        try
        {
            FileOutputStream outputStream = new FileOutputStream(pdf);
            renderer.createPDF(outputStream, true);
        }
        catch(IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
