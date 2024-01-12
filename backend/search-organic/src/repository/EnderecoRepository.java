package repository;
import exceptions.BancoDeDadosException;
import modelo.Endereco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoRepository implements Repository<Integer, Endereco> {


    @Override
    public Integer getProximoId(Connection connection) throws SQLException {
        String sql = "SELECT SUA_SEQUENCIA.NEXTVAL FROM DUAL";

        try (PreparedStatement pstd = connection.prepareStatement(sql);
             ResultSet res = pstd.executeQuery()) {
            if (res.next()) {
                return res.getInt(1);
            }
        }

        return null;
    }

    @Override
    public Endereco adicionar(Endereco endereco) throws BancoDeDadosException {
        Connection con = null;
        try {
            con = DriverManager.getConnection("vemser-dbc.dbccompany.com.br", "VS_13_EQUIPE_1", "oracle");
            Integer proximoId = getProximoId(con);

            String sql = "INSERT INTO ENDERECO (id_endereco, rua, numero, complemento, cep, cidade, estado, pais, regiao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstd = con.prepareStatement(sql)) {
                pstd.setInt(1, proximoId);
                pstd.setString(2, endereco.getLogradouro());
                pstd.setString(3, endereco.getNumero());
                pstd.setString(4, endereco.getComplemento());
                pstd.setString(5, endereco.getCep());
                pstd.setString(6, endereco.getCidade());
                pstd.setString(7, endereco.getEstado());
                pstd.setString(8, endereco.getPais());
                pstd.setString(9, endereco.getRegiao());

                int linhasAfetadas = pstd.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Endereço inserido com sucesso!");
                    endereco.setId(proximoId);
                } else {
                    System.out.println("Falha ao inserir endereço.");
                }
            }
            return endereco;
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
        try (Connection con = DriverManager.getConnection("vemser-dbc.dbccompany.com.br", "VS_13_EQUIPE_1", "oracle")) {
            String sql = "DELETE FROM ENDERECO WHERE id_endereco = ?";
            try (PreparedStatement pstd = con.prepareStatement(sql)) {
                pstd.setInt(1, id);

                int linhasAfetadas = pstd.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Endereço removido com sucesso!");
                    return true;
                } else {
                    System.out.println("Nenhum endereço removido. Verifique o ID.");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        }
    }


    @Override
    public boolean editar(Integer id, Endereco endereco) throws BancoDeDadosException {
        try (Connection con = DriverManager.getConnection("vemser-dbc.dbccompany.com.br", "VS_13_EQUIPE_1", "oracle")) {
            String sql = "UPDATE ENDERECO SET rua = ?, numero = ?, complemento = ?, cep = ?, cidade = ?, estado = ?, pais = ?, regiao = ? WHERE id_endereco = ?";
            try (PreparedStatement pstd = con.prepareStatement(sql)) {
                pstd.setString(1, endereco.getLogradouro());
                pstd.setString(2, endereco.getNumero());
                pstd.setString(3, endereco.getComplemento());
                pstd.setString(4, endereco.getCep());
                pstd.setString(5, endereco.getCidade());
                pstd.setString(6, endereco.getEstado());
                pstd.setString(7, endereco.getPais());
                pstd.setString(8, endereco.getRegiao());
                pstd.setInt(9, id);

                int linhasAfetadas = pstd.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("Endereço editado com sucesso!");
                    return true;
                } else {
                    System.out.println("Nenhum endereço editado. Verifique o ID.");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        }
    }


    @Override
    public List<Endereco> listar() throws BancoDeDadosException {
        List<Endereco> enderecos = new ArrayList<>();

        try (Connection con = DriverManager.getConnection("vemser-dbc.dbccompany.com.br", "VS_13_EQUIPE_1", "oracle")) {
            String sql = "SELECT * FROM ENDERECO";
            try (PreparedStatement pstd = con.prepareStatement(sql)) {
                try (ResultSet rs = pstd.executeQuery()) {
                    while (rs.next()) {
                        Endereco endereco = new Endereco(
                                rs.getString("rua"),
                                rs.getString("numero"),
                                rs.getString("complemento"),
                                rs.getString("cep"),
                                rs.getString("cidade"),
                                rs.getString("estado"),
                                rs.getString("pais")
                        );
                        endereco.setId(rs.getInt("id_endereco"));
                        endereco.setRegiao(rs.getString("regiao"));

                        enderecos.add(endereco);
                    }
                }
            }
        } catch (SQLException e) {
            throw new BancoDeDadosException(e.getCause());
        }

        return enderecos;
    }
}
