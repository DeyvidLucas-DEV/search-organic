package repository;

import exceptions.BancoDeDadosException;
import exceptions.SenhaIncorretaException;
import exceptions.UsuarioJaCadastradoException;
import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository implements Repository<Integer, Usuario> {

    @Override
    public Integer getProximoId(Connection con) throws SQLException {
        String sql = "SELECT SEQ_USUARIO.nextval mysequence from DUAL";
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        if (res.next()) {
            return res.getInt("mysequence");
        }

        return null;
    }

    public Usuario buscaPorLogin(String login) throws BancoDeDadosException {
        Connection con = null;
        try {
            con = ConexaoBancoDeDados.getConnection();

            String sql = "SELECT * FROM Usuario WHERE LOGIN = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, login);

            ResultSet res = stmt.executeQuery();

            if (res.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(res.getInt("ID_USUARIO"));
                usuario.setIdEndereco(res.getInt("ID_ENDERECO"));
                usuario.setLogin(res.getString("LOGIN"));
                usuario.setPassword(res.getString("SENHA"));
                usuario.setCpf(res.getString("CPF"));
                usuario.setAtivo(res.getBoolean("ATIVO"));
                usuario.setNome(res.getString("NOME"));
                usuario.setSobrenome(res.getString("SOBRENOME"));
                usuario.setEmail(res.getString("EMAIL"));
                usuario.setDataNascimento(res.getDate("DATA_NASCIMENTO").toLocalDate());

                return usuario;
            }

            throw new RuntimeException("Usuário não encontrado com o login: " + login);
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public Usuario adicionar(Usuario usuario) throws BancoDeDadosException {
        Connection con = null;
        try {
            con = ConexaoBancoDeDados.getConnection();

            Integer proximoId = this.getProximoId(con);
            usuario.setIdUsuario(proximoId);

            String sql = "INSERT INTO Usuario\n" +
                    "(ID_USUARIO, ID_ENDERECO, LOGIN, SENHA, CPF, ATIVO, NOME, SOBRENOME, EMAIL, DATA_NASCIMENTO)\n" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setInt(2, usuario.getIdEndereco());
            stmt.setString(3, usuario.getLogin());
            stmt.setString(4, usuario.getPassword());
            stmt.setString(5, usuario.getCpf());
            stmt.setBoolean(6, usuario.isAtivo());
            stmt.setString(7, usuario.getNome());
            stmt.setString(8, usuario.getSobrenome());
            stmt.setString(9, usuario.getEmail());
            stmt.setDate(3, Date.valueOf(usuario.getDataNascimento()));

            int res = stmt.executeUpdate();
            if (res > 0) {
                System.out.println("Usuário adicionado com sucesso");
            } else {
                System.out.println("Ocorreu um erro ao adicionar");
            }

            return usuario;
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean remover(Integer id) throws BancoDeDadosException {
        Connection con = null;
        try {
            con = ConexaoBancoDeDados.getConnection();
            String sql = "DELETE FROM Usuario WHERE id_usuario = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            int res = stmt.executeUpdate();
            if (res > 0) {
                System.out.println("Usuário removido com sucesso");
                return true;
            }
            System.out.println("Não foi possível remover o usuário");

        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean editar(Integer id, Usuario usuario) throws BancoDeDadosException {
        Connection con = null;
        try {
            con = ConexaoBancoDeDados.getConnection();

            String sql = "UPDATE Usuario SET " +
                    "ID_ENDERECO = ?, " +
                    "LOGIN = ?, " +
                    "SENHA = ?, " +
                    "CPF = ?, " +
                    "ATIVO = ?, " +
                    "NOME = ?, " +
                    "SOBRENOME = ?, " +
                    "EMAIL = ?, " +
                    "DATA_NASCIMENTO = ? " +
                    "WHERE ID_USUARIO = ?";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, usuario.getIdEndereco());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getCpf());
            stmt.setBoolean(5, usuario.isAtivo());
            stmt.setString(6, usuario.getNome());
            stmt.setString(7, usuario.getSobrenome());
            stmt.setString(8, usuario.getEmail());
            stmt.setDate(3, Date.valueOf(usuario.getDataNascimento()));
            stmt.setInt(10, id);

            int res = stmt.executeUpdate();

            if (res > 0) {
                System.out.println("Usuário atualizado com sucesso");
                return true;
            }
            System.out.println("Ocorreu um erro ao atualizar o usuário");
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public List<Usuario> listar() throws BancoDeDadosException {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = null;
        try {
            con = ConexaoBancoDeDados.getConnection();
            Statement stmt = con.createStatement();

            String sql = "SELECT * FROM Usuario";

            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(res.getInt("ID_USUARIO"));
                usuario.setIdEndereco(res.getInt("ID_ENDERECO"));
                usuario.setLogin(res.getString("LOGIN"));
                usuario.setPassword(res.getString("SENHA"));
                usuario.setCpf(res.getString("CPF"));
                usuario.setAtivo(res.getBoolean("ATIVO"));
                usuario.setNome(res.getString("NOME"));
                usuario.setSobrenome(res.getString("SOBRENOME"));
                usuario.setEmail(res.getString("EMAIL"));
                usuario.setDataNascimento(res.getDate("DATA_NASCIMENTO").toLocalDate());

                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return usuarios;
    }
}
