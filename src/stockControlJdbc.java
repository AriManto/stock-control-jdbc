import java.sql.*;
import java.util.Scanner;

public class stockControlJdbc {
    static Scanner userInput = new Scanner(System.in);
    private String nombre;
    private int stock;
    private double precio;
    private int id;
    private char cont;

    static Connection myConn = null;
    static Statement myStmt = null;
    static ResultSet myRs = null;

    static String dbUrl = "jdbc:mysql://127.0.0.1:3306/stock";
    static String user = "admin";
    static String pass = "admin";

    private void separador(){
        System.out.println("---------------------------------------------------------------------------------------------------");
    }
    private void mostrarMenu(){
        separador();
        System.out.println("C: Crear nuevo producto | M: Mostrar todos los productos por ID | E: Editar un producto por ID "
                +"\nB: Borrar un producto por ID | O: Ordenar lista por... | X: Salir del sistema");
        separador();
    }
    private void crearProducto(){
        System.out.println("Creando nuevo producto");
        separador();
        System.out.println("Nombre del producto:");
        String nombre = userInput.nextLine().toUpperCase();
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
    private void mostrarResultSet(){
        try {
            System.out.printf("%-5s | %-45s | %-5s | %-6s\n", "ID", "Nombre", "Stock", "Precio");
            System.out.println("------+-----------------------------------------------+-------+-----------");
            while (myRs.next()) {
                System.out.printf("#%-4d | %-45s | %-5d | $%-4.2f\n", myRs.getInt("id"), myRs.getString("nombre")
                        , myRs.getInt("stock"), myRs.getDouble("precio"));
            }
        } catch (Exception exc){
            exc.printStackTrace();
        }
    }
    private void mostrarProductosID(){
        System.out.println("Mostrando todos los productos por ID");
        separador();
        try {
            myRs = myStmt.executeQuery("select * from productos");
            mostrarResultSet();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private void seleccionarPorID(){
        try {
            System.out.println("Ingrese el ID del producto:");
            id = userInput.nextInt();
            userInput.skip("\n");
            myRs = myStmt.executeQuery("select * from productos where ID=" + id);
            myRs.next();
            nombre= myRs.getString("nombre");
            stock= myRs.getInt("stock");
            precio=myRs.getDouble("precio");
        }catch(Exception e){e.printStackTrace();}
    }
    private void cambiarValor(String columna){
        System.out.println("¿Cambiar "+ columna +"? - Presione 'S'");
        cont = userInput.next().charAt(0);
        userInput.nextLine();
        if (cont=='S'||cont=='s'){
            System.out.println("Ingrese el nuevo "+columna+":");
            if (columna.equalsIgnoreCase("nombre")){
                nombre = userInput.nextLine().toUpperCase();
            }
            else if (columna.equalsIgnoreCase("stock")){
                stock = userInput.nextInt();
                userInput.nextLine();
            }
            else if (columna.equalsIgnoreCase("precio")) {
                precio = userInput.nextDouble();
                userInput.nextLine();
            }
        }
    }
    private void editarProducto(){
        try {
            seleccionarPorID();
            System.out.println("Usted va a editar:\n#"+ id +" | "+ nombre +" | Stock: "+stock+" | $"+precio);
            System.out.println("¿Desea continuar? - Presione 'S'");
            cont = userInput.next().charAt(0);
            userInput.nextLine();
            if (cont=='S'||cont=='s'){
                cambiarValor("nombre");
                cambiarValor("stock");
                cambiarValor("precio");
                separador();
                System.out.println(nombre+" | stock: "+stock+" | $"+precio+"\nConfirmar edición - Presione 'S'");
                cont = userInput.next().charAt(0);
                userInput.nextLine();
                if (cont=='S'||cont=='s'){
                  int affectedRows = myStmt.executeUpdate("update productos set nombre='"+nombre+"', stock="+stock+",precio="+precio+"where ID="+id);
                    System.out.println("Edición exitosa");
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private void borrarProducto(){
        System.out.println("Ingrese el ID del producto a borrar");
        int id = userInput.nextInt();
        userInput.nextLine();
        try {
            int affectedRows = myStmt.executeUpdate("delete from productos where id="+id);
            System.out.println("¡El producto fue borrado exitosamente!");
        } catch (Exception exc) {
            System.out.println("¡Error borrando el producto!");
            exc.printStackTrace();
        }
    }

    private void nombreCrec(){
        try {
            myRs = myStmt.executeQuery("select * from productos order by nombre asc");
            mostrarResultSet();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private void nombreDecr(){
        try {
            myRs = myStmt.executeQuery("select * from productos order by nombre desc");
            mostrarResultSet();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private void stockCrec(){
        try {
            myRs = myStmt.executeQuery("select * from productos order by stock asc");
            mostrarResultSet();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private void stockDecr(){
        try {
            myRs = myStmt.executeQuery("select * from productos order by stock desc");
            mostrarResultSet();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private void precioCrec(){
        try {
            myRs = myStmt.executeQuery("select * from productos order by precio asc");
            mostrarResultSet();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private void precioDecr(){
        try {
            myRs = myStmt.executeQuery("select * from productos order by precio desc");
            mostrarResultSet();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private void ordenarProductos(){
        separador();
        System.out.println("Ordenar productos por: \n1. Nombre creciente | 2. Nombre decreciente | 3. Stock creciente "
        +"\n4. Stock decreciente | 5. Precio creciente | 6. Precio decreciente");
        separador();
        char tecla = userInput.next().charAt(0);
        switch  (tecla) {
            case '1': nombreCrec();
                break;
            case '2': nombreDecr();
                break;
            case '3': stockCrec();
                break;
            case '4': stockDecr();
                break;
            case '5': precioCrec();
                break;
            case '6': precioDecr();
                break;
            default:
                System.out.println("Opción incorrecta");
        }
    }

    public static void main(String[] args) {
        stockControlJdbc control = new stockControlJdbc();
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
            control.mostrarMenu();
            tecla = userInput.next().charAt(0);
            userInput.nextLine();
            switch(tecla) {
                case 'C':
                case 'c':
                    control.crearProducto();
                    break;
                case 'M':
                case 'm':
                    control.mostrarProductosID();
                    break;
                case 'E':
                case 'e':
                    control.editarProducto();
                    break;
                case 'B':
                case 'b':
                    control.borrarProducto();
                    break;
                case 'o':
                case 'O':
                    control.ordenarProductos();
                    break;
                case 'X':
                case 'x':
                    break;
                default:
                    System.out.println("Opción incorrecta, intente nuevamente");
                    break;
            }
        } while (!(tecla=='X'||tecla=='x'));
    }
}