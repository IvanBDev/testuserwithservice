package it.prova.dao.user;

import java.util.Date;
import java.util.List;

import it.prova.dao.IBaseDAO;
import it.prova.model.User;

public interface UserDAO extends IBaseDAO<User> {
	
	public List<User> findAllUsernameThatStartsWith(String iniziale) throws Exception;
	public List<User> findAllCreatedBefore(Date dataConfronto) throws Exception;
	public List<User> searchBySurnameAndNameThatStartsWith(String cognomeInput, String inzialeNomeInput) throws Exception;
	public User Login(String loginInput, String passwordInput) throws Exception;
	
}
