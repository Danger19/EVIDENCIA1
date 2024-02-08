import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
//Iniciamoz igual con la clase y un mapa que va almacenar objetos
//de la clase Doctor, usamos el ID com clave e igual que en la otra
//clase delimitamos el nobre del archivo al que se cargaran los datos
public class DoctorManager {
    private Map<Integer, Doctor> doctores;
    private final String fileName = "doctores.txt";
//Inicializamos doctores, creando una nueva instncia HashMap
    //cargamos los datos desde el archivo e inicializamos el menú
    //realmente las clases son muy similares entre sí
    public DoctorManager() {
        doctores = new HashMap<>();
        load();
        startMenu();
    }
//Aquí se crea un BufferReaderpara leer desde el archivo, lo abrimos y se lee
    //igual que antes, lee cada linea y se divide con el delimitador
    //extraemos el primero elemento que es el id y lo mismo con el segundo
    //y tercero y se crea un objeto Doctor que se añade al mapa con el id como clave
    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String nombre = parts[1];
                String especialidad = parts[2];
                Doctor doctor = new Doctor(id, nombre, especialidad);
                doctores.put(id, doctor);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar la lista de doctores.");
        }
    }
//Seguimos con el método save, lo mismo se escribe en el archivo y se abre
    //con el for se itera atrabés de las entradas del mapa, obtenemos el objeto Doctor
    //Y se escribe la linea en el archivo
    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Integer, Doctor> entry : doctores.entrySet()) {
                Doctor doctor = entry.getValue();
                bw.write(doctor.getId() + "," + doctor.getNombre() + "," + doctor.getEspecialidad() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la lista de doctores.");
        }
    }
//Usamos el id para iddentificar la busqueda y nos devuelve el doctor asociado
    public Doctor getDoctorById(int id) {
        return doctores.get(id);
    }
//Imprimimos mensaje para indicar que se verá, luego se itera sobre las entradas
    //del mapa, obtiene el objeto Doctor e imprime la información del doctor
    public void listaDoctores() {
        System.out.println("Doctores:");
        for (Map.Entry<Integer, Doctor> entry : doctores.entrySet()) {
            Doctor doctor = entry.getValue();
            System.out.println(doctor.getId() + " : " + doctor.getNombre() + " - " + doctor.getEspecialidad());
        }
    }
//Aquí lo hice un poco diferente porque pense que era era importante
    //el id único así que primero hacemos una comprobaoón simple con el if
    //Y por medio de impresiones solicitamos los datos que vamos guardando
    //al igual que con los pacientes
    //Al final igual, creamos el objeto Doctor delimitado por las variables obtenidas
    //Se agrega al mapa y mostramos mensaje de que se hizo correctamente
    private void crearDoctor() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Ingrese el ID del doctor: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            if (doctores.containsKey(id)) {
                System.out.println("El ID del doctor ya existe. Intente con un ID diferente.");
                return;
            }

            System.out.print("Ingrese el nombre del doctor: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese la especialidad del doctor: ");
            String especialidad = scanner.nextLine();
            Doctor doctor = new Doctor(id, nombre, especialidad);
            doctores.put(id, doctor);
            System.out.println("Doctor creado.");
        } catch (Exception e) {
            System.out.println("Error al ingresar datos.");
        }
    }
//Solicitamos por medio de impresión la ID del doctor
    //leemos y almacenamos en id, verificamos que exista en el mapa
    //si sí exíste borramos y si no mostramos mensaje de que no se encontró
    //el doctor
    private void borrarDoctor() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID del doctor: ");
        int id = scanner.nextInt();
        if (doctores.containsKey(id)) {
            doctores.remove(id);
            System.out.println("Doctor eliminado.");
        } else {
            System.out.println("No se encontró el doctor.");
        }
    }
//Este menú funciona exactamente igual que el de pacientes
    public void startMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nGestión de Doctores:");
            System.out.println("1. Lista de doctores");
            System.out.println("2. Crear doctor");
            System.out.println("3. Eliminar doctor");
            System.out.println("4. Guardar y salir");

            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Seleccione una opción: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        listaDoctores();
                        break;
                    case 2:
                        crearDoctor();
                        break;
                    case 3:
                        borrarDoctor();
                        break;
                    case 4:
                        save();
                        System.out.println("Cambios guardados. Saliendo del programa.");
                        exit = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (Exception e) {
                System.out.println("Error al ingresar la opción. Asegúrese de ingresar un número.");
            }
        }
    }

    public static void main(String[] args) {
        new DoctorManager();
    }

    public static class Doctor {
        private int id;
        private String nombre;
        private String especialidad;

        public Doctor(int id, String nombre, String especialidad) {
            this.id = id;
            this.nombre = nombre;
            this.especialidad = especialidad;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public String getEspecialidad() {
            return especialidad;
        }
    }
}
