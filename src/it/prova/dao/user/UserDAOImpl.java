package it.prova.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.dao.AbstractMySQLDAO;
import it.prova.model.User;

public class UserDAOImpl extends AbstractMySQLDAO implements UserDAO {

	@Override
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<User> list() throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<User> result = new ArrayList<User>();
		User userTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("SELECT * FROM user")) {

			while (rs.next()) {
				userTemp = new User();
				userTemp.setNome(rs.getString("nome"));
				userTemp.setCognome(rs.getString("cognome"));
				userTemp.setLogin(rs.getString("login"));
				userTemp.setPassword(rs.getString("password"));
				userTemp.setDateCreated(rs.getDate("dateCreated"));
				userTemp.setId(rs.getLong("ID"));
				result.add(userTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public User get(Long idInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		User result = null;
		try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE id = ?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new User();
					result.setNome(rs.getString("nome"));
					result.setCognome(rs.getString("cognome"));
					result.setLogin(rs.getString("login"));
					result.setPassword(rs.getString("password"));
					result.setDateCreated(rs.getDate("dateCreated"));
					result.setId(rs.getLong("id"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(User utenteInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (utenteInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO user (nome, cognome, login, password, dateCreated) VALUES (?, ?, ?, ?, ?);")) {
			ps.setString(1, utenteInput.getNome());
			ps.setString(2, utenteInput.getCognome());
			ps.setString(3, utenteInput.getLogin());
			ps.setString(4, utenteInput.getPassword());
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(5, new java.sql.Date(utenteInput.getDateCreated().getTime()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(User utenteInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (utenteInput == null || utenteInput.getId() == null || utenteInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE user SET nome=?, cognome=?, login=?, password=?, dateCreated=? WHERE id=?;")) {
			ps.setString(1, utenteInput.getNome());
			ps.setString(2, utenteInput.getCognome());
			ps.setString(3, utenteInput.getLogin());
			ps.setString(4, utenteInput.getPassword());
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(5, new java.sql.Date(utenteInput.getDateCreated().getTime()));
			ps.setLong(6, utenteInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(User utenteInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (utenteInput == null || utenteInput.getId() == null || utenteInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM user WHERE id = ?")) {
			ps.setLong(1, utenteInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<User> findByExample(User example) throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (example == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<User> result = new ArrayList<User>();
		User userTemp = null;

		String query = "SELECT * FROM user WHERE 1=1 ";
		if (example.getCognome() != null && !example.getCognome().isEmpty()) {
			query += " AND cognome LIKE '" + example.getCognome() + "%' ";
		}
		if (example.getNome() != null && !example.getNome().isEmpty()) {
			query += " AND nome LIKE '" + example.getNome() + "%' ";
		}

		if (example.getLogin() != null && !example.getLogin().isEmpty()) {
			query += " AND login LIKE '" + example.getLogin() + "%' ";
		}

		if (example.getPassword() != null && !example.getPassword().isEmpty()) {
			query += " AND password LIKE '" + example.getPassword() + "%' ";
		}

		if (example.getDateCreated() != null) {
			query += " and dateCreated ='" + new java.sql.Date(example.getDateCreated().getTime()) + "' ";
		}

		try (Statement ps = connection.createStatement()) {
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				userTemp = new User();
				userTemp.setNome(rs.getString("nome"));
				userTemp.setCognome(rs.getString("cognome"));
				userTemp.setLogin(rs.getString("login"));
				userTemp.setPassword(rs.getString("password"));
				userTemp.setDateCreated(rs.getDate("dateCreated"));
				userTemp.setId(rs.getLong("id"));
				result.add(userTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<User> findAllUsernameThatStartsWith(String iniziale) throws Exception {
		// TODO Auto-generated method stub
		List<User> result = new ArrayList<User>();

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (iniziale == null || iniziale.isBlank())
			throw new Exception("Valore di input non ammesso.");

		try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE nome LIKE ?;")) {

			ps.setString(1, iniziale + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					User userTemp = new User();
					userTemp.setNome(rs.getString("nome"));
					userTemp.setCognome(rs.getString("cognome"));
					userTemp.setLogin(rs.getString("login"));
					userTemp.setPassword(rs.getString("password"));
					userTemp.setDateCreated(rs.getDate("dateCreated"));
					userTemp.setId(rs.getLong("id"));

					result.add(userTemp);
				} // niente catch qui

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			
			/*for (User userItem : result) {
				System.out.println(userItem);
			}*/

			return result;

		}
	}

	@Override
	public List<User> findAllCreatedBefore(Date dataConfronto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> searchBySurnameAndNameThatStartsWith(String cognomeInput, String inzialeNomeInput)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User Login(String loginInput, String passwordInput) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
