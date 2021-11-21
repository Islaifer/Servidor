package controller;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Servidor {
    public static void main(String[] args) {
        String trueUser = "islaifer";
        String truePass = "123456";
        System.out.println("Iniciando o servidor....");
        try {
            Scanner teclado = new Scanner(System.in);

            ServerSocket servidor = new ServerSocket(8080);
            Socket cliente = servidor.accept();


            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        Scanner entrada = new Scanner(cliente.getInputStream());
                        PrintStream saida = new PrintStream(cliente.getOutputStream());
                        if(entrada.hasNextLine()){
                            String userCript = entrada.nextLine();
                            String user = new String(Base64.getDecoder().decode(userCript.getBytes(UTF_8)), UTF_8);
                            saida.println("Senha:");
                            String passCript = entrada.nextLine();
                            String pass = new String(Base64.getDecoder().decode(passCript.getBytes(UTF_8)), UTF_8);
                            if(trueUser.equals(user) && truePass.equals(pass)){
                                saida.println("Conexão aceita, digite a equação:");
                                String equacaoCript = entrada.nextLine();
                                String equacao = new String(Base64.getDecoder().decode(equacaoCript.getBytes(UTF_8)), UTF_8);
                                ScriptEngineManager mgr = new ScriptEngineManager();
                                ScriptEngine engine = mgr.getEngineByName("JavaScript");
                                String resultado = String.valueOf(engine.eval(equacao));
                                saida.println(resultado);
                            }else{
                                saida.println("Conexão recusada, usuario ou senha invalidos!");
                            }
                        }
                        entrada.close();
                        cliente.close();
                        servidor.close();
                    } catch(Exception e) {

                    }
                }

            });
            t.start();
            // servidor.close();
        } catch (Exception e) {
            System.err.println("Deu ruim \n" + e.getMessage());
        }

    }
}
