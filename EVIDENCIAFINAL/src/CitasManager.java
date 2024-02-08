import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
//Ahora esta clase si cambia, fue de lejos lo que más me costó
//Iniciamos con las varaibles de las otras clases que almacenan
//una instacia de sus clases para gestionar la infromación
//Luego hacemos la variable Map y delimitamos el nombre del archivo
//al que se cargaran los datos de las citas
public class CitasManager {
    private DoctorManager doctorManager;
    private PacienteManager pacienteManager;
    private Map<Integer, Cita> citas;
    private final String fileName = "citas.txt";
//Iniciamos con el constructor que recibe dos parametros
    //un objeto DoctorManger y uno CitaManager
    //Luego establecemos la variable doctorManager, de la clase Citasmanager
    //Con la instancia de DoctorManager para poder acceder a la gestión de la información
    //de doctores, hacemos exactamente lo mismo con pacienteManager
    //Inicializamos el mapa citas como objeto HashMap para almacenar las citas mientras
    //corre el programa
    //aquí sí es similar, cargamos la info con el load y inicializamos el menú con las instancias
    //antes mencionadas
    public CitasManager(DoctorManager doctorManager, PacienteManager pacienteManager) {
        this.doctorManager = doctorManager;
        this.pacienteManager = pacienteManager;
        this.citas = new HashMap<>();
        load();
        startMenu(doctorManager, pacienteManager);
    }
//Abrimos un bloque try para leer el archivo, usamos un BufferedReader.
    //Con el while leemos cada linea hasta llegar al final nnll, igual, dividimos lineas
    //Y hacemos un parseo de datos, ahora viene lo que personalmente me costó más trabajo
    //que fue empezar a usar las otras clases, usamos el DoctorManager
    //para obtener el objeto Doctor correspondiente al ID del doctor, lo mismo con paciente.
    //Con el if verificamos que los dos existan, creamos la instancia cita con los datos obtenidos
    //Y aquí igual la agregamos al mapa
    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String fechaHora = parts[1];
                String motivo = parts[2];
                int idDoctor = Integer.parseInt(parts[3]);
                int idPaciente = Integer.parseInt(parts[4]);

                DoctorManager.Doctor doctor = doctorManager.getDoctorById(idDoctor);
                PacienteManager.Paciente paciente = pacienteManager.getPacienteById(idPaciente);
                if (doctor != null && paciente != null) {
                    Cita cita = new Cita(id, fechaHora, motivo, doctor, paciente);
                    citas.put(id, cita);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar la lista de citas.");
        }
    }
//Sigamos con el método save, usamos el try para abrir un bloque para la escritura en el archivo
    //iteramos sobre las entradas con el for, luego obtenemos la instancia cita de la entrada
    //Después se escribe en el archivo con cada campo separado
    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<Integer, Cita> entry : citas.entrySet()) {
                Cita cita = entry.getValue();
                bw.write(cita.getId() + "," + cita.getFechaHora() + "," + cita.getMotivo() + ","
                        + cita.getDoctor().getId() + "," + cita.getPaciente().getId() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar la lista de citas.");
        }
    }
//Imprimimos el encabezado para indicar la lista, iteramos con el for
    //Obtenemos la instancia de cita e imprimimos los detalles tomando en cuenta
    //ID, fecha, hora, motivo, nombre del doctor y del paciente.
    public void listaCitas() {
        System.out.println("Citas:");
        for (Map.Entry<Integer, Cita> entry : citas.entrySet()) {
            Cita cita = entry.getValue();
            System.out.println(cita.getId() + " : " + cita.getFechaHora() + " - " + cita.getMotivo() +
                    " - Doctor: " + cita.getDoctor().getNombre() + " - Paciente: " + cita.getPaciente().getNombre());
        }
    }
//Seguimos con el método para crear una cita, empezamos por un proceso similar
    //Primero solicitamos por medio de impresiones los datos y los vamos guardando en
    //las variables id, fechaHora, etc. Aquí también batalle un poco, ahora usamos el
    //doctorManager.getDoctorById(idDoctor) y pacienteManager.getPacienteById(idPaciente)
    //y obtenemos las instancias de cada una
    //Creamos una instancia Cita con la información y se agrega al mapa, igual que en las otras
    //también comprobamos id para comprobar que exista
    private void crearCita(DoctorManager doctorManager, PacienteManager pacienteManager) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Ingrese el ID de la cita: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea pendiente

            System.out.print("Ingrese la fecha y hora de la cita (formato DD/MM/AAAA): ");
            String fechaHora = scanner.nextLine();

            System.out.print("Ingrese el motivo de la cita: ");
            String motivo = scanner.nextLine();

            System.out.print("Ingrese el ID del doctor: ");
            int idDoctor = scanner.nextInt();
            DoctorManager.Doctor doctor = doctorManager.getDoctorById(idDoctor);
            if (doctor == null) {
                System.out.println("No se encontró el doctor con ID " + idDoctor);
                return;
            }

            System.out.print("Ingrese el ID del paciente: ");
            int idPaciente = scanner.nextInt();
            PacienteManager.Paciente paciente = pacienteManager.getPacienteById(idPaciente);
            if (paciente == null) {
                System.out.println("No se encontró el paciente con ID " + idPaciente);
                return;
            }

            Cita cita = new Cita(id, fechaHora, motivo, doctor, paciente);
            citas.put(id, cita);
            System.out.println("Cita creada.");
        } catch (Exception e) {
            System.out.println("Error al ingresar datos. Asegúrese de ingresar valores válidos.");
        }
    }
//Imprimimos la solicitd para que se ingrese el ID, lo guardamos en id
    //lo buscamos en el mapa de citas y si existe la borramos, si no, mostramos
    //mensaje de que no se encontró, realmente es igual a las otras clases.
    private void borrarCita() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID de la cita: ");
        int id = scanner.nextInt();
        if (citas.containsKey(id)) {
            citas.remove(id);
            System.out.println("Cita eliminada.");
        } else {
            System.out.println("No se encontró la cita.");
        }
    }
//Finalmente aquí es lo mismo con la excepción de tomar las instacias de DoctorManager y PacienteManager
    //Y llamando a cada método, a partir de ahí es todo igual.
    public void startMenu(DoctorManager doctorManager, PacienteManager pacienteManager) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nGestión de Citas:");
            System.out.println("1. Lista de citas");
            System.out.println("2. Crear cita");
            System.out.println("3. Eliminar cita");
            System.out.println("4. Guardar y salir");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Seleccione una opción: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    listaCitas();
                    break;
                case 2:
                    crearCita(doctorManager, pacienteManager);
                    break;
                case 3:
                    borrarCita();
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
        PacienteManager pacienteManager = new PacienteManager();
        DoctorManager doctorManager = new DoctorManager();

        new CitasManager(doctorManager, pacienteManager);
    }

    public static class Cita {
        private int id;
        private String fechaHora;
        private String motivo;
        private DoctorManager.Doctor doctor;
        private PacienteManager.Paciente paciente;

        public Cita(int id, String fechaHora, String motivo, DoctorManager.Doctor doctor,
                    PacienteManager.Paciente paciente) {
            this.id = id;
            this.fechaHora = fechaHora;
            this.motivo = motivo;
            this.doctor = doctor;
            this.paciente = paciente;
        }

        public int getId() {
            return id;
        }

        public String getFechaHora() {
            return fechaHora;
        }

        public String getMotivo() {
            return motivo;
        }

        public DoctorManager.Doctor getDoctor() {
            return doctor;
        }

        public PacienteManager.Paciente getPaciente() {
            return paciente;
        }
    }
}
