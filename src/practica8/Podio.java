package practica8;

import java.util.ArrayList;
import java.util.List;

public class Podio {
    private List<String> clasificacion = new ArrayList<>();

    public synchronized void registrarLlegada(String nombre) {
        clasificacion.add(nombre);
    }

    public List<String> getClasificacion() {
        return clasificacion;
    }

    public void limpiar() {
        clasificacion.clear();
    }
}