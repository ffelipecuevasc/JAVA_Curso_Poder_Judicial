package org.fcuevas.utilitarias;

import org.fcuevas.estructurales.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BibliotecaOps {
    private List<Material> materiales;
    private List<Usuario> usuarios;
    private List<Prestamo> prestamos;
    private Scanner SC;

    public BibliotecaOps() {
        materiales = new ArrayList<Material>();
        usuarios = new ArrayList<Usuario>();
        prestamos = new ArrayList<Prestamo>();
        SC = new Scanner(System.in);
    }

    public void agregarMaterialInicio(){
        Integer codigo;
        String titulo, opcion;
        System.out.println("---------------------------");
        opcion = leer("¿Qué material desea agregar [LI, RE, AU]?: ").toUpperCase();
        System.out.println("---------------------------");
        codigo = Integer.parseInt(leer("- Ingrese el código del material: "));
        titulo = leer("- Ingrese el titulo del material: ");
        switch (opcion) {
            case "LI" -> agregarLibro(codigo, titulo);
            case "RE" -> agregarRevista(codigo, titulo);
            case "AU" -> agregarAudioLibro(codigo, titulo);
            default -> System.out.println("[ERROR] ❌ Opción inválida. Reinténtelo.");
        }
    }

    public void agregarLibro(Integer codigo, String titulo) {
        String autor = leer("- Ingrese el autor del material: ");
        Integer anio = Integer.parseInt(leer("- Ingrese el año del material: "));
        String isbn = leer("- Ingrese el ISBN del material: ");
        try{
            Libro libro = new Libro(codigo,titulo,autor,anio,isbn);
            agregarMaterialFinal(libro);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
            System.err.println("[ERROR] ❌ Reintente el ingreso del material.");
        }
    }

    public void agregarRevista(Integer codigo, String titulo){
        Integer numeroEdicion = Integer.parseInt(leer("- Ingrese el número de edición del material: "));
        Integer periodicidad = Integer.parseInt(leer("- Ingrese la periodicidad del material: "));
        try{
            Revista revista = new Revista(codigo,titulo,numeroEdicion,periodicidad);
            agregarMaterialFinal(revista);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
            System.err.println("[ERROR] ❌ Reintente el ingreso del material.");
        }
    }

    public void agregarAudioLibro(Integer codigo, String titulo){
        String narrador = leer("- Ingrese el narrador del material: ");
        Integer duracion = Integer.parseInt(leer("- Ingrese la duración del material: "));
        try{
            AudioLibro audioLibro = new AudioLibro(codigo, titulo, narrador, duracion);
            agregarMaterialFinal(audioLibro);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
            System.err.println("[ERROR] ❌ Reintente el ingreso del material.");
        }
    }

    public void agregarMaterialFinal(Material material) {
        this.materiales.add(material);
        System.out.println("[MATERIAL] ✅ Materiales ingresados con éxito.");
    }

    public void mostrarMateriales(){
        for(Material mat : this.materiales){
            System.out.println(mat.toString());
        }
    }

    public void agregarUsuario(){
        String run = leer("- Ingrese el run del usuario: ");
        String nombre = leer("- Ingrese el nombre del usuario: ");
        String apellido = leer("- Ingrese el apellido del usuario: ");
        String email = leer("- Ingrese el email del usuario: ");
        Integer cupoAcual = Integer.parseInt(leer("- Ingrese el cupo de acual del usuario: "));
        Integer cupoMaximo = Integer.parseInt(leer("- Ingrese el cupo de maximo del usuario: "));
        try{
            Usuario usuario = new Usuario(run, nombre, apellido, email, cupoAcual, cupoMaximo);
            this.usuarios.add(usuario);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
            System.err.println("[ERROR] ❌ Reintente el ingreso del usuario.");
        }
    }

    public void mostrarMaterialesDisponibles(){
        for(Material mat : this.materiales){
            if(mat.getDisponible()) System.out.println(mat.toString());
        }
    }

    public void realizarPrestamo(){
        Integer codigo = Integer.parseInt(leer("- Ingrese el código del material a prestar: "));
        String run = leer("- Ingrese el run del usuario que recibirá el material prestado: ");
        String fechaP = leer("- Ingrese la fecha del préstamo: ");
        String fechaD = leer("- Ingrese la fecha de la devolución: ");
        Material material = buscarMaterial(codigo);
        try{
            Prestamo prestamo = new Prestamo(codigo, run, fechaP, fechaD, material);
            this.prestamos.add(prestamo);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
            System.err.println("[ERROR] ❌ Reintente el préstamo.");
        }
    }

    public void mostrarPrestamoDisponibles(){
        for(Prestamo prestamo : this.prestamos){
            System.out.println(prestamo.toString());
        }
    }

    public Material buscarMaterial(Integer codigo){
        for(Material matAux : this.materiales){
            if(matAux.getCodigo() == codigo) {
                return matAux;
            }
        }
        return null;
    }

    public void cerrarRecursosBiblioteca(){
        this.SC.close();
    }

    private String leer(String instruccion) {
        System.out.print(instruccion);
        return SC.nextLine();
    }
}
