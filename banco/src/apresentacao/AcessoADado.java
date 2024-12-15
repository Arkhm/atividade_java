package apresentacao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe responsável pelo acesso ao banco de dados PostgreSQL.
 */
public class AcessoADado {

    /**
     * Método para conectar ao banco de dados PostgreSQL
     *
     * @return Um objeto Connection
     * @throws SQLException Caso haja erro de conexão
     */
    public Connection connect() throws SQLException {
        String url = "jdbc:postgresql://localhost/banco";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "postgres");
        props.setProperty("ssl", "false");

        return DriverManager.getConnection(url, props);
    }

    /**
     * Cadastra uma nova conta.
     *
     * @param numero Número da conta
     * @param saldo  Saldo da conta
     * @return Mensagem de sucesso ou erro
     */
    public String cadastrar_conta(String numero, double saldo) {
        String SQL = "INSERT INTO public.conta(numero, saldo) VALUES (?, ?)";
        String mensagem;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, numero);
            pstmt.setDouble(2, saldo);
            pstmt.executeUpdate();
            mensagem = "Cadastro de conta " + numero + " realizado com sucesso.";

        } catch (SQLException ex) {
            mensagem = "Erro ao cadastrar conta: " + ex.getMessage();
        }
        return mensagem;
    }

    /**
     * Altera o saldo de uma conta existente.
     *
     * @param numero Número da conta
     * @param saldo  Novo saldo
     * @return Mensagem de sucesso ou erro
     */
    public String alterar_conta(String numero, double saldo) {
        String SQL = "UPDATE public.conta SET saldo = ? WHERE numero = ?";
        String mensagem;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setDouble(1, saldo);
            pstmt.setString(2, numero);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                mensagem = "Conta " + numero + " alterada com sucesso.";
            } else {
                mensagem = "Nenhuma conta encontrada com o número " + numero + ".";
            }

        } catch (SQLException ex) {
            mensagem = "Erro ao alterar conta: " + ex.getMessage();
        }
        return mensagem;
    }

    /**
     * Busca uma conta pelo número.
     *
     * @param numero Número da conta
     * @return Detalhes da conta ou mensagem de erro
     */
    public String buscar_conta(String numero) {
        String SQL = "SELECT numero, saldo FROM public.conta WHERE numero = ?";
        String mensagem;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, numero);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                mensagem = "Conta: " + rs.getString("numero") + ", Saldo: " + rs.getDouble("saldo");
            } else {
                mensagem = "Conta com número " + numero + " não encontrada.";
            }

        } catch (SQLException ex) {
            mensagem = "Erro ao buscar conta: " + ex.getMessage();
        }
        return mensagem;
    }

    /**
     * Deleta uma conta pelo número.
     *
     * @param numero Número da conta
     * @return Mensagem de sucesso ou erro
     */
    public String deletar_conta(String numero) {
        String SQL = "DELETE FROM public.conta WHERE numero = ?";
        String mensagem;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, numero);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                mensagem = "Conta " + numero + " deletada com sucesso.";
            } else {
                mensagem = "Nenhuma conta encontrada com o número " + numero + ".";
            }

        } catch (SQLException ex) {
            mensagem = "Erro ao deletar conta: " + ex.getMessage();
        }
        return mensagem;
    }

    /**
     * Exemplo de execução dos métodos da classe.
     */
    public static void main(String[] args) {
        AcessoADado acesso = new AcessoADado();

        // Testando os métodos
        System.out.println(acesso.cadastrar_conta("12345", 1000.0));
        System.out.println(acesso.buscar_conta("12345"));
        System.out.println(acesso.alterar_conta("12345", 1500.0));
        System.out.println(acesso.buscar_conta("12345"));
        System.out.println(acesso.deletar_conta("12345"));
        System.out.println(acesso.buscar_conta("12345"));
    }
}
