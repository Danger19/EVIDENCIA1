import java.util.Scanner;
//Comenzamos el interface, aquí primeo predefiní el usuario y
//contraseña los definí con algo sencillo 1234 los dos
public class MainInterface {
    private static final String USUA = "1234";
    private static final String CONTRA = "1234";
//Inicializamos el scanner para las entradas y solicitamos
    //usuario y contraseña y lo leemos
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre de usuario: ");
        String inputUsername = scanner.nextLine();
        System.out.print("Ingrese la contraseña: ");
        String inputPassword = scanner.nextLine();
//autenticamos el usuario y mostramos con impresión que el inicio fue
        //exitoso
        if (authenticate(inputUsername, inputPassword)) {
            System.out.println("Inicio de sesión exitoso. Bienvenido:" + USUA);
//inicializamos los gestores de las otras clases, esto lo hice porque
            //al correr esta clase se llamaban las otras clases y hasta el último
            //abría este menú, así que lo solucioné así
            DoctorManager doctorManager = null;
            PacienteManager pacienteManager = null;
            CitasManager citasManager = null;
//Aquí el menú del interface, funciona igual que los anteriores, mostramos el menú
            //por medio de impresiones y guardamos la eleccion en choice
            //Luego igual un switch y vamos llamando a cada método
            boolean exit = false;
            while (!exit) {
                System.out.println("\nMenú Principal:");
                System.out.println("1. Gestionar Doctores");
                System.out.println("2. Gestionar Pacientes");
                System.out.println("3. Gestionar Citas");
                System.out.println("4. Salir");

                System.out.print("Seleccione una opción: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        if (doctorManager == null) {
                            doctorManager = new DoctorManager();
                        }
                        doctorManager.startMenu();
                        break;
                    case 2:
                        if (pacienteManager == null) {
                            pacienteManager = new PacienteManager();
                        }
                        pacienteManager.startMenu();
                        break;
                    case 3:
                        if (citasManager == null) {
                            citasManager = new CitasManager(doctorManager, pacienteManager);
                        }
                        citasManager.startMenu(doctorManager, pacienteManager);
                        break;
                    case 4:
                        exit = true;
                        break;
                    default:
                        System.out.println("Comando no valido.");
                }
            }
        } else {
            System.out.println("Usuario o contraseña incorrectos. Cerrando programa...");
        }
    }
//Método para poder autenticar al usuario
    private static boolean authenticate(String usua, String contra) {
        return usua.equals(USUA) && contra.equals(CONTRA);
    }
}
