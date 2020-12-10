import java.io.*;

public class FiltroDeArchivo extends javax.swing.filechooser.FileFilter{
    String descrip = "";
    String listTipos[];
    FiltroDeArchivo(String tipo, String descripcion)
    {
        listTipos = new String[1];
        listTipos[0] = tipo;
        descrip = descripcion;
    }
    FiltroDeArchivo(String listTipos[], String descripcion)
    {
        this.listTipos = listTipos;
        descrip = descripcion;
    }
    public boolean accept(File fileobj){
        boolean extIgual = false;
        String extension = "";
        int ind=0;
        if(fileobj.getPath().lastIndexOf(".") > 0)
        {
            extension = fileobj.getPath().substring(fileobj.getPath().lastIndexOf(".")+1).toLowerCase();
        }
        if(extension!="")
        {
            while(ind<listTipos.length && !extIgual)
            {
                extIgual = extension.equals(listTipos[ind].toLowerCase());
                ind++;
            }
            return extIgual;
        }
        else
            return fileobj.isDirectory();
    }
    public String getDescription()
    {
        if (descrip != "")
            return descrip.concat(concatTipos());
        else
            return "";
    }
    private String concatTipos()
    {
        StringBuffer tipos = new StringBuffer(" (*."+listTipos[0]);
        for(int i=1;i<listTipos.length;i++)
            tipos.append(",*."+listTipos[i]);
        tipos.append(")");
        return tipos.toString();
    }
}