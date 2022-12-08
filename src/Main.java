import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    static Scanner myObj = new Scanner(System.in);
    static String opcao = "";
    static HashMap<String, String> users = new HashMap<>();

    public static String menuOperacoes() {
        return "\nRestaurante Gourmet - Operações\n\n" +
                "1 - Reservar mesa\n" +
                "2 - Cancelar mesa\n" +
                "3 - Listar mesas\n" +
                "0 - Sair\n";
    }

    public static String menuInicial() {
        return "\nBem vindo ao Restaurante Gourmet\n\n" +
                "1 - Login\n" +
                "2 - Registar\n" +
                "0 - sair\n";
    }

    public static String menuCliente() {
        return "\nRestaurante Gourmet - Clientes\n\n" +
                "1 - Cliente soap\n" +
                "2 - Cliente rest\n";
    }

    public static boolean esceveFicheiro(String email, String senha) {

        try {

            PrintWriter gravarArq = new PrintWriter(new FileOutputStream("src/user/users.txt", true), true);

            gravarArq.write(email + "//" + senha + "\n");

            gravarArq.flush();
            gravarArq.close();

            return true;
        } catch (java.io.FileNotFoundException ex) {
            return false;
        }
    }

    public static void leFicheiro() {

        try {
            FileInputStream is = new FileInputStream("src/user/users.txt");
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader leitor = new BufferedReader(isr);
            String linha;


            while ((linha = leitor.readLine()) != null) {
                String[] dados = linha.split("//");
                if (dados.length == 2) {
                    users.put(dados[0], dados[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String cifraSenha(String senha) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
        byte[] messageDigestSenhaAdmin = algorithm.digest(senha.getBytes("UTF-8"));

        StringBuilder hexStringSenhaAdmin = new StringBuilder();
        for (byte b : messageDigestSenhaAdmin) {
            hexStringSenhaAdmin.append(String.format("%02X", 0xFF & b));
        }

        return hexStringSenhaAdmin.toString();
    }

    public static boolean validaLogin(String email, String senha) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        senha = cifraSenha(senha);

        for (String i : users.keySet()) {

            if (email.equals(i) && senha.equals(users.get(i))) {
                return true;
            }
        }

        return false;
    }

    public static void registar() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String email;
        String senha;
        String confirmarSenha;
        boolean registo = false;

        do {
            boolean validaEmail = false;

            System.out.println("\nRestaurante Gourmet - Registar\n\n" +
                    "Email:");

            email = myObj.next();
            System.out.println("Senha:");
            senha = myObj.next();
            System.out.println("Confrime a senha");
            confirmarSenha = myObj.next();

            if (senha.equals(confirmarSenha)) {

                senha = cifraSenha(senha);

                for (String i : users.keySet()) {
                    if (email.equals(i)) {
                        System.out.println("Email já registado");
                        registo = true;
                        validaEmail = true;
                        break;
                    }
                }

                if (!validaEmail) {
                    if (esceveFicheiro(email, senha)) {
                        System.out.println("Registo feito com sucesso");
                        registo = true;
                    } else {
                        System.out.println("Erro no ficheiro - tente novamente!");
                    }
                }

            } else {
                System.out.println("Senhas diferentes - Tente novamente!");
            }

        } while (!registo);

    }

    public static void login() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String email;
        String senha;

        System.out.println("\nRestaurante Gourmet - Login\n\n" +
                "Email:");

        email = myObj.next();
        System.out.println("Senha:");
        senha = myObj.next();

        if (validaLogin(email, senha)) {
            do {
                System.out.println(menuCliente());
                opcao = myObj.next();

                switch (opcao) {
                    case "1":
                        cliente("soap");
                        opcao = "1";
                        break;
                    case "2":
                        cliente("rest");
                        opcao = "2";
                        break;
                    default:
                        System.out.println("Resposta invalida - tente novamente");
                }

            } while (!opcao.equals("1") && !opcao.equals("2"));
        } else {
            System.out.println("Login invalido - Tente novamente");
        }

    }

    public static void cliente(String tipoCliente) {

        do {
            System.out.println(menuOperacoes());
            opcao = myObj.next();

            switch (opcao) {
                case "1":
                    reservarMesa(tipoCliente);
                    break;
                case "2":
                    cancelarMesa(tipoCliente);
                    break;
                case "3":
                    ListarMesas(tipoCliente);
                    break;
                case "0":
                    System.out.println("Encerrando o login...");
                    break;
                default:
                    System.out.println("Resposta invalida - tente novamente");
            }

        } while (!opcao.equals("0"));

    }

    public static void reservarMesa(String tipoCliente) {
        System.out.println("Ainda fazer reservar mesa com o servidor");
    }

    public static void cancelarMesa(String tipoCliente) {
        System.out.println("Ainda fazer cancelar mesa com o servidor");
    }

    public static void ListarMesas(String tipoCliente) {
        System.out.println("Ainda fazer listar mesas com o servidor");
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        boolean sair = false;

        do {
            users.clear();
            leFicheiro();

            System.out.println(menuInicial());
            opcao = myObj.next();

            switch (opcao) {
                case "1":
                    login();
                    break;
                case "2":
                    registar();
                    break;
                case "0":
                    sair = true;
                    break;
                default:
                    System.out.println("Resposta invalida");
            }

        } while (!sair);
    }
}
