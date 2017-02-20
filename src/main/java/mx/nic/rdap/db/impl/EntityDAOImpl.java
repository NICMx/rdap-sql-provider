package mx.nic.rdap.db.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import mx.nic.rdap.core.db.Entity;
import mx.nic.rdap.db.DBConnection;
import mx.nic.rdap.db.exception.InvalidValueException;
import mx.nic.rdap.db.exception.RdapDatabaseException;
import mx.nic.rdap.db.model.EntityModel;
import mx.nic.rdap.db.spi.EntitySpi;
import mx.nic.rdap.db.struct.SearchResultStruct;

public class EntityDAOImpl implements EntitySpi {

	@Override
	public Entity getByHandle(String entityHandle) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return EntityModel.getByHandle(entityHandle, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public SearchResultStruct<Entity> searchByHandle(String handle, Integer resultLimit) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return EntityModel.searchByHandle(handle, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public SearchResultStruct<Entity> searchByVCardName(String vCardName, Integer resultLimit)
			throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return EntityModel.searchByVCardName(vCardName, resultLimit, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public SearchResultStruct<Entity> searchByRegexHandle(String regexHandle, Integer resultLimit)
			throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return EntityModel.searchByRegexHandle(regexHandle, resultLimit, connection);
		} catch (SQLSyntaxErrorException e) {
			throw new InvalidValueException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public SearchResultStruct<Entity> searchByRegexVCardName(String regexName, Integer resultLimit)
			throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			return EntityModel.searchByRegexName(regexName, resultLimit, connection);
		} catch (SQLSyntaxErrorException e) {
			throw new InvalidValueException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}
	}

	@Override
	public boolean existByHandle(String entityHandle) throws RdapDatabaseException {
		try (Connection connection = DBConnection.getConnection()) {
			EntityModel.existByHandle(entityHandle, connection);
		} catch (SQLException e) {
			throw new RdapDatabaseException(e);
		}

		return true;
	}

}
