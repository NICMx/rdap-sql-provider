package mx.nic.rdap.sql.impl;

import java.sql.Connection;
import java.sql.SQLException;

import mx.nic.rdap.core.db.IpNetwork;
import mx.nic.rdap.core.ip.AddressBlock;
import mx.nic.rdap.db.exception.RdapDataAccessException;
import mx.nic.rdap.db.spi.IpNetworkDAO;
import mx.nic.rdap.sql.DatabaseSession;
import mx.nic.rdap.sql.model.IpNetworkModel;

public class IpNetworkDAOImpl implements IpNetworkDAO {

	@Override
	public IpNetwork getByAddressBlock(AddressBlock block) throws RdapDataAccessException {
		try (Connection connection = DatabaseSession.getRdapConnection()) {
			return IpNetworkModel.getByAddressBlock(block, connection);
		} catch (SQLException e) {
			throw new RdapDataAccessException(e);
		}
	}

}
