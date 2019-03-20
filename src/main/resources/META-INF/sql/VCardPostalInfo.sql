#getAll
SELECT vpi_id, vca_id, vpi_type, vpi_country, vpi_country_code, vpi_city, vpi_street1, vpi_street2, vpi_street3, vpi_state, vpi_postal_code FROM {schema}.vcard_postal_info ORDER BY 1 ASC;

#getByVCardId
SELECT vpi_id, vca_id, vpi_type, vpi_country, vpi_country_code, vpi_city, vpi_street1, vpi_street2, vpi_street3, vpi_state, vpi_postal_code FROM {schema}.vcard_postal_info vpi WHERE vpi.vca_id = ?;

