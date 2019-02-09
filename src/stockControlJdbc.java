/*
    TODO: tratar de reducir editarProducto() a otras funciones mas cortas
    TODO: ordenar por nombre, stock
*/
import java.sql.*;
import java.util.Scanner;

public class stockControlJdbc {
    static Scanner userInput = new Scanner(System.in);
    static Connection myConn = null;
    static Statement myStmt = null;  //Notar que es PreparedStatement
    static ResultSet myRs = null;

    static String dbUrl = "jdbc:mysql://127.0.0.1:3306/stock";
    static String user = "admin";
    static String pass = "admin";

    static String pruebaaaa = "Holaaaa estoy probando el branch!";
    static void separador(){
        System.out.println("-------------------------------------------------------------------------------------------");
    }
    static void mostrarMenu(){
        separador();
        System.out.println("C: Crear nuevo producto | M: Mostrar todos los productos | " +
                "E: Editar un producto por ID \nB: Borrar un producto por ID | X: Salir del sistema");
        separador();
    }
    static void crearProducto(){
        userInput.nextLine(); //Consume la linea
        System.out.println("Creando nuevo producto");
        separador();
        System.out.println("Nombre del producto:");
        String nombre = userInput.nextLine();
        System.out.println("Stock del producto:");
        int stock = userInput.nextInt();
        System.out.println("Precio del producto:");
        double precio = userInput.nextDouble();
        try {
            int affectedRows = myStmt.executeUpdate("insert into productos(nombre,stock,precio) values('"+nombre+"',"+stock+","+precio+")");
            System.out.println("¡El producto "+nombre+" fue creado exitosamente!");
        } catch (Exception exc) {
            System.out.println("¡Error creando el producto!");
            exc.printStackTrace();
        }
    }
    static void mostrarProductos(){
        System.out.println("Mostrando todos los productos");
        separador();
        try {
            myRs = myStmt.executeQuery("select * from productos");
            System.out.printf("%-5s | %-45s | %-5s | %-6s\n", "ID", "Nombre", "Stock", "Precio");
            System.out.println("------+-----------------------------------------------+-------+-----------");
            while (myRs.next()) {
                System.out.printf("#%-4d | %-45s | %-5d | $%-4.2f\n", myRs.getInt("id"), myRs.getString("nombre")
                , myRs.getInt("stock"), myRs.getDouble("precio"));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    static void editarProducto(){
        System.out.println("Ingrese el ID del producto a editar:");
        userInput.nextLine();
        int id = userInput.nextInt();
        try {
            myRs = myStmt.executeQuery("select * from productos where ID=" + id);
            myRs.next();
            String nombre= myRs.getString("nombre");
            int stock= myRs.getInt("stock");
            double precio=myRs.getDouble("precio");
            System.out.println("Usted va a editar:\n#"+ id +" | "+ nombre +" | Stock: "+stock+" | $"+precio);
            System.out.println("¿Desea continuar? - Presione 'S'");
            char cont = userInput.next().charAt(0);
            if (cont=='S'||cont=='s'){
                System.out.println("¿Cambiar nombre? - Presione 'S'");
                cont = userInput.next().charAt(0);
                if (cont=='S'||cont=='s'){
                    System.out.println("Ingrese el nuevo nombre:");
                    nombre = userInput.nextLine();
                }
                System.out.println("¿Cambiar stock? - Presione 'S'");
                cont = userInput.next().charAt(0);
                if (cont=='S'||cont=='s'){
                    System.out.println("Ingrese el nuevo stock:");
                    stock = userInput.nextInt();
                }
                System.out.println("¿Cambiar precio? - Presione 'S'");
                cont = userInput.next().charAt(0);
                if (cont=='S'||cont=='s'){
                    System.out.println("Ingrese el nuevo precio:");
                    precio = userInput.nextDouble();
                }
                separador();
                System.out.println(nombre+" | stock: "+stock+" | $"+precio+"\nConfirmar edición - Presione 'S'");
                cont = userInput.next().charAt(0);
                if (cont=='S'||cont=='s'){
                  int affectedRows = myStmt.executeUpdate("update productos set nombre='"+nombre+"', stock="+stock+",precio="+precio+"where ID="+id);
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    static void borrarProducto(){
        System.out.println("Ingrese el ID del producto a borrar");
        int id = userInput.nextInt();
        try {
            int affectedRows = myStmt.executeUpdate("delete from productos where id="+id);
            System.out.println("¡El producto fue borrado exitosamente!");
        } catch (Exception exc) {
            System.out.println("¡Error borrando el producto!");
            exc.printStackTrace();
        }
    }


    public static void main(String[] args) {
        System.out.println("Bienvenido al sistema de gestión\nConectando con base de datos...");
        //Conexión con base de datos
        try {
            myConn = DriverManager.getConnection(dbUrl, user, pass);
            myStmt = myConn.createStatement();
            System.out.println("Conexión exitosa");
        } catch (Exception exc) {
            exc.printStackTrace();
            System.out.println("Error conectando con base de datos.");
        }

        char tecla='0';
        do {
            mostrarMenu();
            /*try {
            tecla = (char)System.in.read();
            } catch (Exception e) {
                System.out.println("Error");
            }*/
            tecla = userInput.next().charAt(0);
            switch(tecla) {
                case 'C':
                case 'c':
                    crearProducto();
                    break;
                case 'M':
                case 'm':
                    mostrarProductos();
                    break;
                case 'E':
                case 'e':
                    editarProducto();
                    break;
                case 'B':
                case 'b':
                    borrarProducto();
                    break;
                case 'X':
                case 'x':
                    break;
                default:
                    System.out.println("Tecla incorrecta, intente nuevamente");
                    break;
            }
        } while (!(tecla=='X'||tecla=='x'));
    }
}