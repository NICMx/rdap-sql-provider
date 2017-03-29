package mx.nic.rdap.sql;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import mx.nic.rdap.db.exception.IpAddressFormatException;

public class IpUtils {

	private static final BigInteger FIRST_OCTECT_LIMIT = new BigInteger("4294967295"); // 0xFFFF_FFFF

	private static final int IPV4_ADDRESS_ARRAY_SIZE = 4;
	private static final int IPV6_ADDRESS_ARRAY_SIZE = 16;

	private static final int IPV6_PART_SIZE = 8;
	private static final BigInteger IPV4_MAX_VALUE = FIRST_OCTECT_LIMIT; // 0xFFFF_FFFF

	public static BigInteger addressToNumber(Inet4Address address) {
		byte[] byteAddress = address.getAddress();
		byte[] number = new byte[IPV4_ADDRESS_ARRAY_SIZE + 1];
		System.arraycopy(byteAddress, 0, number, 1, IPV4_ADDRESS_ARRAY_SIZE);
		return new BigInteger(number);
	}

	public static BigInteger inet6AddressToNumber(Inet6Address address) {
		byte[] byteAddress = address.getAddress();
		byte[] number = new byte[IPV6_ADDRESS_ARRAY_SIZE + 1];
		System.arraycopy(byteAddress, 0, number, 1, IPV6_ADDRESS_ARRAY_SIZE);
		return new BigInteger(number);
	}

	public static BigInteger inet6AddressToUpperPart(Inet6Address address) {
		return new BigInteger(toUpperPart(address.getAddress()));
	}

	public static BigInteger inet6AddressToLowerPart(Inet6Address address) {
		return new BigInteger(toLowerPart(address.getAddress()));
	}

	private static byte[] toUpperPart(byte[] address) {
		byte[] upper = new byte[IPV6_PART_SIZE + 1];
		System.arraycopy(address, 0, upper, 1, IPV6_PART_SIZE);
		return upper;
	}

	private static byte[] toLowerPart(byte[] address) {
		byte[] upper = new byte[IPV6_PART_SIZE + 1];
		System.arraycopy(address, IPV6_PART_SIZE, upper, 1, IPV6_PART_SIZE);
		return upper;
	}

	public static InetAddress numberToInet6(String upperPartNumber, String lowerPartNumber)
			throws IpAddressFormatException {
		long upper;
		long lower;
		try {
			upper = Long.parseUnsignedLong(upperPartNumber);
			lower = Long.parseUnsignedLong(lowerPartNumber);
		} catch (NumberFormatException e) {
			throw new IpAddressFormatException("Invalid IPv6 address.", e);
		}

		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * 2);
		buffer.putLong(upper);
		buffer.putLong(lower);

		try {
			return InetAddress.getByAddress(buffer.array());
		} catch (UnknownHostException e) {
			throw new RuntimeException("Programming error: Automatically-generated array has an invalid length.", e);
		}
	}

	public static InetAddress numberToInet4(String ipv4Number) throws IpAddressFormatException {
		BigInteger ipNumber = new BigInteger(ipv4Number);
		if (ipNumber.compareTo(IPV4_MAX_VALUE) > 0) {
			throw new IpAddressFormatException("Invalid IPv4 address: " + ipv4Number);
		}

		try {
			return InetAddress.getByName(ipv4Number);
		} catch (UnknownHostException e) {
			throw new RuntimeException("Programming error: Integer-formatted address triggered a lookup.", e);
		}
	}

}
