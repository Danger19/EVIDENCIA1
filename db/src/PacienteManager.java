import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PacienteManager {
    //Iniciamos con un mapa para almacenar a los pacientes
    //vamos a usar el ID como clave
    private Map<Integer, Paciente> pacientes;
    //Aquí definioms el nombre del archivo donde cargaremos
    //los datos
    private final String fileName = "pacientes.txt";
//Inicamos el constructor de la clase, cargamos los archivos al iniciar
    //con load desde el archivo e inicializamos el menú de la gestión
    //con startMenu
    public PacienteManager() {
        pacientes = new HashMap<>();
        load();
        startMenu();
    }
//Creamos un método para cargar los pacientes desde el archivo
    //Con el while leemos cada linea del mismo y lo dividimos
    //en partes con el delimitador ","
    //Se parsea la información y creamos un objeto paciente y finalmente
    //Lo agregamos al mapa
    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String nombre = parts[1];
                Paciente paciente = new Paciente(id, nombre);
                pacientes.put(id, paciente);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar la lista de pacientes.");
        }
    }
//Con este método vamos a guardar los pacientes en el archivo
    //Primero iteramos sobre el mapa y después escribimos la infromación en el archivo

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Integer, Paciente> entry : pacientes.entrySet()) {
                Paciente paciente = entry.getValue();
                bw.write(paciente.getId() + "," + paciente.getNombre() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la lista de pacientes.");
        }
    }
//Con este método vamos a obtener un paciente por su ID
    public Paciente getPacienteById(int id) {
        return pacientes.get(id);
    }
//Aqui mostramos una lista de los pacientes, itera sobre el mapa
    //y muestra la información
    public void listaPacientes() {
        System.out.println("Pacientes:");
        for (Map.Entry<Integer, Paciente> entry : pacientes.entrySet()) {
            Paciente paciente = entry.getValue();
            System.out.println(paciente.getId() + " : " + paciente.getNombre());
        }
    }
//Ahora seguimos con el método para crear un nuevo paciente
    //abrimos el scanner, solicitamos los datos, los almacenamos en sus
    //variables corresponientes y al final los asignamos al objeto Paciente
    //que agregamos al mapa
    private void crearPaciente(Scanner scanner) {
        try {
            System.out.print("Ingrese el ID del paciente: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Ingrese el nombre del paciente: ");
            String nombre = scanner.nextLine();
            Paciente paciente = new Paciente(id, nombre);
            pacientes.put(id, paciente);
            System.out.println("Paciente creado.");
        } catch (Exception e) {
            System.out.println("Error al ingresar datos. Asegúrese de ingresar valores válidos.");
        }
    }
//Aquí iniciamos el método para borrar a un paciente
    //Solicitamos el ID y lo guardamos en id comprobamos mediante
    // el if si en el mapa hay una entrada con esa id
    // si sí la hay se elimina y si no imprimimos mensaje indicando que
    //no se encontró al paciente
    private void borrarPaciente(Scanner scanner) {
        System.out.print("Ingrese el ID del paciente: ");
        int id = scanner.nextInt();
        if (pacientes.containsKey(id)) {
            pacientes.remove(id);
            System.out.println("Paciente eliminado.");
        } else {
            System.out.println("No se encontró el paciente.");
        }
    }
// Iniciamos con la variable exit como false para controlar el bucle
    //seguimos igual y creamos un objeto scanner, ejecutamos el while hasta
    //que exit sea false
    //Y mostramos el menú por medio de impresión de mensaje en consola
    // al final solicitamos seleccionar una opción y la almacenamos en choice
    //Y al final un swtich normal para llamar a los métodos
    public void startMenu() {
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        while (!exit) {
            System.out.println("\nGestión de Pacientes:");
            System.out.println("1. Lista de pacientes");
            System.out.println("2. Crear paciente");
            System.out.println("3. Eliminar paciente");
            System.out.println("4. Guardar y salir");

            System.out.print("Seleccione una opción: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listaPacientes();
                    break;
                case 2:
                    crearPaciente(scanner);
                    break;
                case 3:
                    borrarPaciente(scanner);
                    break;
                case 4:
                    save();
                    System.out.println("Cambios guardados. Saliendo del programa.");
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    public static void main(String[] args) {
        new PacienteManager();
    }
//Declaramos la clase Paciente
    public static class Paciente {
        private int id;
        private String nombre;

        public Paciente(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }
    }
}

